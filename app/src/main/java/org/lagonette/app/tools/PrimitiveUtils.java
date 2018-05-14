package org.lagonette.app.tools;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PrimitiveUtils {

	public interface NonNullFactory<T> {

		@NonNull
		T create();
	}

	public static int unbox(@Nullable Integer value, int defaultValue) {
		if (value != null) {
			return value;
		}
		else {
			return defaultValue;
		}
	}

	public static long unbox(@Nullable Long value, long defaultValue) {
		if (value != null) {
			return value;
		}
		else {
			return defaultValue;
		}
	}

	public static float unbox(@Nullable Float value, float defaultValue) {
		if (value != null) {
			return value;
		}
		else {
			return defaultValue;
		}
	}

	public static double unbox(@Nullable Double value, double defaultValue) {
		if (value != null) {
			return value;
		}
		else {
			return defaultValue;
		}
	}

	public static <T> T ensure(@Nullable T value, @NonNull NonNullFactory<T> factory) {
		if (value != null) {
			return value;
		}
		else {
			return factory.create();
		}
	}
}
