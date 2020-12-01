package com.koopers.techservice.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.activities.Detail_Activity;
import com.koopers.techservice.utils.image.QrScan;

import java.util.List;

public class MyEquipo2 extends RecyclerView.Adapter<MyEquipo2.ViewHolder> implements Filterable, SectionTitleProvider {
    private MyEquipoFilter filter;
    private List<Equipo> mValues;
    private List<Equipo> fValues;
    private final QrScan qrScan;
    private Context ctx;

    public MyEquipo2(Context ctx, List<Equipo> equipoList) {
        qrScan = new QrScan(ctx);
        mValues = equipoList;
        fValues = equipoList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.lytDevice.setVisibility(View.VISIBLE);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        holder.itemMarca.setText(holder.mItem.getMarca() + " " + holder.mItem.getModelo());
        holder.itemSpecy.setText(holder.mItem.getDescripcion());
        holder.itemCaty.setText(holder.mItem.getCategoria());
        holder.itemEstado.setText(holder.mItem.getEstado());

        holder.itemQrCode.setImageBitmap(qrScan.StringToQr(holder.mItem.getId() + ""));
        holder.itemQrCode.setOnClickListener(v -> qrScan.CustomDialog(holder.mItem, true));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, Detail_Activity.class);
            intent.putExtra("equipo", holder.mItem);
            ctx.startActivity(intent);
        });
    }

    public void setData(List<Equipo> newList, List<Equipo> newFullyList) {
        this.fValues = newFullyList;
        this.mValues = newList;
        notifyDataSetChanged();
    }

    void setmValues(List<Equipo> mValues) {
        this.mValues = mValues;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        return 0;
    }

    @Override
    public MyEquipoFilter getFilter() {
        if(filter == null){
            filter = new MyEquipoFilter(this, fValues, mValues);
        }
        return filter;
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return mValues.get(position).getMarca().substring(0, 1);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lytDevice;
        private ImageView itemQrCode;
        private TextView itemEstado;
        private TextView itemSpecy;
        private TextView itemMarca;
        private TextView itemCaty;
        private Equipo mItem;

        private ViewHolder(View view) {
            super(view);
            itemEstado = view.findViewById(R.id.itemEstado);
            itemQrCode = view.findViewById(R.id.itemQrCode);
            itemSpecy = view.findViewById(R.id.itemSpecy);
            itemMarca = view.findViewById(R.id.itemMarca);
            lytDevice = view.findViewById(R.id.lytDevice);
            itemCaty = view.findViewById(R.id.itemCaty);
        }
    }
}
