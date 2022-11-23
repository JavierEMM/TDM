package pe.edu.pucp.tdm.ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ListaDispositivosAdapter extends RecyclerView.Adapter<ListaDispositivosAdapter.DispositivoViewHolder> {

    private ArrayList<DispositivoDTO> listaDispositivos;
    private Context context;

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

    public class DispositivoViewHolder extends RecyclerView.ViewHolder{
        DispositivoDTO dispositivo;

        public DispositivoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public DispositivoViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.listardispositivos_rv, parent,false);
        return new DispositivoViewHolder(itemView);
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
    }

    @Override
    public int getItemCount(){
        return listaDispositivos.size();
    }
}
