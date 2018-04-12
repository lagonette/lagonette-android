package org.lagonette.app.room.converter;


import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class DataTypeConverters {

	@TypeConverter
	public static LatLng toLocation(@Nullable String position) {
		if (position == null) {
			return null;
		}

		String[] pieces = position.split(",");

		return new LatLng(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]));
	}
}
