package org.lagonette.app.api.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.lagonette.app.api.adapter.contract.WrongDataPoster;
import org.lagonette.app.api.response.Location;
import org.lagonette.app.api.response.Partner;

import java.io.IOException;

public class PartnerTypeAdapter extends TypeAdapter<Partner> {

    private static final String TAG = "PartnerTypeAdapter";

    @NonNull
    private Gson mGson;

    @NonNull
    private WrongDataPoster mWrongDataPoster;

    @NonNull
    private final StringBuilder mStringBuilder;

    public PartnerTypeAdapter(@NonNull Gson gson, @NonNull WrongDataPoster wrongDataPoster) {
        mGson = gson;
        mWrongDataPoster = wrongDataPoster;
        mStringBuilder = new StringBuilder();
    }

    @Override
    public void write(JsonWriter out, Partner partner) throws IOException {
        mGson.toJson(partner, Partner.class, out);
    }

    @Override
    public Partner read(JsonReader in) throws IOException {
        Partner partner = mGson.fromJson(in, Partner.class);

        for (Location location : partner.locations) {
            location.partnerId = partner.id;
        }

        check(partner);

        return partner;
    }

    private void prependPartner(@NonNull StringBuilder stringBuilder, @NonNull Partner partner) {
        stringBuilder
                .insert(0, "]")
                .insert(0, partner.name)
                .insert(0, ", name: ")
                .insert(0, partner.id)
                .insert(0, "[id:");
    }

    private void check(@NonNull Partner partner) {
        boolean send = false;

        if (TextUtils.isEmpty(partner.description)) {
            send = true;
            mStringBuilder.append("empty description, ");
        } else {
            String trimmedDescription = partner.description.trim();
            if (trimmedDescription.length() != partner.description.length()) {
                send = true;
                partner.description = trimmedDescription;
                mStringBuilder.append("description has extra space, ");
            }
        }

        //TODO extract check method
        if (TextUtils.isEmpty(partner.shortDescription)) {
            send = true;
            mStringBuilder.append("empty short description, ");
        }

        if (send) {
            mStringBuilder.insert(0, ": ");
            prependPartner(mStringBuilder, partner);
            mStringBuilder
                    .insert(0, "Wrong data detected for ")
                    .delete(mStringBuilder.length() - 2, mStringBuilder.length());
            FirebaseCrash.logcat(Log.WARN, TAG, mStringBuilder.toString());
            mStringBuilder.setLength(0);
            mWrongDataPoster.postWrongData();
        }
    }

}
