package com.koopers.techservice.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.activities.Detail_Activity;
import com.koopers.techservice.utils.image.QrScan;

import java.util.List;

public class MyEquipo extends RecyclerView.Adapter<MyEquipo.ViewHolder> {
    private final QrScan qrScan;
    private List<Equipo> mValues;
    private Context ctx;

    public MyEquipo(Context ctx, List<Equipo> equipoList) {
        qrScan = new QrScan(ctx);
        mValues = equipoList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_equipo1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.itemMarca.setText(holder.mItem.getMarca());
        holder.itemEstado.setText(holder.mItem.getEstado());
        holder.itemSerie.setText(String.valueOf(holder.mItem.getModelo()));
        holder.itemQrCode.setImageBitmap(qrScan.StringToQr(holder.mItem.getId() + ""));

        holder.itemQrCode.setOnClickListener(v -> qrScan.CustomDialog(holder.mItem,true));

        holder.itemVer.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, Detail_Activity.class);
            intent.putExtra("equipo", holder.mItem);
            ctx.startActivity(intent);
        });
    }

    public void setData(List<Equipo> newList) {
        this.mValues = newList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemSerie;
        private TextView itemMarca;
        private TextView itemEstado;
        private ImageView itemQrCode;
        private Button itemVer;
        private Equipo mItem;

        private ViewHolder(View view) {
            super(view);
            itemVer = itemView.findViewById(R.id.itemVer);
            itemSerie = itemView.findViewById(R.id.itemSerie);
            itemMarca = itemView.findViewById(R.id.itemMarca);
            itemEstado = itemView.findViewById(R.id.itemEstado);
            itemQrCode = itemView.findViewById(R.id.itemQrCode);
        }
    }
}
