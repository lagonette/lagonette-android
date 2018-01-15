package org.lagonette.app.tools.functions;

public interface Function<R, P> {

    R apply(P param);
}
