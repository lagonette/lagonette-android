package org.lagonette.app.app.widget.performer.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.util.IntentUtils;

public class IntentPerformer {

	@NonNull
	private final Context mContext;

	@NonNull
	public Consumer<Error> onError = Consumer::doNothing;

	public IntentPerformer(@NonNull Context context) {
		mContext = context;
	}

	public void startDirection(@NonNull String label, double latitude, double longitude) {
		boolean success = IntentUtils.startDirection(
				mContext,
				label,
				latitude,
				longitude
		);
		if (!success) {
			onError.accept(Error.NO_DIRECTION_APP_FOUND);
		}
	}

	public void makeCall(@NonNull String phoneNumber) {
		boolean success = IntentUtils.makeCall(
				mContext,
				phoneNumber
		);
		if (!success) {
			onError.accept(Error.NO_CALL_APP_FOUND);
		}
	}

	public void goToWebsite(@NonNull String url) {
		boolean success = IntentUtils.goToWebsite(
				mContext,
				url
		);
		if (!success) {
			onError.accept(Error.NO_BROWSER_APP_FOUND);
		}
	}

	public void writeEmail(@NonNull String email) {
		boolean success = IntentUtils.writeEmail(
				mContext,
				email
		);
		if (!success) {
			onError.accept(Error.NO_EMAIL_APP_FOUND);
		}
	}
}
