package org.lagonette.app.api.service;

import android.support.annotation.NonNull;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.api.response.PartnersResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LaGonetteService {

    String HOST = BuildConfig.USE_DEV_API ? "http://82.225.211.150:18001/dolibarr/htdocs/" : "https://dolibarr.lagonette.org/";

    String ENDPOINT_CATEGORIES = "partnersinterface.php?format=app_categories";

    String ENDPOINT_CATEGORIES_MD5 = "partnersinterface.php?format=app_categories&hashonly=1";

    String ENDPOINT_PARTNERS = "partnersinterface.php?format=app_partners";

    String ENDPOINT_PARTNERS_MD5 = "partnersinterface.php?format=app_partners&hashonly=1";

    interface Category {

        @NonNull
        @GET(ENDPOINT_CATEGORIES)
        Call<CategoriesResponse> getCategories();

        @NonNull
        @GET(ENDPOINT_CATEGORIES_MD5)
        Call<Md5SumResponse> getMd5Sum();

    }

    interface Partner {

        @NonNull
        @GET(ENDPOINT_PARTNERS)
        Call<PartnersResponse> getPartners();

        @NonNull
        @GET(ENDPOINT_PARTNERS_MD5)
        Call<Md5SumResponse> getMd5Sum();

    }

}

