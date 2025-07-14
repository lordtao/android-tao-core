/**
 * ****************************************************************************
 * Copyright (c) 2015 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 *
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 *
 *
 * Project:
 * TAO Core
 *
 *
 * License agreement:
 *
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowMetrics
import ua.at.tsvetkov.application.DeviceType
import kotlin.math.sqrt

/**
 * Return screen dimensions
 *
 * @author Alexandr Tsvetkov 2014-2025
 */
class Screen {

    data class ScreenDpi(val xdpi: Float, val ydpi: Float)
    data class ScreenBounds(val width: Float, val height: Float)

    companion object {

        const val PHONE_MAX_DIAGONAL_FOR_DETERMINATION = 7.5f

        lateinit var dpi: ScreenDpi
            private set
        lateinit var deviceType: DeviceType
            private set
        var diagonalInch: Double = 0.0
            private set
        var diagonalMm: Double = 0.0
            private set
        var diagonalPixels: Double = 0.0
            private set

        fun init(context: Context) {
            dpi = getScreenDpi(context)
            deviceType = getDeviceType(context)
            diagonalInch = getDiagonalInInch(context)
            diagonalMm = getDiagonalInMm(context)
            diagonalPixels = getDiagonalInPixels(context)
        }

        fun getScreenDpi(context: Context): ScreenDpi {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 (Android 11) 
                val dm = DisplayMetrics()
                @Suppress("DEPRECATION")
                context.display.getMetrics(dm)

                val xdpi = if (dm.xdpi > 0) dm.xdpi else context.resources.configuration.densityDpi.toFloat()
                val ydpi = if (dm.ydpi > 0) dm.ydpi else context.resources.configuration.densityDpi.toFloat()

                return ScreenDpi(xdpi = xdpi, ydpi = ydpi)
            } else { // API Level 28 (Android 9) and 29 (Android 10)
                @Suppress("DEPRECATION")
                val dm = DisplayMetrics()

                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                display.getRealMetrics(dm) // getRealMetrics() для получения полных метрик, включая DPI
                return ScreenDpi(xdpi = dm.xdpi, ydpi = dm.ydpi)
            }
        }

        /**
         * The exact physical pixels per inch of the screen in the X dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getXdpi(context: Context): Float {
            return dpi.xdpi
        }

        /**
         * The exact physical pixels per inch of the screen in the Y dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getYdpi(context: Context): Float {
            return dpi.ydpi
        }

        /**
         * Return width of screen in pixels
         *
         * @param context your Context
         * @return width in pixels
         */
        @JvmStatic
        fun getWidth(context: Context): Int {
            return getSizeInPixels(context).width.toInt()
        }

        /**
         * Return height of screen in pixels
         *
         * @param context your Context
         * @return height in pixels
         */
        @JvmStatic
        fun getHeight(context: Context): Int {
            return getSizeInPixels(context).height.toInt()
        }

