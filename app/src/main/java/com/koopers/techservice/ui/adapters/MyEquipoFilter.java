package com.koopers.techservice.ui.adapters;

import android.widget.Filter;

import com.koopers.techservice.data.local.entity.Equipo;

import java.util.ArrayList;
import java.util.List;

public class MyEquipoFilter extends Filter {
    private MyEquipo2 mAdapter;
    private boolean back;
    private List<Equipo> PurgeList;
    private List<Equipo> filterList;
    private List<Equipo> filteredDevice;

    MyEquipoFilter(MyEquipo2 mAdapter,List<Equipo> filterList, List<Equipo> PurgeList){
        super();
        this.back = false;
        this.mAdapter = mAdapter;
        this.PurgeList = PurgeList;
        this.filterList = filterList;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED DEVICES
            filteredDevice = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if( filterList.get(i).getMarca().toUpperCase().contains(constraint) ||
                        filterList.get(i).getModelo().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredDevice.add(filterList.get(i));
                }
            }

            results.count =filteredDevice.size();
            results.values = filteredDevice;
        }else{
            if(back){
                results.count  = PurgeList.size();
                filteredDevice = PurgeList;
                results.values = PurgeList;
            }else{
                results.count  = filterList.size();
                filteredDevice = filterList;
                results.values = filterList;
            }

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //REFRESH
        mAdapter.setmValues(filteredDevice);
    }

}