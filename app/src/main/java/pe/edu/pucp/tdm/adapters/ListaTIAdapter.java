package pe.edu.pucp.tdm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.config.GlideConfig;
import pe.edu.pucp.tdm.dto.TIUserDTO;

public class ListaTIAdapter extends RecyclerView.Adapter<ListaTIAdapter.TIUserDTOViewHolder> {

    private ArrayList<TIUserDTO> list = new ArrayList<>();
    private Context context;
    private OnItemClickListener editar;
    private OnItemClickListener borrar;
    private RecyclerView.Adapter<ListaTIAdapter.TIUserDTOViewHolder> adapter;

    public OnItemClickListener getBorrar() {
        return borrar;
    }

    public void setBorrar(OnItemClickListener borrar) {
        this.borrar = borrar;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setEditar(OnItemClickListener editar) {
        this.editar = editar;
    }

    public class TIUserDTOViewHolder extends RecyclerView.ViewHolder{
        TIUserDTO tiUserDTO;
        public TIUserDTOViewHolder(@NonNull View itemView, OnItemClickListener editar,OnItemClickListener borrar){
            super(itemView);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminarListaTI);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    borrar.OnItemClick(getAdapterPosition());
                }
            });
            Button btnEditar = itemView.findViewById(R.id.btnEditarListaTI);
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editar.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public TIUserDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_list_tis,parent,false);
        return new TIUserDTOViewHolder(itemView, editar,borrar);
    }

    @Override
    public void onBindViewHolder(@NonNull TIUserDTOViewHolder holder, int position) {
        TIUserDTO tiUserDTO = list.get(position);
        holder.tiUserDTO=tiUserDTO;
        TextView nombre = holder.itemView.findViewById(R.id.textNombreListaTI);
        TextView dni = holder.itemView.findViewById(R.id.textDNIListaTI);
        ImageView imageView = holder.itemView.findViewById(R.id.imageVerPerfil);
        nombre.setText("Nombre: "+tiUserDTO.getNombres());
        dni.setText("DNI: "+tiUserDTO.getDni());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users/"+tiUserDTO.getDni()+"/photo.jpg");
        Glide.with(context).load(storageReference).into(imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<TIUserDTO> getList() {
        return list;
    }

    public void setList(ArrayList<TIUserDTO> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
