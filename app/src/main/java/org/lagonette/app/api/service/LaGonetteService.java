package org.lagonette.app.api.service;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.PartnersResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LaGonetteService {

    String HOST = BuildConfig.USE_DEV_API ? "http://82.225.211.150:18001/dolibarr/htdocs/" : "https://dolibarr.lagonette.org/";

    String ENDPOINT_CATEGORIES = "partnersinterface.php?format=app_categories";

    String ENDPOINT_PARTNERS = "partnersinterface.php?format=app_partners";

    interface Category {

        @GET(ENDPOINT_CATEGORIES)
        Call<CategoriesResponse> getCategories();

    }

    interface Partner {

        @GET(ENDPOINT_PARTNERS)
        Call<PartnersResponse> getPartners();

    }

}

