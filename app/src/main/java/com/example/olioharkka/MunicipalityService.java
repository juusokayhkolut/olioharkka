package com.example.olioharkka;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MunicipalityService {
    @GET("municipality/details")
    Call<MunicipalityDetails> getMunicipalityDetails(@Query("name") String name);
}
