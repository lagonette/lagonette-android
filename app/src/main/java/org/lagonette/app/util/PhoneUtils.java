package org.lagonette.app.util;

import com.crashlytics.android.Crashlytics;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtils {

	public final static String COUNTRY_CODE = "FR";

	public static String format(String number) {
		try {
			PhoneNumberUtil util = PhoneNumberUtil.getInstance();
			Phonenumber.PhoneNumber phoneNumber = util.parse(number, COUNTRY_CODE);
			return util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
		}
		catch (NumberParseException e) {
			Crashlytics.logException(e);
			return number;
		}
	}
}
