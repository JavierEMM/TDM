package pe.edu.pucp.tdm.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.cliente.ClienteDetallesDispositivoActivity;
import pe.edu.pucp.tdm.cliente.ClienteListaDispositivosAdapter;
import pe.edu.pucp.tdm.cliente.ClientePedidoAceptadoActivity;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ListaClientePedidosAdapter extends RecyclerView.Adapter<ListaClientePedidosAdapter.PedidoViewHolder> {
    private ArrayList<PedidoDTO> listaPedidos ;
    private ArrayList<PedidoDTO> listaOriginal;
    private Context context;

    public ArrayList<PedidoDTO> getListaPedidos() {
        return listaPedidos;
    }


    public void setListaPedidos(ArrayList<PedidoDTO> list){
        this.listaPedidos=list;
        listaOriginal=new ArrayList<>();
        listaOriginal.addAll(list);
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


        Button button = holder.itemView.findViewById(R.id.button4);
        if(d.getEstado().equals("pendiente") || d.getEstado().equals("rechazado")){
            button.setEnabled(false);
        }else{
            button.setEnabled(true);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClientePedidoAceptadoActivity.class);
                intent.putExtra("pedido",d);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {return  listaPedidos.size();}

    public void setList(ArrayList<PedidoDTO>list ){
        this.listaPedidos=list;
        listaOriginal= new ArrayList<>();
        listaOriginal.addAll(list);
    }

    public void filtrado(String txtBuscar){
        if(txtBuscar.equalsIgnoreCase("todos")){
            listaPedidos.clear();
            listaPedidos.addAll(listaOriginal);
        }else{
            listaPedidos.clear();
            for(PedidoDTO d : listaOriginal){
                Log.d("msg","El estado es "+d.getEstado());
                if(d.getEstado().toLowerCase().equalsIgnoreCase(txtBuscar)){
                    listaPedidos.add(d);
                    Log.d("msg","El caso coincide");
                }
            }
        }
        notifyDataSetChanged();
    }




}
