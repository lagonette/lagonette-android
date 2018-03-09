package org.lagonette.app.background.client.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.tools.functions.Supplier;

public class SignatureStore {

    @NonNull
    public Consumer<String> saveSignature = NullFunctions::doNothing;

    @NonNull
    public Supplier<String> retrieveLocalSignature;

    @Nullable
    private String mSignature;

    public void store(@NonNull String signature) {
        mSignature = signature;
    }

    public void save() {
        if (!TextUtils.isEmpty(mSignature)) {
            saveSignature.accept(mSignature);
        }
    }

    public boolean hasSignatureChanged(@NonNull String remoteSignature) {
        String localSignature = retrieveLocalSignature.get();
        return TextUtils.isEmpty(localSignature) || !localSignature.equals(remoteSignature);
    }

}
