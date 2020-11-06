package com.deepesh.memezter;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemeAPI {


    @GET("gimme")
    Call<MemeModal> getMeme();

}
