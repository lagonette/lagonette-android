package org.lagonette.app.api.adapter;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.lagonette.app.api.response.Location;
import org.lagonette.app.api.response.Partner;

import java.io.IOException;

// TODO Check and log incorrect data
public class LocationTypeAdapter extends TypeAdapter<Location> {

    private static final String TAG = "LocationTypeAdapter";

//    @NonNull
//    private final WrongDataPoster mWrongDataPoster;

    @NonNull
    private Gson mGson;

//    @NonNull
//    private final StringBuilder mStringBuilder;

    public LocationTypeAdapter(@NonNull Gson gson/*, @NonNull WrongDataPoster wrongDataPoster*/) {
        mGson = gson;
//        mWrongDataPoster = wrongDataPoster;
//        mStringBuilder = new StringBuilder();
    }

    @Override
    public void write(JsonWriter out, Location location) throws IOException {
        mGson.toJson(location, Partner.class, out);
    }

    @Override
    public Location read(JsonReader in) throws IOException {
        Location location = mGson.fromJson(in, Location.class);
//        check(location);
        return location;
    }

//    private void check(
//            @NonNull Location location,
//                       @NonNull long partnerId,
//                       @NonNull String partnerName) {
//        boolean send = false;
//
//        if (location.displayLocation && (location.latitude == 0 || location.longitude == 0)) {
//            send = true;
//            mStringBuilder.append("wrong coordinates, ");
//        }
//
//        if (location.displayLocation
//                && (TextUtils.isEmpty(location.address)
//                || TextUtils.isEmpty(location.city)
//                || TextUtils.isEmpty(location.zipCode))) {
//            send = true;
//            mStringBuilder.append("empty address, ");
//        }
//
//        if (send) {
//            mStringBuilder.insert(0, ": ");
//            prependPartner(mStringBuilder, partnerId, partnerName);
//            mStringBuilder
//                    .insert(0, "Wrong data detected for ")
//                    .delete(mStringBuilder.length() - 2, mStringBuilder.length());
//            FirebaseCrash.logcat(Log.WARN, TAG, mStringBuilder.toString());
//            mStringBuilder.setLength(0);
//            mWrongDataPoster.postWrongData();
//        }
//    }

//    private void prependPartner(
//            @NonNull StringBuilder stringBuilder,
//            @NonNull long partnerId,
//            @NonNull String partnerName) {
//        stringBuilder
//                .insert(0, "]")
//                .insert(0, partnerName)
//                .insert(0, ", name: ")
//                .insert(0, partnerId)
//                .insert(0, "[id:");
//    }
}
