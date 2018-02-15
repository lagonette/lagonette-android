package org.lagonette.app.background.client;

import android.support.annotation.NonNull;

import org.lagonette.app.background.client.exception.ApiClientException;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.IntBiConsumer;
import org.lagonette.app.tools.functions.IntObjConsumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.tools.functions.Supplier;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitClient<R> {

    @NonNull
    public Supplier<Call<R>> callFactory;

    @NonNull
    public IntObjConsumer<R> onResponseSuccessful = NullFunctions::doNothing;

    @NonNull
    public IntBiConsumer<String, ResponseBody> onResponseError = NullFunctions::doNothing;

    @NonNull
    public Consumer<ApiClientException> onException = NullFunctions::doNothing;

    public void call() {
        try {
            Call<R> call = callFactory.get();
            Response<R> response = call.execute();

            if (response.isSuccessful()) {
                onResponseSuccessful.accept(response.code(), response.body());
            } else {
                onResponseError.accept(response.code(), response.message(), response.errorBody());
            }
        } catch (IOException e) {
            onException.accept(new ApiClientException(e));
        }
    }
}
