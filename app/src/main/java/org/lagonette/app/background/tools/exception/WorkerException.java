package org.lagonette.app.background.tools.exception;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.error.Error;

public class WorkerException
		extends Exception {

	@NonNull
	public final Error error;

	public WorkerException(@NonNull Error error) {
		this.error = error;
	}

	public WorkerException(@NonNull Error error, String message) {
		super(message);
		this.error = error;
	}

	public WorkerException(@NonNull Error error, String message, Throwable cause) {
		super(message, cause);
		this.error = error;
	}

	public WorkerException(@NonNull Error error, Throwable cause) {
		super(cause);
		this.error = error;
	}

}