        /**
         * Gets the size of the display, in pixels.
         *
         * @param context your Context
         * @return Point with x and y dimensions as width and height in pixels
         */
        @SuppressLint("NewApi")
        @JvmStatic
        fun getSizeInPixels(context: Context): ScreenBounds {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 (Android 11)
                val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
                val bounds: Rect = windowMetrics.bounds
                return ScreenBounds(width = bounds.width().toFloat(), height = bounds.height().toFloat())
            } else { // API Level 28 (Android 9) and 29 (Android 10)
                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                val sizePoint = Point()
                @Suppress("DEPRECATION")
                display.getRealSize(sizePoint)
                return ScreenBounds(width = sizePoint.x.toFloat(), height = sizePoint.y.toFloat())
            }
        }

        /**
         * Gets the physical size of the display, in inch
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in inch
         */
        @JvmStatic
        fun getSizeInInch(context: Context): ScreenBounds {
            var width: Float
            var height: Float
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 (Android 11) и выше
                val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
                val bounds: Rect = windowMetrics.bounds

                val dm = DisplayMetrics()
                @Suppress("DEPRECATION")
                context.display.getMetrics(dm)

                val xdpi = if (dm.xdpi > 0) dm.xdpi else context.resources.configuration.densityDpi.toFloat()
                val ydpi = if (dm.ydpi > 0) dm.ydpi else context.resources.configuration.densityDpi.toFloat()

                width = bounds.width() / xdpi
                height = bounds.height() / ydpi

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // API Level 29 (Android 10)
                val dm = DisplayMetrics()
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getRealMetrics(dm)

                width = dm.widthPixels / dm.xdpi
                height = dm.heightPixels / dm.ydpi

            } else { // API Level 28 (Android 9)
                @Suppress("DEPRECATION")
                val dm = DisplayMetrics()

                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                display.getRealMetrics(dm)

                width = dm.widthPixels / dm.xdpi
                height = dm.heightPixels / dm.ydpi
            }
            return ScreenBounds(width, height)
        }

        /**
         * Gets the physical size of the display, in mm
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in mm
         */
        @JvmStatic
        fun getSizeInMm(context: Context): ScreenBounds {
            val bounds = getSizeInInch(context)
            val width = Converter.inchToMm(bounds.width)
            val height = Converter.inchToMm(bounds.height)
            return ScreenBounds(width, height)
        }

        /**
         * Get biggest screen dimension - width or height in pixels.
         *
         * @param context your Context
         * @return biggest dimension in pixels
         */
        @JvmStatic
        fun getBiggestSizeInPixels(context: Context): Int {
            val size = getSizeInPixels(context)
            return if (size.width > size.height) {
                size.width
            } else {
                size.height
            }.toInt()
        }

        /**
         * Get biggest screen dimension - width or height in mm.
         *
         * @param context your Context
         * @return biggest dimension in mm
         */
        @JvmStatic
        fun getBiggestSizeInMm(context: Context): Float {
            val size = getSizeInMm(context)
            return if (size.width > size.height) {
                size.width
            } else {
                size.height
            }
        }

        /**
         * Get biggest screen dimension - width or height in inch.
         *
         * @param context your Context
         * @return biggest dimension in inch
         */
        @JvmStatic
        fun getBiggestSizeInInch(context: Context): Float {
            val size = getSizeInInch(context)
            return if (size.width > size.height) {
                size.width
            } else {
                size.height
            }
        }

        /**
         * Return pixels count from physical X dimension in inch
         *
         * @param context your Context
         * @param inch    size in inch
         * @return pixels count
         */
        @JvmStatic
        fun inchToPixelsX(context: Context, inch: Float): Float {
            return inch * getXdpi(context)
        }

        /**
         * Return pixels count from physical Y dimension in inch
         *
         * @param context your Context
         * @param inch    size in inch
         * @return pixels count
         */
        @JvmStatic
        fun inchToPixelsY(context: Context, inch: Float): Float {
            return inch * getYdpi(context)
        }

        /**
         * Converts a size in mm in the pixel count in the X dimension.
         *
         * @param context your Context
         * @param mm      size in mm
         * @return pixels count
         */
        @JvmStatic
        fun mmToPixelsX(context: Context, mm: Float): Float {
            return inchToPixelsX(context, Converter.mmToInch(mm))
        }

        /**
         * Converts a size in mm in the pixel count in the Y dimension.
         *
         * @param context your Context
         * @param mm      size in mm
         * @return pixels count
         */
        @JvmStatic
        fun mmToPixelsY(context: Context, mm: Float): Float {
            return inchToPixelsY(context, Converter.mmToInch(mm))
        }

        /**
         * Return size in inch from X axis pixels count
         *
         * @param context your Context
         * @param pixelsX X axis pixels count
         * @return size in inch
         */
        @JvmStatic
        fun pixelXtoInch(context: Context, pixelsX: Float): Float {
            return pixelsX / getXdpi(context)
        }

        /**
         * Return size in inch from Y axis pixels count
         *
         * @param context your Context
         * @param pixelsY Y axis pixels count
         * @return size in inch
         */
        @JvmStatic
        fun pixelYtoInch(context: Context, pixelsY: Float): Float {
            return pixelsY / getYdpi(context)
        }

        /**
         * Return size in mm from X axis pixels count
         *
         * @param context your Context
         * @param pixelsX X axis pixels count
         * @return size in mm
         */
        @JvmStatic
        fun pixelXtoMm(context: Context, pixelsX: Float): Float {
            return Converter.inchToMm(pixelXtoInch(context, pixelsX))
        }

        /**
         * Return size in mm from Y axis pixels count
         *
         * @param context your Context
         * @param pixelsY Y axis pixels count
         * @return size in mm
         */
        @JvmStatic
        fun pixelYtoMm(context: Context, pixelsY: Float): Float {
            return Converter.inchToMm(pixelYtoInch(context, pixelsY))
        }

        /**
         * Convert size in dp to pixels on axis X
         *
         * @param context your Context
         * @param dpX     size in dp
         * @return pixels
         */
        @JvmStatic
        fun xdpToPixels(context: Context, dpX: Float): Float {
            return Converter.dpToPixels(dpX, getXdpi(context))
        }

        /**
         * Convert size in dp to pixels on axis X
         *
         * @param context your Context
         * @param dpY     size in dp
         * @return pixels
         */
        @JvmStatic
        fun ydpToPixels(context: Context, dpY: Float): Float {
            return Converter.dpToPixels(dpY, getYdpi(context))
        }

        /**
         * Return device diagonal in inch
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun getDiagonalInInch(context: Context): Double {
            val pf = getSizeInInch(context)
            return sqrt((pf.width * pf.width + pf.height * pf.height).toDouble())
        }

        /**
         * Return device diagonal in mm
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun getDiagonalInMm(context: Context): Double {
            val pf = getSizeInMm(context)
            return sqrt((pf.width * pf.width + pf.height * pf.height).toDouble())
        }

        /**
         * Return device diagonal in pixels
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun getDiagonalInPixels(context: Context): Double {
            val pf = getSizeInPixels(context)
            return sqrt((pf.width * pf.width + pf.height * pf.height).toDouble())
        }

        private fun getDeviceType(context: Context): DeviceType {
            val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_TYPE_MASK
            if (uiMode == Configuration.UI_MODE_TYPE_WATCH ||
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
            ) {
                return DeviceType.WATCH
            }

            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val hasTelephony = telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE

            val screenSize = getSizeInInch(context)
            val diagonalInches = sqrt(
                (screenSize.width * screenSize.width) + (screenSize.height * screenSize.height)
            )

            return when {
                diagonalInches > PHONE_MAX_DIAGONAL_FOR_DETERMINATION -> DeviceType.TABLET
                hasTelephony -> DeviceType.PHONE
                else -> DeviceType.UNKNOWN
            }
        }

    }

}
