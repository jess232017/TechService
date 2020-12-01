package com.koopers.techservice.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.koopers.techservice.data.CategoriaRepository;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.network.Resource;

import java.util.List;

public class CategoriaViewModel extends ViewModel {
    private final CategoriaRepository categoriaRepository;
    private MutableLiveData<Resource<List<Categy>>> categy;

    public CategoriaViewModel(){
        categoriaRepository = new CategoriaRepository();
        categy = categoriaRepository.getCategory();
    }

    public LiveData<Resource<List<Categy>>> getCategory(){
        return categy;
    }

    public void forceUpdate(){
        categy.postValue(categoriaRepository.getCategory().getValue());
    }
}
