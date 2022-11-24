package pe.edu.pucp.tdm.ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class PedidosActivityAdapter extends RecyclerView.Adapter<PedidosActivityAdapter.PedidoViewHolder>{

    private ArrayList<PedidoDTO> listaPedidos;
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

    public class PedidoViewHolder extends RecyclerView.ViewHolder{
        PedidoDTO pedido;

        public PedidoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.listardispositivos_rv, parent,false);
        return new PedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        PedidoDTO p = listaPedidos.get(position);
        holder.pedido = p;
        TextView textNombre = holder.itemView.findViewById(R.id.textNombreP);
        TextView textMotivo = holder.itemView.findViewById(R.id.textMotivo);
        TextView textCurso = holder.itemView.findViewById(R.id.textCurso);
        TextView textTiempo = holder.itemView.findViewById(R.id.textTiempo);
        TextView textPrograma = holder.itemView.findViewById(R.id.textPrograma);
        TextView textCodigo = holder.itemView.findViewById(R.id.textCodigo);
        TextView textOtros = holder.itemView.findViewById(R.id.textOtros);
        TextView textEstado = holder.itemView.findViewById(R.id.textEstado);

        textNombre.setText(p.getNombreDispositivo());
        textMotivo.setText(p.getMotivo());
        textCurso.setText(p.getCurso());
        textTiempo.setText(p.getTiempo());
        textPrograma.setText(p.getProgramas());
        textCodigo.setText(p.getCodigoPUCP());
        textOtros.setText(p.getOtros());
        textEstado.setText(p.getEstado());
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }
}
