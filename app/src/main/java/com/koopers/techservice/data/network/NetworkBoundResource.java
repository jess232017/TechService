package com.koopers.techservice.data.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.koopers.techservice.data.local.entity.Equipo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    protected NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createCall().enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(@NonNull Call<RequestType> call, @NonNull Response<RequestType> response) {
                result.removeSource(dbSource);
                saveResultAndReInit(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RequestType> call, @NonNull Throwable t) {
                onFetchFailed();
                result.removeSource(dbSource);
                result.addSource(dbSource, newData -> result.setValue(Resource.error(t.getMessage(), newData)));
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    private void saveResultAndReInit(RequestType response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), newData -> result.setValue(Resource.success(newData)));
            }
        }.execute();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    private boolean shouldFetch(@Nullable ResultType data) {
        return true;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract boolean shouldFetch(@Nullable List<Equipo> data);

    @NonNull
    @MainThread
    protected abstract Call<RequestType> createCall();

    @MainThread
    private void onFetchFailed() {
    }

    public final MutableLiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
