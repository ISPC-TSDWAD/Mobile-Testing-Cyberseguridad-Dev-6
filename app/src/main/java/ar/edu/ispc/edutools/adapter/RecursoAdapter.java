package ar.edu.ispc.edutools.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.ispc.edutools.R;
import ar.edu.ispc.edutools.model.Recurso;

/**
 * Adapter del listado de recursos pedagogicos. Cada fila arma un
 * contentDescription compuesto para que TalkBack la lea en un solo foco.
 */
public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder> {

    /** La Activity decide que hacer con el elemento tocado. */
    public interface OnRecursoClickListener {
        void onRecursoClick(Recurso recurso);
    }

    private final List<Recurso> recursos;
    private final OnRecursoClickListener listener;

    public RecursoAdapter(List<Recurso> recursos, OnRecursoClickListener listener) {
        this.recursos = recursos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recurso, parent, false);
        return new RecursoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecursoViewHolder holder, int position) {
        Recurso recurso = recursos.get(position);

        holder.tvTitulo.setText(recurso.getTitulo());
        holder.tvTipo.setText(recurso.getTipoLegible());
        holder.tvAsignatura.setText(recurso.getAsignatura());

        holder.itemView.setContentDescription(
                recurso.getTitulo() + ". Tipo: " + recurso.getTipoLegible()
                        + ". Asignatura: " + recurso.getAsignatura());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecursoClick(recurso);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recursos == null ? 0 : recursos.size();
    }

    static class RecursoViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitulo;
        final TextView tvTipo;
        final TextView tvAsignatura;

        RecursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvItemTitulo);
            tvTipo = itemView.findViewById(R.id.tvItemTipo);
            tvAsignatura = itemView.findViewById(R.id.tvItemAsignatura);
        }
    }
}
