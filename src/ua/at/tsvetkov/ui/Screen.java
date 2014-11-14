/**
 * 
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
 * @author Alexandr Tsvetkov 2014
 */
public class Screen {

   /**
    * The exact physical pixels per inch of the screen in the X dimension.
    * 
    * @param context
    * @return
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
    * @param context
    * @return
    */
   public static float getYdpi(Context context) {
      DisplayMetrics dm = new DisplayMetrics();
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      display.getMetrics(dm);
      return dm.ydpi;
   }

   /**
    * Return width of screen
    * 
    * @param context
    * @return width
    */
   public static float getWidth(Context context) {
      return getSizeInPixels(context).x;
   }

   /**
    * Return height of screen
    * 
    * @param context
    * @return height
    */
   public static float getHeight(Context context) {
      return getSizeInPixels(context).y;
   }

   /**
    * Gets the size of the display, in pixels.
    * 
    * @param context
    * @return
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
    * @param context
    * @return
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
    * @param context
    * @return
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
    * @param context
    * @return
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
    * @param context
    * @return
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
    * @param context
    * @return
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
    * @param context
    * @param inch
    * @return
    */
   public static float inchToPixelsX(Context context, float inch) {
      return inch * getXdpi(context);
   }

   /**
    * Return pixels count from physical Y dimension in inch
    * 
    * @param context
    * @param inch
    * @return
    */
   public static float inchToPixelsY(Context context, float inch) {

      return inch * getYdpi(context);
   }

   /**
    * Converts a size in mm in the pixel count in the X dimension.
    * 
    * @param context
    * @param mm
    * @return
    */
   public static float mmToPixelsX(Context context, float mm) {
      return inchToPixelsX(context, Converter.mmToInch(mm));
   }

   /**
    * Converts a size in mm in the pixel count in the Y dimension.
    * 
    * @param context
    * @param mm
    * @return
    */
   public static float mmToPixelsY(Context context, float mm) {
      return inchToPixelsY(context, Converter.mmToInch(mm));
   }

   /**
    * Return size in inch from X axis pixels count
    * 
    * @param context
    * @param px
    * @return
    */
   public static float pixelXtoInch(Context context, float pixelsX) {
      return pixelsX / getXdpi(context);
   }

   /**
    * Return size in inch from Y axis pixels count
    * 
    * @param context
    * @param px
    * @return
    */
   public static float pixelYtoInch(Context context, float pixelsY) {
      return pixelsY / getYdpi(context);
   }

   /**
    * Return size in mm from X axis pixels count
    * 
    * @param context
    * @param px
    * @return
    */
   public static float pixelXtoMm(Context context, float pixelsX) {
      return Converter.inchToMm(pixelXtoInch(context, pixelsX));
   }

   /**
    * Return size in mm from Y axis pixels count
    * 
    * @param context
    * @param px
    * @return
    */
   public static float pixelYtoMm(Context context, float pixelsY) {
      return Converter.inchToMm(pixelYtoInch(context, pixelsY));
   }

   /**
    * Convert size in dp to pixels on axis X
    * 
    * @param context
    * @param px
    * @return
    */
   public static float xdpToPixels(Context context, float dpX) {
      return Converter.dpToPixels(dpX, getXdpi(context));
   }

   /**
    * Convert size in dp to pixels on axis X
    * 
    * @param context
    * @param px
    * @return
    */
   public static float ydpToPixels(Context context, float dpY) {
      return Converter.dpToPixels(dpY, getYdpi(context));
   }

}
