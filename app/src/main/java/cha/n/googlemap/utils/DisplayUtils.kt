package cha.n.googlemap.utils

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager

class DisplayUtils {
    companion object {
        fun getScreenWidthPx(context: Context): Int {
            val wm =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }

        fun getScreenHeightPx(context: Context): Int {
            val wm =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }

        fun getDeviceDensity(context: Context): Float {
            return context.resources.displayMetrics.density
        }

        fun floatToDip(context: Context, float: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, float, context.resources.displayMetrics
            ).toInt()
        }
    }
}