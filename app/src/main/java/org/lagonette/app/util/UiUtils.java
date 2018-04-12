package org.lagonette.app.util;

import android.content.res.Resources;
import android.os.Build;

public class UiUtils {

	public static int getStatusBarHeight(Resources resources) {
		int result = 0;
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = resources.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}

}
