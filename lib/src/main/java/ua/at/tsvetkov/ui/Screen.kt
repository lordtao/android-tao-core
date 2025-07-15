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
import android.app.Activity
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
import ua.at.tsvetkov.util.logger.Log
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
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
        var screenBoundsInch: ScreenBounds = ScreenBounds(0f, 0f)
            private set
        var screenBoundsMm: ScreenBounds = ScreenBounds(0f, 0f)
            private set
        var screenBoundsPixels: ScreenBounds = ScreenBounds(0f, 0f)
            private set

        var isInit = ::dpi.isInitialized && ::deviceType.isInitialized
            private set

        fun init(activity: Activity) {

            prepareDpi(activity)

            prepareBoundsPixels(activity)
            prepareBoundsInch()
            prepareBoundsMm()

            prepareDiagonalInch()
            prepareDiagonalMm()
            prepareDiagonalPixels()

            prepareDeviceType(activity)

            printScreenInfo()
        }

        private fun printScreenInfo() {
            val df = DecimalFormat("##.##")
            df.roundingMode = RoundingMode.UP
            Log.i(
                "Device type:     $deviceType\n" +
                        "Screen diagonal: ${df.format(diagonalInch)}\"\n" +
                        "Screen width:    ${df.format(screenBoundsInch.width)}\"\n" +
                        "Screen height:   ${df.format(screenBoundsInch.height)}\"\n" +
                        "Screen width:    ${df.format(screenBoundsPixels.width)} px\n" +
                        "Screen height:   ${df.format(screenBoundsPixels.height)} px\n" +
                        "Screen xdpi:     ${df.format(dpi.xdpi)} dpi\n" +
                        "Screen ydpi:     ${df.format(dpi.ydpi)} dpi"
            )
        }

        private fun prepareDpi(activity: Activity) {
            val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            dpi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 (Android 11)
                val dm = DisplayMetrics()
                @Suppress("DEPRECATION")
                activity.display.getMetrics(dm)

                val xdpi = if (dm.xdpi > 0) dm.xdpi else activity.resources.configuration.densityDpi.toFloat()
                val ydpi = if (dm.ydpi > 0) dm.ydpi else activity.resources.configuration.densityDpi.toFloat()

                ScreenDpi(xdpi = xdpi, ydpi = ydpi)
            } else { // API Level 28 (Android 9) and 29 (Android 10)
                @Suppress("DEPRECATION")
                val dm = DisplayMetrics()

                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                display.getRealMetrics(dm) // getRealMetrics() для получения полных метрик, включая DPI
                ScreenDpi(xdpi = dm.xdpi, ydpi = dm.ydpi)
            }
        }

        /**
         * Gets the size of the display, in pixels.
         *
         * @param context your Context
         * @return Point with x and y dimensions as width and height in pixels
         */
        @SuppressLint("NewApi")
        @JvmStatic
        private fun prepareBoundsPixels(context: Context) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            screenBoundsPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 (Android 11)
                val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
                val bounds: Rect = windowMetrics.bounds
                ScreenBounds(width = bounds.width().toFloat(), height = bounds.height().toFloat())
            } else { // API Level 28 (Android 9) and 29 (Android 10)
                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay

                @Suppress("DEPRECATION")
                val sizePoint = Point()
                @Suppress("DEPRECATION")
                display.getRealSize(sizePoint)
                ScreenBounds(width = sizePoint.x.toFloat(), height = sizePoint.y.toFloat())
            }
        }

        /**
         * Gets the physical size of the display, in inch
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in inch
         */
        @JvmStatic
        private fun prepareBoundsInch() {
            val width = screenBoundsPixels.width / dpi.xdpi
            val height = screenBoundsPixels.height / dpi.ydpi
            screenBoundsInch = ScreenBounds(width, height)
        }

        /**
         * Gets the physical size of the display, in mm
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in mm
         */
        @JvmStatic
        private fun prepareBoundsMm() {
            val width = Converter.inchToMm(screenBoundsInch.width)
            val height = Converter.inchToMm(screenBoundsInch.height)
            screenBoundsMm = ScreenBounds(width, height)
        }

        /**
         * The exact physical pixels per inch of the screen in the X dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getXdpi(): Float {
            checkInitialization()
            return dpi.xdpi
        }

        /**
         * The exact physical pixels per inch of the screen in the Y dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getYdpi(): Float {
            checkInitialization()
            return dpi.ydpi
        }

        /**
         * Return width of screen in pixels
         *
         * @param context your Context
         * @return width in pixels
         */
        @JvmStatic
        fun getWidth(): Int {
            return screenBoundsPixels.width.toInt()
        }

        /**
         * Return height of screen in pixels
         *
         * @param context your Context
         * @return height in pixels
         */
        @JvmStatic
        fun getHeight(): Int {
            return screenBoundsPixels.height.toInt()
        }

        /**
         * Get biggest screen dimension - width or height in pixels.
         *
         * @param context your Context
         * @return biggest dimension in pixels
         */
        @JvmStatic
        fun getBiggestSizeInPixels(): Int {
            return if (screenBoundsPixels.width > screenBoundsPixels.height) {
                screenBoundsPixels.width
            } else {
                screenBoundsPixels.height
            }.toInt()
        }

        /**
         * Get biggest screen dimension - width or height in mm.
         *
         * @param context your Context
         * @return biggest dimension in mm
         */
        @JvmStatic
        fun getBiggestSizeInMm(): Float {
            return if (screenBoundsMm.width > screenBoundsMm.height) {
                screenBoundsMm.width
            } else {
                screenBoundsMm.height
            }
        }

        /**
         * Get biggest screen dimension - width or height in inch.
         *
         * @param context your Context
         * @return biggest dimension in inch
         */
        @JvmStatic
        fun getBiggestSizeInInch(): Float {
            return if (screenBoundsInch.width > screenBoundsInch.height) {
                screenBoundsInch.width
            } else {
                screenBoundsInch.height
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
        fun inchToPixelsX(inch: Float): Float {
            return inch * getXdpi()
        }

        /**
         * Return pixels count from physical Y dimension in inch
         *
         * @param context your Context
         * @param inch    size in inch
         * @return pixels count
         */
        @JvmStatic
        fun inchToPixelsY(inch: Float): Float {
            return inch * getYdpi()
        }

        /**
         * Converts a size in mm in the pixel count in the X dimension.
         *
         * @param context your Context
         * @param mm      size in mm
         * @return pixels count
         */
        @JvmStatic
        fun mmToPixelsX(mm: Float): Float {
            return inchToPixelsX(Converter.mmToInch(mm))
        }

        /**
         * Converts a size in mm in the pixel count in the Y dimension.
         *
         * @param context your Context
         * @param mm      size in mm
         * @return pixels count
         */
        @JvmStatic
        fun mmToPixelsY(mm: Float): Float {
            return inchToPixelsY(Converter.mmToInch(mm))
        }

        /**
         * Return size in inch from X axis pixels count
         *
         * @param context your Context
         * @param pixelsX X axis pixels count
         * @return size in inch
         */
        @JvmStatic
        fun pixelXtoInch(pixelsX: Float): Float {
            return pixelsX / getXdpi()
        }

        /**
         * Return size in inch from Y axis pixels count
         *
         * @param context your Context
         * @param pixelsY Y axis pixels count
         * @return size in inch
         */
        @JvmStatic
        fun pixelYtoInch(pixelsY: Float): Float {
            return pixelsY / getYdpi()
        }

        /**
         * Return size in mm from X axis pixels count
         *
         * @param context your Context
         * @param pixelsX X axis pixels count
         * @return size in mm
         */
        @JvmStatic
        fun pixelXtoMm(pixelsX: Float): Float {
            return Converter.inchToMm(pixelXtoInch(pixelsX))
        }

        /**
         * Return size in mm from Y axis pixels count
         *
         * @param context your Context
         * @param pixelsY Y axis pixels count
         * @return size in mm
         */
        @JvmStatic
        fun pixelYtoMm(pixelsY: Float): Float {
            return Converter.inchToMm(pixelYtoInch(pixelsY))
        }

        /**
         * Convert size in dp to pixels on axis X
         *
         * @param context your Context
         * @param dpX     size in dp
         * @return pixels
         */
        @JvmStatic
        fun xdpToPixels(dpX: Float): Float {
            return Converter.dpToPixels(dpX, getXdpi())
        }

        /**
         * Convert size in dp to pixels on axis X
         *
         * @param context your Context
         * @param dpY     size in dp
         * @return pixels
         */
        @JvmStatic
        fun ydpToPixels(dpY: Float): Float {
            return Converter.dpToPixels(dpY, getYdpi())
        }

        /**
         * Return device diagonal in inch
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun prepareDiagonalInch() {
            diagonalInch = sqrt(
                (screenBoundsInch.width.pow(2) + screenBoundsInch.height.pow(2))
                    .toDouble()
            )
        }

        /**
         * Return device diagonal in mm
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun prepareDiagonalMm() {
            diagonalMm = sqrt(
                (screenBoundsMm.width.pow(2) + screenBoundsMm.height.pow(2))
                    .toDouble()
            )
        }

        /**
         * Return device diagonal in pixels
         *
         * @param context
         * @return diagonal
         */
        @JvmStatic
        fun prepareDiagonalPixels() {
            diagonalPixels = sqrt(
                (screenBoundsPixels.width.pow(2) + screenBoundsPixels.height.pow(2))
                    .toDouble()
            )
        }

        private fun prepareDeviceType(context: Context) {
            val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_TYPE_MASK
            if (uiMode == Configuration.UI_MODE_TYPE_WATCH ||
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
            ) {
                deviceType = DeviceType.WATCH
                return
            }

            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val hasTelephony = telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE

            deviceType = when {
                diagonalInch > PHONE_MAX_DIAGONAL_FOR_DETERMINATION -> DeviceType.TABLET
                hasTelephony -> DeviceType.PHONE
                else -> DeviceType.UNKNOWN
            }
        }

        private fun checkInitialization() {
            if (!isInit) {
                throw IllegalStateException("You need call Screen.init(activity) first!")
            }
        }

    }

}
