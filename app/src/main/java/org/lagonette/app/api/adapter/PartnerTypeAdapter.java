package org.lagonette.app.api.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.lagonette.app.api.response.Partner;

import java.io.IOException;

public class PartnerTypeAdapter extends TypeAdapter<Partner> {

    public interface WrongDataSender {

        void acknowledgeWrongData();

    }
    private static final String TAG = "PartnerTypeAdapter";

    @NonNull
    private Gson mGson;

    @NonNull
    private WrongDataSender mWrongDataSender;

    @NonNull
    private final StringBuilder mStringBuilder;

    public PartnerTypeAdapter(@NonNull Gson gson, @NonNull WrongDataSender wrongDataSender) {
        mGson = gson;
        mWrongDataSender = wrongDataSender;
        mStringBuilder = new StringBuilder();
    }

    @Override
    public void write(JsonWriter out, Partner partner) throws IOException {
        mGson.toJson(partner, Partner.class, out);
    }

    @Override
    public Partner read(JsonReader in) throws IOException {
        Partner partner = mGson.fromJson(in, Partner.class);

        if (TextUtils.isEmpty(partner.address)
                && partner.latitude == 0
                && partner.longitude == 0) {
            prependPartner(mStringBuilder, partner);
            mStringBuilder.insert(0, "Skip partner: ");
            FirebaseCrash.logcat(Log.WARN, TAG, mStringBuilder.toString());
            mStringBuilder.setLength(0);
            partner = null;
        }
        else {
            boolean send = false;

            if (partner.latitude == 0 || partner.longitude == 0) {
                send = true;
                mStringBuilder.append("wrong coordinates, ");
            }

            if (TextUtils.isEmpty(partner.description)) {
                send = true;
                mStringBuilder.append("empty description, ");
            }
            else {
                String trimmedDescription = partner.description.trim();
                if (trimmedDescription.length() != partner.description.length()) {
                    send = true;
                    partner.description = trimmedDescription;
                    mStringBuilder.append("description has extra space, ");
                }
            }

            // TODO extract check method
            if (TextUtils.isEmpty(partner.shortDescription)) {
                send = true;
                mStringBuilder.append("empty short description, ");
            }

            if (TextUtils.isEmpty(partner.address)
                    || TextUtils.isEmpty(partner.city)
                    || TextUtils.isEmpty(partner.zipCode)) {
                send = true;
                mStringBuilder.append("empty address, ");
            }

            if (send) {
                mStringBuilder.insert(0, ": ");
                prependPartner(mStringBuilder, partner);
                mStringBuilder
                        .insert(0, "Wrong data detected for ")
                        .delete(mStringBuilder.length() - 2, mStringBuilder.length());
                FirebaseCrash.logcat(Log.WARN, TAG, mStringBuilder.toString());
                mStringBuilder.setLength(0);
                mWrongDataSender.acknowledgeWrongData();
            }
        }

        return partner;
    }

    public void prependPartner(@NonNull StringBuilder stringBuilder, @NonNull Partner partner) {
        stringBuilder
                .insert(0, "]")
                .insert(0, partner.name)
                .insert(0, ", name: ")
                .insert(0, partner.id)
                .insert(0, "[id:");
    }
}
