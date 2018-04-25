package org.lagonette.app.background.tools;

import android.support.annotation.NonNull;

import com.squareup.moshi.JsonDataException;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.tools.exception.WorkerException;
import org.lagonette.app.tools.functions.main.Supplier;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RetrofitClient<R> {

	@NonNull
	private final Supplier<Call<R>> mCallFactory;

	@NonNull
	private final Error mError;

	public RetrofitClient(@NonNull Error error, @NonNull Supplier<Call<R>> callFactory) {
		mCallFactory = callFactory;
		mError = error;
	}

	@NonNull
	public R call() throws WorkerException {
		try {
			Call<R> call = mCallFactory.get();
			Response<R> response = call.execute();

			if (response.isSuccessful()) {
				R body = response.body();
				if (body != null) {
					return body;
				}
				else {
					throw new WorkerException(mError, "Body is NULL");
				}
			}
			else {
				throw new WorkerException(mError, "Response error → " + response.code() + ": " + response.message());
			}
		}
		catch (IOException | JsonDataException e) {
			throw new WorkerException(mError, e);
		}
	}
}
