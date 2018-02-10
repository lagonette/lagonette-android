package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface Predicate<P> {

    boolean test(@NonNull P param);
}
