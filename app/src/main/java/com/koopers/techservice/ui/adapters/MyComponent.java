package com.koopers.techservice.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Elemento;

import java.util.List;

public class MyComponent extends RecyclerView.Adapter<MyComponent.ViewHolder> {
    private List<Elemento> mValues;

    public MyComponent(List<Elemento> equipoList) {
        mValues = equipoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_elemento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Elemento mItem = mValues.get(position);
        holder.setDate.setText(mItem.getHora());
        holder.setTitulo.setText(mItem.getTitulo());
        holder.setEstado.setText(mItem.getEstado());
        holder.setDisponible.setChecked(mItem.getDisponibilidad()==0);
        holder.setDisponible.setOnCheckedChangeListener((buttonView, isChecked) ->
                holder.setDisponible.setChecked(mItem.getDisponibilidad()==0));
        holder.setDescripcion.setText(mItem.getDescripcion());
    }

    public void setData(List<Elemento> newList) {
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
        private TextView setDate;
        private TextView setTitulo;
        private TextView setEstado;
        private CheckBox setDisponible;
        private TextView setDescripcion;

        private ViewHolder(View view) {
            super(view);
            setDate = itemView.findViewById(R.id.setDate);
            setTitulo = itemView.findViewById(R.id.setTitulo);
            setEstado = itemView.findViewById(R.id.setEstado);
            setDisponible = itemView.findViewById(R.id.setDisponible);
            setDescripcion = itemView.findViewById(R.id.setDescripcion);
        }
    }
}
