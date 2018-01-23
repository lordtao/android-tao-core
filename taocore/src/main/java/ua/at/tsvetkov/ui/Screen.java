/**
 * ****************************************************************************
 * Copyright (c) 2015 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p/>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p/>
 * Project:
 * TAO Core
 * <p/>
 * License agreement:
 * <p/>
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Return screen dimensions
 *
 * @author Alexandr Tsvetkov 2014
 */
final public class Screen {

   private Screen() {
   }

   /**
    * The exact physical pixels per inch of the screen in the X dimension.
    *
    * @param context your Context
    * @return pixels per inch
    */
   public static float getXdpi(Context context) {
      DisplayMetrics dm = new DisplayMetrics();
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      display.getMetrics(dm);
      return dm.xdpi;
   }

   /**
    * The exact physical pixels per inch of the screen in the Y dimension.
    *
    * @param context your Context
    * @return pixels per inch
    */
   public static float getYdpi(Context context) {
      DisplayMetrics dm = new DisplayMetrics();
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      display.getMetrics(dm);
      return dm.ydpi;
   }

   /**
    * Return width of screen in pixels
    *
    * @param context your Context
    * @return width in pixels
    */
   public static int getWidth(Context context) {
      return getSizeInPixels(context).x;
   }

   /**
    * Return height of screen in pixels
    *
    * @param context your Context
    * @return height in pixels
    */
   public static int getHeight(Context context) {
      return getSizeInPixels(context).y;
   }

   /**
    * Gets the size of the display, in pixels.
    *
    * @param context your Context
    * @return Point with x and y dimensions as width and height in pixels
    */
   @SuppressLint("NewApi")
   @SuppressWarnings("deprecation")
   public static Point getSizeInPixels(Context context) {
      Point size = new Point();
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
         display.getSize(size);
      } else {
         size.x = display.getWidth();
         size.y = display.getHeight();
      }
      return size;
   }

   /**
    * Gets the physical size of the display, in inch
    *
    * @param context your Context
    * @return PointF with x and y dimensions as width and height in inch
    */
   public static PointF getSizeInInch(Context context) {
      DisplayMetrics dm = new DisplayMetrics();
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      display.getMetrics(dm);
      PointF size = new PointF();
      size.x = dm.widthPixels / dm.xdpi;
      size.y = dm.heightPixels / dm.ydpi;
      return size;
   }

   /**
    * Gets the physical size of the display, in mm
    *
    * @param context your Context
    * @return PointF with x and y dimensions as width and height in mm
    */
   public static PointF getSizeInMm(Context context) {
      PointF size = getSizeInInch(context);
      size.x = Converter.inchToMm(size.x);
      size.y = Converter.inchToMm(size.y);
      return size;
   }

   /**
    * Get biggest screen dimension - width or height in pixels.
    *
    * @param context your Context
    * @return biggest dimension in pixels
    */
   public static int getBiggestSizeInPixels(Context context) {
      Point size = getSizeInPixels(context);
      if (size.x > size.y) {
         return size.x;
      } else {
         return size.y;
      }
   }

   /**
    * Get biggest screen dimension - width or height in mm.
    *
    * @param context your Context
    * @return biggest dimension in mm
    */
   public static float getBiggestSizeInMm(Context context) {
      PointF size = getSizeInMm(context);
      if (size.x > size.y) {
         return size.x;
      } else {
         return size.y;
      }
   }

   /**
    * Get biggest screen dimension - width or height in inch.
    *
    * @param context your Context
    * @return biggest dimension in inch
    */
   public static float getBiggestSizeInInch(Context context) {
      PointF size = getSizeInInch(context);
      if (size.x > size.y) {
         return size.x;
      } else {
         return size.y;
      }
   }

   /**
    * Return pixels count from physical X dimension in inch
    *
    * @param context your Context
    * @param inch    size in inch
    * @return pixels count
    */
   public static float inchToPixelsX(Context context, float inch) {
      return inch * getXdpi(context);
   }

   /**
    * Return pixels count from physical Y dimension in inch
    *
    * @param context your Context
    * @param inch    size in inch
    * @return pixels count
    */
   public static float inchToPixelsY(Context context, float inch) {

      return inch * getYdpi(context);
   }

   /**
    * Converts a size in mm in the pixel count in the X dimension.
    *
    * @param context your Context
    * @param mm      size in mm
    * @return pixels count
    */
   public static float mmToPixelsX(Context context, float mm) {
      return inchToPixelsX(context, Converter.mmToInch(mm));
   }

   /**
    * Converts a size in mm in the pixel count in the Y dimension.
    *
    * @param context your Context
    * @param mm      size in mm
    * @return pixels count
    */
   public static float mmToPixelsY(Context context, float mm) {
      return inchToPixelsY(context, Converter.mmToInch(mm));
   }

   /**
    * Return size in inch from X axis pixels count
    *
    * @param context your Context
    * @param pixelsX X axis pixels count
    * @return size in inch
    */
   public static float pixelXtoInch(Context context, float pixelsX) {
      return pixelsX / getXdpi(context);
   }

   /**
    * Return size in inch from Y axis pixels count
    *
    * @param context your Context
    * @param pixelsY Y axis pixels count
    * @return size in inch
    */
   public static float pixelYtoInch(Context context, float pixelsY) {
      return pixelsY / getYdpi(context);
   }

   /**
    * Return size in mm from X axis pixels count
    *
    * @param context your Context
    * @param pixelsX X axis pixels count
    * @return size in mm
    */
   public static float pixelXtoMm(Context context, float pixelsX) {
      return Converter.inchToMm(pixelXtoInch(context, pixelsX));
   }

   /**
    * Return size in mm from Y axis pixels count
    *
    * @param context your Context
    * @param pixelsY Y axis pixels count
    * @return size in mm
    */
   public static float pixelYtoMm(Context context, float pixelsY) {
      return Converter.inchToMm(pixelYtoInch(context, pixelsY));
   }

   /**
    * Convert size in dp to pixels on axis X
    *
    * @param context your Context
    * @param dpX     size in dp
    * @return pixels
    */
   public static float xdpToPixels(Context context, float dpX) {
      return Converter.dpToPixels(dpX, getXdpi(context));
   }

   /**
    * Convert size in dp to pixels on axis X
    *
    * @param context your Context
    * @param dpY     size in dp
    * @return pixels
    */
   public static float ydpToPixels(Context context, float dpY) {
      return Converter.dpToPixels(dpY, getYdpi(context));
   }


}
