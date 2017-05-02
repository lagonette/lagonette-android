package org.lagonette.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lagonette.android.api.adapter.LongTypeAdapter;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface LaGonetteService {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://82.225.211.150:18001/dolibarr/htdocs/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @GET("partnersinterface.php?format=app_partners")
    Call<PartnersResponse> getPartners();

    @GET("partnersinterface.php?format=app_categories")
    Call<CategoriesResponse> getCategories();

}

