package org.lagonette.app.util;

import android.os.StrictMode;

import org.lagonette.app.BuildConfig;

public class StrictModeUtils {

	public static void enableStrictMode() {
		if (BuildConfig.DEBUG) {
			StrictMode.setThreadPolicy(
					new StrictMode.ThreadPolicy.Builder()
							.detectAll()
							.build()
			);
			StrictMode.setVmPolicy(
					new StrictMode.VmPolicy.Builder()
							.detectAll()
							.build()
			);
		}
	}
}
