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
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ListaDispositivosAdminAdapter extends RecyclerView.Adapter<ListaDispositivosAdminAdapter.PedidoDTOViewHolder> {

    private ArrayList<PedidoDTO> pedidoDTOS = new ArrayList<>();
    private ArrayList<PedidoDTO> listaOriginal;
    private Context context;

    public class PedidoDTOViewHolder extends RecyclerView.ViewHolder{
        PedidoDTO pedidoDTO;
        public PedidoDTOViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public PedidoDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_dispositivos_admin,parent,false);
        return new PedidoDTOViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoDTOViewHolder holder, int position) {
        PedidoDTO pedidoDTO = pedidoDTOS.get(position);
        holder.pedidoDTO=pedidoDTO;
        TextView nombreDispositivo = holder.itemView.findViewById(R.id.textNombreListaDispositivos);
        TextView tiempoPrestamo = holder.itemView.findViewById(R.id.textTiempoPrestamo);
        TextView motivoPrestamo = holder.itemView.findViewById(R.id.textMotivoPrestamo);
        TextView cursoPrestamo = holder.itemView.findViewById(R.id.textCursoPrestamo);
        TextView estadoPrestamo = holder.itemView.findViewById(R.id.textEstadoPrestamo);
        TextView codigoPrestamo = holder.itemView.findViewById(R.id.textCodigoPUCPPrestado);
        //ARMAR LO QUE SE VA A MOSTRAR
        nombreDispositivo.setText(pedidoDTO.getNombreDispositivo());
        codigoPrestamo.setText("Codigo: " + pedidoDTO.getCodigoPUCP());
        tiempoPrestamo.setText("Tiempo: " + pedidoDTO.getTiempo());
        motivoPrestamo.setText("Motivo: " + pedidoDTO.getMotivo());
        cursoPrestamo.setText("Curso: " + pedidoDTO.getCurso());
        estadoPrestamo.setText("Estado: " + pedidoDTO.getEstado());
    }

    @Override
    public int getItemCount() {
        return pedidoDTOS.size();
    }

    public ArrayList<PedidoDTO> getList() {
        return pedidoDTOS;
    }

    public void setList(ArrayList<PedidoDTO> list) {
        this.pedidoDTOS = list;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(list);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void filtrado(String txtBuscar){
        int size = txtBuscar.length();
        if(size == 0){
            pedidoDTOS.clear();
            pedidoDTOS.addAll(listaOriginal);
        }else{
            pedidoDTOS.clear();
            for(PedidoDTO d : listaOriginal){
                if(d.getNombreDispositivo().toLowerCase().contains(txtBuscar)){
                    pedidoDTOS.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }
}
