package org.lagonette.app.locator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Moshi;

import org.lagonette.app.api.service.LaGonetteService;

public class Api {

	@Nullable
	private static LaGonetteService.Partner SERVICE_PARTNER;

	@Nullable
	private static LaGonetteService.Category SERVICE_CATEGORY;

	@Nullable
	private static Moshi MOSHI;

	private Api() {
	}

	@NonNull
	public static LaGonetteService.Partner partner() {
		if (SERVICE_PARTNER == null) {
			throw new IllegalStateException("SERVICE_PARTNER was not set!");
		}
		return SERVICE_PARTNER;
	}

	public static void setPartnerService(@NonNull LaGonetteService.Partner service) {
		SERVICE_PARTNER = service;
	}

	@NonNull
	public static LaGonetteService.Category category() {
		if (SERVICE_CATEGORY == null) {
			throw new IllegalStateException("SERVICE_CATEGORY was not set!");
		}
		return SERVICE_CATEGORY;
	}

	public static void setCategoryService(@NonNull LaGonetteService.Category service) {
		SERVICE_CATEGORY = service;
	}

	@NonNull
	public static Moshi moshi() {
		if (MOSHI == null) throw new IllegalStateException("MOSHI was not set!");
		return MOSHI;
	}

	public static void setMoshi(@NonNull Moshi moshi) {
		MOSHI = moshi;
	}

}