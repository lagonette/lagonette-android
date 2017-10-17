package org.lagonette.app.api.adapter;

import android.support.annotation.NonNull;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.lagonette.app.api.adapter.contract.WrongDataPoster;
import org.lagonette.app.api.response.Partner;

import java.io.IOException;
import java.util.List;

public class PartnerListTypeAdapter
        extends TypeAdapter<List<Partner>>
        implements WrongDataPoster {

    @NonNull
    private Gson mGson;

    private boolean mSendWrongDataException;

    public PartnerListTypeAdapter(@NonNull Gson gson) {
        mGson = gson;
    }

    @Override
    public void write(JsonWriter out, List<Partner> partners) throws IOException {
        mGson.toJson(partners, partners.getClass(), out);
    }

    @Override
    public List<Partner> read(JsonReader in) throws IOException {
        List<Partner> partners = mGson.fromJson(in, List.class);
        if(mSendWrongDataException) {
            //TODO Use crashlitics
            FirebaseCrash.report(new IllegalArgumentException("API send incomplete data."));
            mSendWrongDataException = false;
        }
        return partners;
    }

    @Override
    public void postWrongData() {
        mSendWrongDataException = true;
    }
}
