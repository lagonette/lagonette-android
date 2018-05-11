package org.lagonette.app.util;

public class LocationUtils {

	public static boolean displayAsExchangeOffice(
			boolean isExchangeOffice,
			boolean isGonetteHeadquarter) {
		return isExchangeOffice && !isGonetteHeadquarter;
	}
}
