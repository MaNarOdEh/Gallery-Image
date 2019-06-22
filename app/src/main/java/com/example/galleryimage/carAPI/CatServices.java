package com.example.galleryimage.carAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatServices {
   @GET("/v1/images/search")
    Call<List<ImageResult>> catsImageServices(@Query("x-api-key") String token);
}
