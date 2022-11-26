package pe.edu.pucp.tdm.cliente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ClienteListaDispositivosAdapter extends RecyclerView.Adapter<ClienteListaDispositivosAdapter.DispositivosViewHolder> {

    private ArrayList<DispositivoDTO> listaDispositivos ;
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

    public  class DispositivosViewHolder extends RecyclerView.ViewHolder{
        DispositivoDTO dispositivoDTO;
        public DispositivosViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }



    @NonNull
    @Override
    public DispositivosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.itemrvcliente,parent,false);
        return new DispositivosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DispositivosViewHolder holder, int position) {
        DispositivoDTO d= listaDispositivos.get(position);
        holder.dispositivoDTO=d;
        TextView textViewNombre= holder.itemView.findViewById(R.id.textView23);
        textViewNombre.setText(d.getNombre());
    }

    @Override
    public int getItemCount() {
        return listaDispositivos.size();
    }
}
