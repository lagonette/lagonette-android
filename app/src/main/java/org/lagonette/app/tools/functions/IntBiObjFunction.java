package org.lagonette.app.tools.functions;

public interface IntBiObjFunction<R, P2, P3> {

    R apply(int param1, P2 param2, P3 param3);
}
