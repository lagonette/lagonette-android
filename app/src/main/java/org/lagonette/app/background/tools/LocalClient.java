package org.lagonette.app.background.tools;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.tools.exception.WorkerException;
import org.zxcv.functions.main.Supplier;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

public class LocalClient<R> {

	private static final String TAG = "LocalClient";

	@NonNull
	private final Supplier<JsonAdapter<R>> adapterFactory;

	@NonNull
	private final Supplier<InputStream> getJsonSource;

	@NonNull
	private final Error mError;

	public LocalClient(
			@NonNull Supplier<InputStream> getJsonSource,
			@NonNull Supplier<JsonAdapter<R>> adapterFactory,
			@NonNull Error error) {
		this.adapterFactory = adapterFactory;
		this.getJsonSource = getJsonSource;
		mError = error;
	}

	@NonNull
	public R call() throws WorkerException {

		JsonAdapter<R> jsonAdapter = adapterFactory.get();

		BufferedSource bufferedSource = Okio.buffer(
				Okio.source(
						getJsonSource.get()
				)
		);

		try {
			R body = jsonAdapter.fromJson(bufferedSource);
			if (body != null) {
				return body;
			}
			else {
				throw new WorkerException(mError, "Body is NULL.");
			}
		}
		catch (IOException | JsonDataException e) {
			throw new WorkerException(mError, e);
		}
		finally {
			try {
				bufferedSource.close();
			}
			catch (IOException e) {
				Log.e(TAG, "call: buffered source did not close.", e);
			}
		}
	}
}
