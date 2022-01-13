package com.andylai.donutbuttons;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

public final class Utils {
	public static Drawable getCompatDrawable(Resources resources, int resId) {
		return ResourcesCompat.getDrawable(resources, resId, null);
	}

	public static int getCompatColor(Resources resources, int resId) {
		return ResourcesCompat.getColor(resources, resId, null);
	}
}
