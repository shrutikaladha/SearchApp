package co.assignment.searchapp.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import co.assignment.searchapp.AppController;
import co.assignment.searchapp.model.Photo;
import co.assignment.searchapp.model.SearchModel;
import co.assignment.searchapp.utils.NetworkState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDataSource extends PageKeyedDataSource<Long, Photo> {

    private static final String TAG = FeedDataSource.class.getSimpleName();

    private AppController appController;

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private String queryText;

    public FeedDataSource(AppController appController, String queryText) {
        this.appController = appController;

        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.queryText = queryText;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Photo> callback) {
        if(queryText == null || queryText.trim().length() == 0) {
            return;
        }
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        appController.getRestApi().getSearchResults(queryText, params.requestedLoadSize, 1L)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        if(response.isSuccessful()) {
                            if(response.body().getPhotos() != null)
                            callback.onResult(response.body().getPhotos().getPhoto(), null, 2l);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Photo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Photo> callback) {
        Log.i(TAG, "Loading Range " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);

        appController.getRestApi().getSearchResults(queryText, params.requestedLoadSize, params.key).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if(response.isSuccessful()) {
                    if(response.body().getPhotos() != null && response.body().getPhotos().getTotal() != null) {
                        long nextKey = (params.key == Long.parseLong(response.body().getPhotos().getTotal())) ? null : params.key + 1;
                        callback.onResult(response.body().getPhotos().getPhoto(), nextKey);
                    }
                    networkState.postValue(NetworkState.LOADED);

                } else networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
