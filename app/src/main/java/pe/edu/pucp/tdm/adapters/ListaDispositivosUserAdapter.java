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
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.dto.UsuarioDTO;

public class ListaDispositivosUserAdapter extends RecyclerView.Adapter<ListaDispositivosUserAdapter.PedidoDTOViewHolder> {

    private ArrayList<PedidoDTO> pedidoDTOS = new ArrayList<>();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_dispositivosuser,parent,false);
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
        //ARMAR LO QUE SE VA A MOSTRAR
        nombreDispositivo.setText(pedidoDTO.getNombreDispositivo());
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
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
