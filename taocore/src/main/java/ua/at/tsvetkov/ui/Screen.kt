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
import android.graphics.Point
import android.graphics.PointF
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import ua.at.tsvetkov.application.Diagonal

/**
 * Return screen dimensions
 *
 * @author Alexandr Tsvetkov 2014
 */
class Screen {

    companion object {

        /**
         * The exact physical pixels per inch of the screen in the X dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getXdpi(context: Context): Float {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getMetrics(dm)
            return dm.xdpi
        }

        /**
         * The exact physical pixels per inch of the screen in the Y dimension.
         *
         * @param context your Context
         * @return pixels per inch
         */
        @JvmStatic
        fun getYdpi(context: Context): Float {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getMetrics(dm)
            return dm.ydpi
        }

        /**
         * Return width of screen in pixels
         *
         * @param context your Context
         * @return width in pixels
         */
        @JvmStatic
        fun getWidth(context: Context): Int {
            return getSizeInPixels(context).x
        }

        /**
         * Return height of screen in pixels
         *
         * @param context your Context
         * @return height in pixels
         */
        @JvmStatic
        fun getHeight(context: Context): Int {
            return getSizeInPixels(context).y
        }

        /**
         * Gets the size of the display, in pixels.
         *
         * @param context your Context
         * @return Point with x and y dimensions as width and height in pixels
         */
        @SuppressLint("NewApi")
        @JvmStatic
        fun getSizeInPixels(context: Context): Point {
            val size = Point()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                display.getSize(size)
            } else {
                size.x = display.width
                size.y = display.height
            }
            return size
        }

        /**
         * Gets the physical size of the display, in inch
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in inch
         */
        @JvmStatic
        fun getSizeInInch(context: Context): PointF {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getMetrics(dm)
            val size = PointF()
            size.x = dm.widthPixels / dm.xdpi
            size.y = dm.heightPixels / dm.ydpi
            return size
        }

        /**
         * Gets the physical size of the display, in mm
         *
         * @param context your Context
         * @return PointF with x and y dimensions as width and height in mm
         */
        @JvmStatic
        fun getSizeInMm(context: Context): PointF {
            val size = getSizeInInch(context)
            size.x = Converter.inchToMm(size.x)
            size.y = Converter.inchToMm(size.y)
            return size
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
            return if (size.x > size.y) {
                size.x
            } else {
                size.y
            }
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
            return if (size.x > size.y) {
                size.x
            } else {
                size.y
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
            return if (size.x > size.y) {
                size.x
            } else {
                size.y
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
            return Math.sqrt((pf.x * pf.x + pf.y * pf.y).toDouble())
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
            return Math.sqrt((pf.x * pf.x + pf.y * pf.y).toDouble())
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
            return Math.sqrt((pf.x * pf.x + pf.y * pf.y).toDouble())
        }

        /**
         * Return device Diagonal enum constant
         *
         * @param context
         * @return Diagonal
         */
        @JvmStatic
        fun getDiagonal(context: Context): Diagonal? {
            val size = getDiagonalInInch(context)
            for (diagonal in Diagonal.values()) {
                if (diagonal.isThis(size)) {
                    return diagonal
                }
            }
            return null
        }

        /**
         * Checks whether the device is a watch or a small phone
         *
         * @param context
         * @return is the device is a telephone
         */
        @JvmStatic
        fun isWatchOrSmallPhone(context: Context): Boolean {
            val diagonal = getDiagonalInInch(context)
            return diagonal <= Diagonal.SMALL_PHONE_OR_WATCH.max
        }

        /**
         * Checks whether the device is a telephone
         *
         * @param context
         * @return is the device is a telephone
         */
        @JvmStatic
        fun isPhone(context: Context): Boolean {
            val diagonal = getDiagonalInInch(context)
            return diagonal >= Diagonal.PHONE_2.min && diagonal <= Diagonal.PHONE_6.max
        }

        /**
         * Checks whether the device is a tablet
         *
         * @param context
         * @return is the device is a tablet
         */
        @JvmStatic
        fun isTablet(context: Context): Boolean {
            val diagonal = getDiagonalInInch(context)
            return diagonal >= Diagonal.TABLET_7.min
        }

    }

}
