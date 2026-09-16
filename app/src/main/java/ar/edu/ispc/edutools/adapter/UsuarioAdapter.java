package ar.edu.ispc.edutools.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.ispc.edutools.R;
import ar.edu.ispc.edutools.model.UsuarioItem;

/** Adapter del listado de usuarios. */
public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    public interface OnUsuarioClickListener {
        void onUsuarioClick(UsuarioItem usuario);
    }

    private final List<UsuarioItem> usuarios;
    private final OnUsuarioClickListener listener;

    public UsuarioAdapter(List<UsuarioItem> usuarios, OnUsuarioClickListener listener) {
        this.usuarios = usuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioItem usuario = usuarios.get(position);

        holder.tvNombre.setText(usuario.getNombreCompleto());
        holder.tvRol.setText(usuario.getRolLegible());
        holder.tvEstado.setText(usuario.getEstadoLegible());

        holder.itemView.setContentDescription(
                usuario.getNombreCompleto() + ". Rol: " + usuario.getRolLegible()
                        + ". Estado: " + usuario.getEstadoLegible());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUsuarioClick(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios == null ? 0 : usuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        final TextView tvNombre;
        final TextView tvRol;
        final TextView tvEstado;

        UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvRol = itemView.findViewById(R.id.tvItemRol);
            tvEstado = itemView.findViewById(R.id.tvItemEstado);
        }
    }
}
