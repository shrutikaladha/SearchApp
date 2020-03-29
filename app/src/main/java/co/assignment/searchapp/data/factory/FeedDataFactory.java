package co.assignment.searchapp.data.factory;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import co.assignment.searchapp.AppController;
import co.assignment.searchapp.data.FeedDataSource;

public class FeedDataFactory extends DataSource.Factory {

    private MutableLiveData<FeedDataSource> mutableLiveData;
    private FeedDataSource feedDataSource;
    private AppController appController;
    private String queryText;

    public FeedDataFactory(AppController appController, String queryText) {
        this.appController = appController;
        this.mutableLiveData = new MutableLiveData<FeedDataSource>();
        this.queryText = queryText;
    }

    @NonNull
    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource(appController, queryText);
        mutableLiveData.postValue(feedDataSource);
        return feedDataSource;
    }

    public MutableLiveData<FeedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public void updateQueryText(String queryText) {
        this.queryText = queryText;
        feedDataSource.invalidate();
    }
}
