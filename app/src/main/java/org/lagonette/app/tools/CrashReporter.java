package org.lagonette.app.tools;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class CrashReporter {

	public static void init(@NonNull Context context) {
		Fabric.with(context, new Crashlytics());
	}

	public static boolean isEnabled() {
		return Fabric.isInitialized();
	}

	public static void logException(Throwable throwable) {
		if(isEnabled()) Crashlytics.logException(throwable);
	}

	public static void log(int priority, String tag, String msg) {
		if(isEnabled()) Crashlytics.log(priority, tag, msg);
	}

	public static void log(String msg) {
		if(isEnabled()) Crashlytics.log(msg);
	}

}
