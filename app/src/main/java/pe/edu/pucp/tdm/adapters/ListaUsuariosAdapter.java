package pe.edu.pucp.tdm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.dto.UsuarioDTO;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioDTOViewHolder> {

    private ArrayList<UsuarioDTO> usuarioDTOS;
    private Context context;
    private OnItemClickListener detalles;

    public OnItemClickListener getDetalles() {
        return detalles;
    }

    public void setDetalles(OnItemClickListener detalles) {
        this.detalles = detalles;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public class UsuarioDTOViewHolder extends RecyclerView.ViewHolder{
        UsuarioDTO usuarioDTO;
        public UsuarioDTOViewHolder(@NonNull View itemView, OnItemClickListener detalles){
            super(itemView);
            Button btnDetalles = itemView.findViewById(R.id.btnDetallesListaUsuarios);
            btnDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detalles.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public UsuarioDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_usuarios,parent,false);
        return new UsuarioDTOViewHolder(itemView, detalles);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioDTOViewHolder holder, int position) {
        UsuarioDTO usuarioDTO = usuarioDTOS.get(position);
        holder.usuarioDTO=usuarioDTO;
        TextView nombre = holder.itemView.findViewById(R.id.textNombreListaUsuarios);
        TextView dni = holder.itemView.findViewById(R.id.textDNIListaUsuarios);
        nombre.setText("Nombre: "+usuarioDTO.getCorreo());
        dni.setText("DNI: "+usuarioDTO.getDNI());
    }

    @Override
    public int getItemCount() {
        return usuarioDTOS.size();
    }

    public ArrayList<UsuarioDTO> getList() {
        return usuarioDTOS;
    }

    public void setList(ArrayList<UsuarioDTO> list) {
        this.usuarioDTOS = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}