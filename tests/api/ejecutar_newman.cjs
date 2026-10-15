// Ejecuta la coleccion y publica solo resultados, sin requests ni respuestas privadas.
const fs = require('node:fs');
const path = require('node:path');
const root = path.resolve(__dirname, '../..');
const newman = require(path.join(root, 'app/build/newman-runtime/node_modules/newman'));
const readJson = file => JSON.parse(fs.readFileSync(file, 'utf8').replace(/^\uFEFF/, ''));
const local = path.join(__dirname, 'dev6.local.postman_environment.json');
if (!fs.existsSync(local)) {
    console.error('Falta el entorno privado dev6.local.postman_environment.json.');
    process.exit(2);
}
const privateEnv = readJson(local);
const publicEnv = readJson(path.join(__dirname, 'dev6.postman_environment.json'));
const values = new Map(publicEnv.values.map(v => [v.key, { ...v }]));
for (const v of privateEnv.values) values.set(v.key, { ...v });
for (const key of ['username', 'password', 'usernameAsesor', 'passwordAsesor']) {
    if (!values.get(key)?.value) {
        console.error(`Falta completar ${key} en el entorno privado.`);
        process.exit(2);
    }
}
const baseUrl = values.get('baseUrl')?.value;
if (baseUrl !== 'https://dev6.pythonanywhere.com') {
    console.error('El entorno debe apuntar al servidor de pruebas de EduTools por HTTPS.');
    process.exit(2);
}
// Las variables transitorias nunca se reutilizan de una corrida anterior.
for (const key of ['token', 'refresh', 'tokenAsesor', 'recursoId', 'copiaId', 'categoriaId', 'asignaturaId', 'usuarioId', 'qaUsername', 'qaPassword', 'qaResourceTitle']) {
    values.set(key, { key, value: '', enabled: true });
}
const collection = readJson(path.join(__dirname, 'EduTools.postman_collection.json'));
const started = new Date().toISOString();
newman.run({
    collection,
    environment: { ...publicEnv, values: [...values.values()] },
    reporters: [],
    timeoutRequest: 15000,
    timeoutScript: 10000,
    delayRequest: 150,
}, async (error, summary) => {
    if (error || !summary?.run) {
        console.error('No se pudo completar Newman. No se exportaron datos privados.');
        process.exitCode = 2;
        return;
    }
    const run = summary.run;
    // Limpieza defensiva: solo fixtures identificados de esta corrida.
    const cleanup = { deleted: 0, remaining: 0, errors: 0 };
    const token = summary.environment.get('token');
    const qaUsername = summary.environment.get('qaUsername');
    const qaTitle = summary.environment.get('qaResourceTitle');
    const request = async (route, method = 'GET') => fetch(baseUrl + route, {
        method, headers: { Authorization: 'Bearer ' + token }, signal: AbortSignal.timeout(15000),
    });
    if (token) {
        for (const key of ['copiaId', 'recursoId']) {
            const id = summary.environment.get(key);
            if (!id || !qaTitle) continue;
            try {
                const response = await request('/api/pedagogia/recursos/' + id + '/');
                if (response.status === 404) continue;
                if (response.status !== 200) throw new Error('No se pudo verificar fixture');
                const resource = await response.json();
                if (![qaTitle, qaTitle + ' editado', qaTitle + ' copia'].includes(resource.titulo)) {
                    cleanup.errors++; continue;
                }
                const result = await request('/api/pedagogia/recursos/' + id + '/', 'DELETE');
                if (result.status === 204) cleanup.deleted++; else cleanup.remaining++;
            } catch { cleanup.errors++; }
        }
        if (qaUsername) {
            try {
                const response = await request('/api/users/');
                if (response.status !== 200) throw new Error('No se pudo verificar fixtures');
                const users = await response.json();
                const names = [qaUsername, qaUsername + '_debil', qaUsername + '_sin_token'];
                for (const user of users.filter(u => names.includes(u.username))) {
                    const result = await request('/api/users/' + user.id + '/', 'DELETE');
                    if (result.status === 204) cleanup.deleted++; else cleanup.remaining++;
                }
            } catch { cleanup.errors++; }
        }
    } else { cleanup.errors++; }
    const stats = {};
    for (const key of ['iterations', 'items', 'scripts', 'prerequests', 'requests', 'tests', 'assertions', 'testScripts']) {
        const stat = run.stats[key];
        if (stat) stats[key] = { total: stat.total, pending: stat.pending, failed: stat.failed };
    }
    const executions = run.executions.map(e => ({
        item: { name: e.item.name },
        response: e.response ? { code: e.response.code, responseTime: e.response.responseTime } : null,
        assertions: (e.assertions || []).map(a => ({ assertion: a.assertion, failed: Boolean(a.error) })),
        requestFailed: Boolean(e.requestError),
    }));
    const failures = run.failures.map(f => ({
        source: { name: f.source?.name || 'Coleccion' },
        error: { name: f.error?.name || 'Error', test: f.error?.test || '' },
    }));
    const report = {
        collection: { info: { name: collection.info.name } },
        metadata: { started, finished: new Date().toISOString(), baseUrl, roles: ['admin', 'asesor'], sanitized: true, cleanup },
        run: { stats, executions, failures },
    };
    const output = path.join(__dirname, 'reporte_newman.json');
    fs.writeFileSync(output, JSON.stringify(report, null, 2) + '\n');
    console.log(`Newman: ${stats.requests.total} requests, ${stats.assertions.total} assertions, ${failures.length} fallas.`);
    console.log('Reporte sanitizado guardado en tests/api/reporte_newman.json.');
    failures.forEach(f => console.log(`Fallo: ${f.source.name} / ${f.error.test || f.error.name}`));
    console.log(`Limpieza: ${cleanup.remaining} fixtures pendientes, ${cleanup.errors} errores.`);
    process.exitCode = failures.length || cleanup.remaining || cleanup.errors ? 1 : 0;
});
