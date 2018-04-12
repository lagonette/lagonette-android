package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import org.lagonette.app.room.entity.LocationMetadata;

import java.util.List;

public class PartnersResponse
		extends ApiResponse {

	private static final String TAG = "PartnersResponse";

	@Json(name = "partners")
	private List<Partner> mPartners;

	public void prepareInsert(
			@NonNull List<org.lagonette.app.room.entity.Partner> partners,
			@NonNull List<org.lagonette.app.room.entity.Location> locations,
			@NonNull List<LocationMetadata> locationMetadataList,
			@NonNull List<org.lagonette.app.room.entity.PartnerSideCategory> partnerSideCategories) {
		for (Partner partner : mPartners) {
			if (partner != null) {
				partner.prepareInsert(partners, locations, locationMetadataList, partnerSideCategories);
			}
		}
	}

}