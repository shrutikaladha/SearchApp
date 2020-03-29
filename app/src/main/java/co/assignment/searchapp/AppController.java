package co.assignment.searchapp;

import android.app.Application;
import android.content.Context;

import co.assignment.searchapp.network.RestApi;
import co.assignment.searchapp.network.RestApiFactory;

public class AppController extends Application {
    private RestApi restApi;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public static AppController create(Context context) {
        return AppController.get(context);
    }

    public RestApi getRestApi() {
        if(restApi == null) {
            restApi = RestApiFactory.create();
        }
        return restApi;
    }

    public void setRestApi(RestApi restApi) {
        this.restApi = restApi;
    }
}
