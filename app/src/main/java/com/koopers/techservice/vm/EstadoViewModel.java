package com.koopers.techservice.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.koopers.techservice.data.EstadoRepository;
import com.koopers.techservice.data.local.entity.Estado;
import com.koopers.techservice.data.network.Resource;

import java.util.List;

public class EstadoViewModel extends ViewModel {
    private final EstadoRepository estadoRepository;
    private MutableLiveData<Resource<List<Estado>>> status;

    public EstadoViewModel(){
        estadoRepository = new EstadoRepository();
        status = estadoRepository.getStatus();
    }

    public LiveData<Resource<List<Estado>>> getStatus(){
        return status;
    }

    public void forceUpdate(){
        status.postValue(estadoRepository.getStatus().getValue());
    }
}
