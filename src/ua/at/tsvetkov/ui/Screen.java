/**
 * 
 */
package ua.at.tsvetkov.ui;

import ua.at.tsvetkov.util.Converter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * @author Alexandr Tsvetkov 2014
 */
public class Screen {

   /**
    * Gets the size of the display, in pixels.
    * 
    * @param activity
    * @return
    */
   @SuppressLint("NewApi")
   @SuppressWarnings("deprecation")
   public static Point getSizeInPixels(Activity activity) {
      Point size = new Point();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
         activity.getWindowManager().getDefaultDisplay().getSize(size);
      } else {
         Display display = activity.getWindowManager().getDefaultDisplay();
         size.x = display.getWidth();
         size.y = display.getHeight();
      }
      return size;
   }

   /**
    * Gets the physical size of the display, in inch
    * 
    * @param activity
    * @return
    */
   public static PointF getSizeInInch(Activity activity) {
      DisplayMetrics dm = new DisplayMetrics();
      activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
      PointF size = new PointF();
      size.x = dm.widthPixels / dm.xdpi;
      size.y = dm.heightPixels / dm.ydpi;
      return size;
   }

   /**
    * Gets the physical size of the display, in mm
    * 
    * @param activity
    * @return
    */
   public static PointF getSizeInMm(Activity activity) {
      PointF size = getSizeInInch(activity);
      size.x = Converter.inchToMm(size.x);
      size.y = Converter.inchToMm(size.y);
      return size;
   }

   /**
    * Return pixels count from physical X dimension in inch
    * 
    * @param activity
    * @param inch
    * @return
    */
   public static float inchToPixelsX(Activity activity, float inch) {
      DisplayMetrics dm = new DisplayMetrics();
      activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
      return inch * dm.xdpi;
   }

   /**
    * Return pixels count from physical Y dimension in inch
    * 
    * @param activity
    * @param inch
    * @return
    */
   public static float inchToPixelsY(Activity activity, float inch) {
      DisplayMetrics dm = new DisplayMetrics();
      activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
      return inch * dm.ydpi;
   }

   /**
    * Converts a size in mm in the pixel count in the X dimension.
    * 
    * @param activity
    * @param mm
    * @return
    */
   public static float mmToPixelsX(Activity activity, float mm) {
      return inchToPixelsX(activity, Converter.mmToInch(mm));
   }

   /**
    * Converts a size in mm in the pixel count in the Y dimension.
    * 
    * @param activity
    * @param mm
    * @return
    */
   public static float mmToPixelsY(Activity activity, float mm) {
      return inchToPixelsY(activity, Converter.mmToInch(mm));
   }

}
