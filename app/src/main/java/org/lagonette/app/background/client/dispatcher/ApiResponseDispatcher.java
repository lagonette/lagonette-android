package org.lagonette.app.background.client.dispatcher;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.lagonette.app.api.response.ApiResponse;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;

public class ApiResponseDispatcher<R extends ApiResponse> {

    @NonNull
    public Consumer<R> onSuccessful = NullFunctions::doNothing;

    @NonNull
    public Consumer<String> onBodyError = NullFunctions::doNothing;

    public void onResponseSuccessful(
            int code,
            @NonNull R body) {
        if (TextUtils.isEmpty(body.errors)) {
            onSuccessful.accept(body);
        }
        else {
            onBodyError.accept(body.errors);
        }
    }

}
