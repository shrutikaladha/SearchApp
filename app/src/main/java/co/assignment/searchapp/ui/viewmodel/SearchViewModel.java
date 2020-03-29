package co.assignment.searchapp.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import co.assignment.searchapp.AppController;
import co.assignment.searchapp.data.factory.FeedDataFactory;
import co.assignment.searchapp.model.Photo;
import co.assignment.searchapp.utils.NetworkState;

public class SearchViewModel extends ViewModel {

    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Photo>> photoLiveData;
    private String queryText = "";
    FeedDataFactory feedDataFactory;

    private AppController appController;
    public SearchViewModel(@NonNull AppController appController) {
        this.appController = appController;
        init();
    }

    private void init() {
        executor = Executors.newFixedThreadPool(5);
        feedDataFactory = new FeedDataFactory(appController, queryText);
        networkState = Transformations.switchMap(feedDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        photoLiveData = (new LivePagedListBuilder(feedDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Photo>> getArticleLiveData() {
        return photoLiveData;
    }

    public void updateQueryText(String text) {
        this.queryText = text;
        feedDataFactory.updateQueryText(queryText);
    }

}
