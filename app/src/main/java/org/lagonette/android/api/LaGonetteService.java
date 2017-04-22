package org.lagonette.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface LaGonetteService {

    @GET("partnersinterface.php?format=app_partners")
    Call<PartnersResponse> getPartners();

    @GET("partnersinterface.php?format=app_categories")
    Call<CategoriesResponse> getCategories();

    public static final Gson gson = new GsonBuilder().registerTypeAdapter(Long.class, new TypeAdapter<Long>() {
        @Override
        public Long read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try {
                Long value = Long.valueOf(stringValue);
                return value;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public void write(JsonWriter writer, Long value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }).create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://82.225.211.150:18001/dolibarr/htdocs/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

}

