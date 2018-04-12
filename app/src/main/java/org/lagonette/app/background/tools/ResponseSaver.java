package org.lagonette.app.background.tools;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.room.dao.WriterDao.CategoryEntities;
import org.lagonette.app.room.dao.WriterDao.PartnerEntities;
import org.lagonette.app.tools.functions.main.BiConsumer;
import org.lagonette.app.tools.functions.main.Consumer;

public class ResponseSaver {

	@NonNull
	private final BiConsumer<CategoryEntities, PartnerEntities> mSaveEntities;

	@NonNull
	private final Consumer<String> mSaveCategorySignature;

	@NonNull
	private final Consumer<String> mSavePartnerSignature;

	public ResponseSaver(
			@NonNull BiConsumer<CategoryEntities, PartnerEntities> saveEntities,
			@NonNull Consumer<String> saveCategorySignature,
			@NonNull Consumer<String> savePartnerSignature) {
		mSaveEntities = saveEntities;
		mSaveCategorySignature = saveCategorySignature;
		mSavePartnerSignature = savePartnerSignature;
	}

	public void save(
			@Nullable CategoriesResponse categoriesResponse,
			@Nullable PartnersResponse partnersResponse) {

		mSaveEntities.accept(
				convertEntity(categoriesResponse),
				convertEntity(partnersResponse)
		);

		if (categoriesResponse != null) {
			mSaveCategorySignature.accept(categoriesResponse.md5Sum);
		}

		if (partnersResponse != null) {
			mSavePartnerSignature.accept(partnersResponse.md5Sum);
		}
	}


	public PartnerEntities convertEntity(@Nullable PartnersResponse body) {
		PartnerEntities entities = new PartnerEntities();

		if (body != null) {
			entities.hasValues = true;
			body.prepareInsert(
					entities.partners,
					entities.locations,
					entities.locationMetadata,
					entities.partnerSideCategories
			);
		}

		return entities;
	}

	public CategoryEntities convertEntity(@Nullable CategoriesResponse body) {
		CategoryEntities entities = new CategoryEntities();

		if (body != null) {
			entities.hasValues = true;
			body.prepareInsert(
					entities.categories,
					entities.categoryMetadata
			);
		}

		return entities;
	}
}
