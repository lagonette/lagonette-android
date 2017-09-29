package org.lagonette.app.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lagonette.app.api.adapter.BooleanTypeAdapter;
import org.lagonette.app.api.adapter.LongTypeAdapter;
import org.lagonette.app.api.adapter.PartnerListTypeAdapter;
import org.lagonette.app.api.adapter.PartnerTypeAdapter;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Partner;
import org.lagonette.app.api.response.PartnersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

// TODO Separate retrofit builder into 2
public interface LaGonetteService {

    // TODO put this into a Locator
    Gson defaultGson = new GsonBuilder()
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
            .create();

    PartnerListTypeAdapter PARTNER_LIST_TYPE_ADAPTER = new PartnerListTypeAdapter(defaultGson);

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(List.class, new PartnerListTypeAdapter(defaultGson))
            .registerTypeAdapter(Partner.class, new PartnerTypeAdapter(defaultGson, PARTNER_LIST_TYPE_ADAPTER))
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://dolibarr.lagonette.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @GET("partnersinterface.php?format=app_partners")
    Call<PartnersResponse> getPartners();

    @GET("partnersinterface.php?format=app_categories")
    Call<CategoriesResponse> getCategories();

}

