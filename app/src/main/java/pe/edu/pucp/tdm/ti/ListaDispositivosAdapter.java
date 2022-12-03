package pe.edu.pucp.tdm.ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaUsuariosAdapter;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ListaDispositivosAdapter extends RecyclerView.Adapter<ListaDispositivosAdapter.DispositivoViewHolder> {

    private ArrayList<DispositivoDTO> listaDispositivos;
    private Context context;
    private OnItemClickListener editar;

    public ArrayList<DispositivoDTO> getListaDispositivos() {
        return listaDispositivos;
    }

    public void setListaDispositivos(ArrayList<DispositivoDTO> listaDispositivos) {
        this.listaDispositivos = listaDispositivos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OnItemClickListener getEditar() {
        return editar;
    }

    public void setEditar(OnItemClickListener editar) {
        this.editar = editar;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public class DispositivoViewHolder extends RecyclerView.ViewHolder{
        DispositivoDTO dispositivo;
        public DispositivoViewHolder(@NonNull View itemView, ListaDispositivosAdapter.OnItemClickListener editar){
            super(itemView);
            Button btneditar = itemView.findViewById(R.id.btnEditar);
            btneditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editar.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public DispositivoViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.listardispositivos_rv, parent,false);
        return new DispositivoViewHolder(itemView,editar);
    }

    @Override
    public void onBindViewHolder(DispositivoViewHolder holder, int position) {
        DispositivoDTO d = listaDispositivos.get(position);
        holder.dispositivo = d;
        TextView textNombre = holder.itemView.findViewById(R.id.textNombre);
        TextView textTipo = holder.itemView.findViewById(R.id.textTipo);
        TextView textMarca = holder.itemView.findViewById(R.id.textMarca);
        TextView textCaracteristicas = holder.itemView.findViewById(R.id.textCaracteristicas);
        TextView textIncluye = holder.itemView.findViewById(R.id.textIncluye);
        TextView textStock = holder.itemView.findViewById(R.id.textStock);

        textNombre.setText(d.getNombre());
        textTipo.setText(d.getTipo());
        textMarca.setText(d.getMarca());
        textCaracteristicas.setText(d.getCaracteristicas());
        textIncluye.setText(d.getIncluye());
        textStock.setText(String.valueOf(d.getStock()));
    }

    @Override
    public int getItemCount(){
        return listaDispositivos.size();
    }
}
