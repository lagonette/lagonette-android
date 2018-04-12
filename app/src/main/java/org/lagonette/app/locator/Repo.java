package org.lagonette.app.locator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.repo.MainRepo;

public final class Repo {

	@Nullable
	private static MainRepo REPO;

	private Repo() {
	}

	@NonNull
	public static MainRepo get() {
		if (REPO == null) throw new IllegalStateException("Repo was not set!");
		return REPO;
	}

	public static void set(@NonNull MainRepo repo) {
		REPO = repo;
	}

}