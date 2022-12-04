package pe.edu.pucp.tdm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.cliente.ClienteListaDispositivosAdapter;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ListaClientePedidosAdapter extends RecyclerView.Adapter<ListaClientePedidosAdapter.PedidoViewHolder> {
    private ArrayList<PedidoDTO> listaPedidos ;
    private Context context;

    public ArrayList<PedidoDTO> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(ArrayList<PedidoDTO> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public class  PedidoViewHolder extends RecyclerView.ViewHolder{
        PedidoDTO pedido;
        public PedidoViewHolder(@NonNull View itemView){
            super(itemView);
        }

    }


    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_cliente_pedidos_rv,parent,false);
        return new PedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        PedidoDTO d= listaPedidos.get(position);
        holder.pedido=d;
        TextView textViewNombre= holder.itemView.findViewById(R.id.textViewPedidosNombre);
        textViewNombre.setText(d.getNombreDispositivo());
        TextView textViewEstado = holder.itemView.findViewById(R.id.textViewPedidosEstado);
        textViewEstado.setText(d.getEstado());

    }

    @Override
    public int getItemCount() {return  listaPedidos.size();}
}
