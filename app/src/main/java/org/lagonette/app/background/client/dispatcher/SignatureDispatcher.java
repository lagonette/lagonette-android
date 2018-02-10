package org.lagonette.app.background.client.dispatcher;

import android.support.annotation.NonNull;

import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.tools.functions.Predicate;

public class SignatureDispatcher {

    @NonNull
    public Runnable onSignatureNotChanged = NullFunctions::doNothing;

    @NonNull
    public Consumer<String> onSignatureChanged = NullFunctions::doNothing;

    @NonNull
    public Runnable onBodyError = NullFunctions::doNothing;

    @NonNull
    public Predicate<String> hasSignatureChanged = NullFunctions::falsePredicate;

    public void onResponseSuccessful(
            int code,
            @NonNull Md5SumResponse body) {
        String remoteSignature = body.md5Sum;
        if (remoteSignature == null) {
            onBodyError.run();
        } else if (hasSignatureChanged.test(remoteSignature)) {
            onSignatureChanged.accept(remoteSignature);
        }
        else {
            onSignatureNotChanged.run();
        }
    }

}
