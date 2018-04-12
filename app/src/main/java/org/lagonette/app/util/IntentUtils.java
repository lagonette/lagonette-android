package org.lagonette.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

public final class IntentUtils {

	public static final String SCHEME_TEL = "tel";

	public static final String SCHEME_MAILTO = "mailto";

	public static final String SCHEME_HTTP = "http";

	public static final String SCHEME_HTTPS = "https";

	private IntentUtils() {
	}

	public static boolean startDirection(
			@NonNull Context context,
			@NonNull String label,
			double latitude,
			double longitude) {
		Intent intent = new Intent(
				android.content.Intent.ACTION_VIEW,
				Uri.parse("geo:0,0?q=" + latitude + "," + longitude + "(" + label + ")")
		);
		PackageManager packageManager = context.getPackageManager();
		if (intent.resolveActivity(packageManager) != null) {
			context.startActivity(intent);
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean makeCall(@NonNull Context context, @NonNull String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(SCHEME_TEL, phoneNumber, null));
		PackageManager packageManager = context.getPackageManager();
		if (intent.resolveActivity(packageManager) != null) {
			context.startActivity(intent);
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean goToWebsite(@NonNull Context context, @NonNull String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(formatUrl(url)));
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean writeEmail(@NonNull Context context, @NonNull String email) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(SCHEME_MAILTO, email, null));
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
			return true;
		}
		else {
			return false;
		}
	}

	private static String formatUrl(@NonNull String url) {
		if (!url.startsWith(SCHEME_HTTP + "://") && !url.startsWith(SCHEME_HTTPS + "://")) {
			url = SCHEME_HTTP + "://" + url;
		}
		return url;
	}
}
