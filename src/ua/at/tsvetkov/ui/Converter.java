package ua.at.tsvetkov.ui;

public class Converter {

   /**
    * One inch in mm
    */
   public static final float INCH_IN_MM   = 25.4f;
   /**
    * One mm in inches
    */
   public static final float MM_IN_INCH   = 1f / INCH_IN_MM;
   /**
    * One inch in cm
    */
   public static final float INCH_IN_CM   = 2.54f;
   /**
    * One cm in inch
    */
   public static final float CM_IN_INCH   = 1f / INCH_IN_CM;
   /**
    * One inch in twip
    */
   public static final float INCH_IN_TWIP = 1440f;
   /**
    * One twip in inch
    */
   public static final float TWIP_IN_INCH = 1f / INCH_IN_TWIP;
   /**
    * One mm in twip
    */
   public static final float MM_IN_TWIP   = 56.6925562674f;
   /**
    * One twip in mm
    */
   public static final float TWIP_IN_MM   = 1f / MM_IN_TWIP;
   /**
    * One twip in pt
    */
   public static final float TWIP_IN_PT   = 20f;
   /**
    * One pt in twip
    */
   public static final float PT_IN_TWIP   = 1 / TWIP_IN_PT;
   /**
    * One inch in pt
    */
   public static final float INCH_IN_PT   = 72f;
   /**
    * One pt in inch
    */
   public static final float PT_IN_INCH   = 1f / INCH_IN_PT;
   /**
    * One mm in pt
    */
   public static final float MM_IN_PT     = INCH_IN_MM / INCH_IN_PT;
   /**
    * One pt in mm
    */
   public static final float PT_IN_MM     = 1f / MM_IN_PT;

   public static float inchToMm(float inch) {
      return inch * INCH_IN_MM;
   }

   public static float mmToInch(float mm) {
      return mm * MM_IN_INCH;
   }

   public static float inchToCm(float inch) {
      return inch * INCH_IN_CM;
   }

   public static float cmToInch(float cm) {
      return cm * CM_IN_INCH;
   }

   public static float inchToPt(float inch) {
      return inch * INCH_IN_PT;
   }

   public static float ptToInch(float pt) {
      return pt * PT_IN_INCH;
   }

   public static float mmToPt(float mm) {
      return mm * MM_IN_PT;
   }

   public static float ptToMm(float pt) {
      return pt * PT_IN_MM;
   }

   public static float twipToPt(float twip) {
      return twip * TWIP_IN_PT;
   }

   public static float ptToTwip(float pt) {
      return pt * PT_IN_TWIP;
   }

   public static float twipToInch(float twip) {
      return twip * TWIP_IN_INCH;
   }

   public static float inchToTwip(float inch) {
      return inch * INCH_IN_TWIP;
   }

   public static float twipToMm(float twip) {
      return twip * TWIP_IN_MM;
   }

   public static float mmToTwip(float mm) {
      return mm * MM_IN_TWIP;
   }

   public static float dpToPixels(float dp, float dpi) {
      return dp * (dpi / 160);
   }

}
