package org.lagonette.app.background.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.moshi.Moshi;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.R;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.tools.LocalClient;
import org.lagonette.app.background.tools.ResponseSaver;
import org.lagonette.app.background.tools.RetrofitClient;
import org.lagonette.app.background.tools.exception.WorkerException;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.tools.CrashReporter;
import org.lagonette.app.util.PreferenceUtils;
import org.zxcv.functions.main.BiConsumer;
import org.zxcv.functions.throwable.Predicate;
import org.zxcv.functions.throwable.Supplier;

public class DataRefreshWorker
		extends BackgroundWorker {

	private static final String TAG = "DataRefreshWorker";

	@NonNull
	private final Supplier<Md5SumResponse, WorkerException> mGetCategorySignature;

	@NonNull
	private final Predicate<String, WorkerException> mCategoryHasChanged;

	@NonNull
	private final Supplier<CategoriesResponse, WorkerException> mGetNewCategories;

	@NonNull
	private final Supplier<CategoriesResponse, WorkerException> mGetLocalCategories;

	@NonNull
	private final Supplier<Md5SumResponse, WorkerException> mGetPartnerSignature;

	@NonNull
	private final Predicate<String, WorkerException> mPartnerHasChanged;

	@NonNull
	private final Supplier<PartnersResponse, WorkerException> mGetLocalPartners;

	@NonNull
	private final Supplier<PartnersResponse, WorkerException> mGetNewPartners;

	@NonNull
	private final BiConsumer<CategoriesResponse, PartnersResponse> mSaveEntities;

	@NonNull
	private final Supplier<Boolean, WorkerException> mIsDatabaseEmpty;

	public DataRefreshWorker(@NonNull Context context) {
		super(context);

		// Construct
		LaGonetteDatabase database = DB.get();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

		Moshi moshi = Api.moshi();
		LaGonetteService.Category categoryService = Api.category();
		LaGonetteService.Partner partnerService = Api.partner();

		mIsDatabaseEmpty = () -> database.categoryDao().getCategorieCount() == 0;

		mGetLocalCategories = new LocalClient<>(
				() -> context.getResources().openRawResource(R.raw.categories),
				() -> moshi.adapter(CategoriesResponse.class),
				Error.CATEGORIES_NOT_UPDATED
		)::call;

		mGetLocalPartners = new LocalClient<>(
				() -> context.getResources().openRawResource(R.raw.partners),
				() -> moshi.adapter(PartnersResponse.class),
				Error.PARTNERS_NOT_UPDATED
		)::call;

		mGetCategorySignature = new RetrofitClient<>(
				Error.CATEGORIES_NOT_UPDATED,
				categoryService::getMd5Sum
		)::call;

		mGetNewCategories = new RetrofitClient<>(
				Error.CATEGORIES_NOT_UPDATED,
				categoryService::getCategories
		)::call;

		mGetPartnerSignature = new RetrofitClient<>(
				Error.PARTNERS_NOT_UPDATED,
				partnerService::getMd5Sum
		)::call;

		mGetNewPartners = new RetrofitClient<>(
				Error.CATEGORIES_NOT_UPDATED,
				partnerService::getPartners
		)::call;

		mCategoryHasChanged = remoteSignature -> {
			String localSignature = preferences.getString(
					PreferenceUtils.KEY_CATEGORY_MD5_SUM,
					PreferenceUtils.DEFAULT_VALUE_CATEGORY_MD5_SUM
			);
			return !remoteSignature.equals(localSignature);
		};

		mPartnerHasChanged = remoteSignature -> {
			String localSignature = preferences.getString(
					PreferenceUtils.KEY_PARTNER_MD5_SUM,
					PreferenceUtils.DEFAULT_VALUE_PARTNER_MD5_SUM
			);
			return !remoteSignature.equals(localSignature);
		};

		mSaveEntities = new ResponseSaver(
				database.writerDao()::insert,
				signature -> preferences.edit()
						.putString(PreferenceUtils.KEY_CATEGORY_MD5_SUM, signature)
						.apply(),
				signature -> preferences.edit()
						.putString(PreferenceUtils.KEY_PARTNER_MD5_SUM, signature)
						.apply()
		)::save;
	}

	@NonNull
	@Override
	protected WorkerState doWork() {
		try {

			fillWithLocalValueIfNeeded();

			fillWithServerValueIfNeeded();

			return WorkerState.success();

		}
		catch (WorkerException e) {
			Log.e(TAG, "Caught: ", e);
			CrashReporter.logException(e);
			return WorkerState.error(e.error);
		}
	}

	private void fillWithServerValueIfNeeded() throws WorkerException {
		if (BuildConfig.FILL_DB_WITH_SERVER_VALUE) {

			CategoriesResponse categories = null;
			String categorySignature = mGetCategorySignature.get().md5Sum;
			if (mCategoryHasChanged.test(categorySignature)) {
				categories = mGetNewCategories.get();
			}

			PartnersResponse partners = null;
			String partnerSignature = mGetPartnerSignature.get().md5Sum;
			if (mPartnerHasChanged.test(partnerSignature)) {
				partners = mGetNewPartners.get();
			}

			mSaveEntities.accept(categories, partners);
		}
	}

	private void fillWithLocalValueIfNeeded() throws WorkerException {
		if (BuildConfig.FILL_DB_WITH_LOCAL_VALUE) {
			if (mIsDatabaseEmpty.get()) {
				CategoriesResponse categories = mGetLocalCategories.get();
				PartnersResponse partners = mGetLocalPartners.get();
				mSaveEntities.accept(categories, partners);
			}
		}
	}

}