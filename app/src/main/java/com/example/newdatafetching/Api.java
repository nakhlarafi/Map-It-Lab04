package com.example.newdatafetching;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://anontech.info/courses/cse491/";

    @GET("employees.json")
    Call<List<Fetcher>> fetcher();
    //Call<List<FetcherLocation>> locFetcher();
}
