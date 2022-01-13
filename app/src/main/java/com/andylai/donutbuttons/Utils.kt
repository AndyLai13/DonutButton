package com.andylai.donutbuttons

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat

object Utils {
    fun getCompatDrawable(resources: Resources, resId: Int): Drawable {
        return ResourcesCompat.getDrawable(resources, resId, null)
            ?: throw Resources.NotFoundException("Resource error")
    }

    fun getCompatColor(resources: Resources, resId: Int): Int {
        return ResourcesCompat.getColor(resources, resId, null)
    }
}