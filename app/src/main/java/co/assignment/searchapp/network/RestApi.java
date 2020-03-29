package co.assignment.searchapp.network;

import co.assignment.searchapp.model.SearchModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("/services/rest")
    Call<SearchModel> getSearchResults(@Query("text") String text,
                                       @Query("per_page") int numItems,
                                       @Query("page") Long pageNo);
}
