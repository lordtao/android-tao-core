/**
 * ****************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
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
package ua.at.tsvetkov.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PointF;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import ua.at.tsvetkov.ui.Screen;
import ua.at.tsvetkov.util.Log;

/**
 * Return the basic parameters of the application. Initialize and restoration of essential parameters for the app. Data saves and loads in
 * to the Shared Preferences with name equals the app packages name. Automatically create the working directory for the application in the
 * standard place. Checks is the app is new or is new version and whether it was previously installed.
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public final class AppConfig {

   public static final  String  APP_NAME                = "APP_NAME";
   public static final  String  APP_VERSION_NAME        = "APP_VERSION_NAME";
   public static final  String  APP_VERSION_CODE        = "APP_VERSION_CODE";
   public static final  String  APP_WORKING_DIRECTORY   = "APP_WORKING_DIRECTORY";
   public static final  String  IMEI                    = "IMEI";
   public static final  String  NEW_VERSION             = "NEW_VERSION";
   public static final  String  NEW_INSTALL             = "NEW_INSTALL";
   public static final  String  FRESH_INSTALL           = "FRESH_INSTALL";
   public static final  boolean SAVE                    = true;
   public static final  boolean NOT_SAVE                = false;
   private static final String  PREFIX                  = "| ";
   private static final String  LINE                    = "▪=====================▪";
   private static final String  LINE_EMPTY              = "▪                     ▪";
   private static final String  LINE_DOUBLE             = "===========================================================================";
   private static final String  DEFAULT_SETTINGS_STRING = "|                        Default Shared Preferences";
   private static final String  CURRENT_SETTINGS_STRING = "|                               Shared Data";
   private static final String  TRIAL_IS_EXPIRED        = "Sorry, the trial version has expired ";
   private static final String  DIV_LEFT                = "▪ ";
   private static final String  DIV_RIGHT               = " ▪";

   private static String            workingDirectory    = "";
   private static String            appName             = "";
   private static Context           myContext           = null;
   private static SharedPreferences preferences         = null;
   private static Editor            editor              = null;
   private static Diagonal          diagonal            = null;
   private static boolean           isDebuggable        = true;
   private static boolean           isBeingDebugged     = true;
   private static boolean           isNewApplication    = false;
   private static boolean           isNewVersion        = false;
   private static boolean           isFreshInstallation = false;
   private static boolean           isStrictMode        = false;
   private static boolean           isInitialized       = false;

   /**
    * Init configuration. Use in class extended Application class. Create the working dirs in standard dir "/Android/data/" + application
    * package name.
    *
    * @param context Base context
    * @throws NumberFormatException
    */
   public static void init(Context context) {
      init(context, false);
   }

   /**
    * Init configuration. Usually in class extended Application class.
    *
    * @param context          Base context
    * @param putWorkDirInRoot true - put the working dirs in SD root, false - put the working dirs in standart dir "/Android/data/" +
    *                         appData.applicationInfo.packageName.
    * @throws NumberFormatException
    */
   public static void init(Context context, boolean putWorkDirInRoot) {
      if (context == null) {
         Log.e("Can't init AppConfig, your context eq null.");
         return;
      }
      myContext = context;
      isNewVersion = false;
      isNewApplication = false;
      isFreshInstallation = false;
      isInitialized = true;
      PackageInfo appData = null;
      try {
         appData = myContext.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
      } catch (NameNotFoundException e) {
         Log.e("Package not found", e);
      }
      preferences = context.getSharedPreferences(appData.packageName, Context.MODE_PRIVATE);
      editor = preferences.edit();
      appName = context.getString(appData.applicationInfo.labelRes);
      String dirName = appName.replaceAll("[\\W&&\\D&&\\S]", "");
      if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
         if (putWorkDirInRoot) {
            workingDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName + File.separatorChar;
         } else {
            workingDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + appData.applicationInfo.packageName + File.separatorChar;
         }
      } else {
         workingDirectory = context.getFilesDir().getPath() + File.separator + dirName + File.separatorChar;
      }

      File path = new File(workingDirectory);
      if (!path.exists()) {
         isFreshInstallation = true;
         if (path.mkdir()) {
            android.util.Log.i(DIV_LEFT + appName + DIV_RIGHT, "➧ Created the working directory: " + workingDirectory);
         } else {
            android.util.Log.e(DIV_LEFT + appName + DIV_RIGHT, "➧ Creating of the working directory is failed.\nPlease check the permission android.permission.WRITE_EXTERNAL_STORAGE.");
         }
      } else {
         isFreshInstallation = false;
      }
      isNewApplication = (preferences.getInt(APP_VERSION_CODE, -1) < 0);
      if (appData.versionCode > preferences.getInt(APP_VERSION_CODE, 0)) {
         isNewVersion = true;
      }

      PointF pf   = Screen.getSizeInInch(context);
      double diag = Math.sqrt(pf.x * pf.x + pf.y * pf.y);
      if (diag < 6) {
         diagonal = Diagonal.PHONE;
      } else if (diag >= 6 && diag < 8) {
         diagonal = Diagonal.TABLET_7;
      } else if (diag >= 8 && diag < 11) {
         diagonal = Diagonal.TABLET_10;
      } else {
         diagonal = Diagonal.TABLET_BIG;
      }

      editor.putString(APP_NAME, appName);
      editor.putString(APP_VERSION_NAME, String.valueOf(appData.versionName));
      editor.putInt(APP_VERSION_CODE, appData.versionCode);
      editor.putString(APP_WORKING_DIRECTORY, workingDirectory);
      editor.putBoolean(NEW_VERSION, isNewVersion);
      editor.putBoolean(NEW_INSTALL, isNewApplication);
      editor.putBoolean(FRESH_INSTALL, isFreshInstallation);
      save();

      isDebuggable = (0 != (myContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
      isBeingDebugged = android.os.Debug.isDebuggerConnected();
      if (isDebuggable) {
         android.util.Log.i(DIV_LEFT + appName + DIV_RIGHT, "➧ Log enabled.");
      } else {
         android.util.Log.w(DIV_LEFT + appName + DIV_RIGHT, "➧ Log is prohibited because debug mode is disabled.");
         Log.setDisabled(true);
      }

   }

   /**
    * Checking the initializing of this class and print error stack trace otherwise.
    *
    * @return true if initiated
    */
   public static boolean isInitializing() {
      if (isInitialized) {
         return true;
      } else {
         throw new IllegalArgumentException("AppConfig is not initiated. Call ua.at.tsvetkov.application.AppConfig.init(your_context) in your Application code.");
      }
   }

   /**
    * Whether the app is debuggable or not
    *
    * @return is debuggable
    */
   public static boolean isDebuggable() {
      return isDebuggable;
   }

   /**
    * Whether the app is currently being debugged (e.g. over ADB)
    *
    * @return is being debugged
    */
   public static boolean isBeingDebugged() {
      return isBeingDebugged;
   }

   /**
    * Print stored SharedPreferences in to the LogCat
    */
   public static void printInfo() {
      if (!isDebuggable) {
         return;
      }
      android.util.Log.i(LINE, LINE_DOUBLE);

      android.util.Log.i(LINE_EMPTY, PREFIX + "App name:                    " + appName);
      android.util.Log.i(LINE_EMPTY, PREFIX + "First installation:          " + isNewVersion);
      android.util.Log.i(LINE_EMPTY, PREFIX + "Working directory:           " + workingDirectory);
      android.util.Log.i(LINE_EMPTY, PREFIX + "Strict mode:                 " + isStrictMode);
      android.util.Log.i(LINE_EMPTY, PREFIX + "Diagonal:                    " + diagonal);
      android.util.Log.i(LINE_EMPTY, PREFIX + "Application signature SHA-1: " + getApplicationSignatureKeyHash());
      android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
      android.util.Log.i(LINE_EMPTY, CURRENT_SETTINGS_STRING);
      android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
      int max = 0;
      for (Map.Entry<String, ?> setting : preferences.getAll().entrySet()) {
         int length = setting.getKey().length();
         if (max < length) {
            max = length;
         }
      }
      String formatString = PREFIX + "%-" + max + "s = %s";
      for (Map.Entry<String, ?> setting : preferences.getAll().entrySet()) {
         android.util.Log.i(LINE_EMPTY, String.format(formatString, setting.getKey(), setting.getValue()));
      }
      android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
      android.util.Log.i(LINE_EMPTY, DEFAULT_SETTINGS_STRING);
      android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
      SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences();
      for (Map.Entry<String, ?> setting : defaultSharedPreferences.getAll().entrySet()) {
         android.util.Log.i(LINE_EMPTY, String.format(formatString, setting.getKey(), setting.getValue()));
      }
      android.util.Log.i(LINE, LINE_DOUBLE);
   }

   /**
    * Return default shared preferences (main Application settings)
    *
    * @return default SharedPreferences
    */
   public static SharedPreferences getDefaultSharedPreferences() {
      return PreferenceManager.getDefaultSharedPreferences(myContext);
   }

   /**
    * Return base application Context
    *
    * @return Context
    */
   public static Context getContext() {
      if (isInitializing()) {
         return myContext;
      } else {
         return null;
      }
   }

   /**
    * Checks whether the device is a telephone
    *
    * @return is the device is a telephone
    */
   public static boolean isPhone() {
      return diagonal == Diagonal.PHONE;
   }

   /**
    * Checks whether the device is a tablet
    *
    * @return is the device is a tablet
    */
   public static boolean isTablet() {
      return diagonal != Diagonal.PHONE;
   }

   /**
    * Return device diagonal enum constant
    *
    * @return Diagonal
    */
   public static Diagonal getDeviceDiagonal() {
      return diagonal;
   }

   /**
    * Return current working directory
    *
    * @return String - current working directory
    */
   public static String getApplicationWorkingDir() {
      return workingDirectory;
   }

   /**
    * Return the app name without spaces.
    *
    * @return the app name without spaces.
    */
   public static String getAppName() {
      return appName;
   }

   /**
    * Return true if new version of the app is started
    *
    * @return is new version of app is started
    */
   public static boolean isNewVersion() {
      return isNewVersion;
   }

   /**
    * Return true if the app is absolutely new
    *
    * @return is the app is absolutely new
    */
   public static boolean isNewApplication() {
      return isNewApplication;
   }

   /**
    * Return true if the app is totally fresh (any old app dirs is absent in to the working dir)
    *
    * @return is the app is totally fresh
    */
   public static boolean isFreshInstallation() {
      return isFreshInstallation;
   }

   /**
    * Retrieve a boolean value from the preferences.
    *
    * @param key      The name of the preference to modify.
    * @param defValue Value to return if this preference does not exist.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a boolean.
    */
   public static boolean getBoolean(String key, boolean defValue) {
      return preferences.getBoolean(key, defValue);
   }

   /**
    * Retrieve a float value from the preferences.
    *
    * @param key      The name of the preference to modify.
    * @param defValue Value to return if this preference does not exist.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
    */
   public static float getFloat(String key, float defValue) {
      return preferences.getFloat(key, defValue);
   }

   /**
    * Retrieve a float value from the preferences, 0 if not found
    *
    * @param key The name of the preference to modify.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
    */
   public static float getFloat(String key) {
      return preferences.getFloat(key, 0);
   }

   /**
    * Return int value
    *
    * @param key      The name of the preference to modify.
    * @param defValue Value to return if this preference does not exist.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a int.
    */
   public static int getInt(String key, int defValue) {
      return preferences.getInt(key, defValue);
   }

   /**
    * Return int value, 0 if not found
    *
    * @param key The name of the preference to modify.
    * @return value
    */
   public static int getInt(String key) {
      return preferences.getInt(key, 0);
   }

   /**
    * Return long value
    *
    * @param key      The name of the preference to modify.
    * @param defValue Value to return if this preference does not exist.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
    */
   public static long getLong(String key, long defValue) {
      return preferences.getLong(key, defValue);
   }

   /**
    * Return long value, 0 if not found
    *
    * @param key The name of the preference to modify.
    * @return value
    */
   public static long getLong(String key) {
      return preferences.getLong(key, 0);
   }

   /**
    * Return value or "" if not found
    *
    * @param key The name of the preference to modify.
    * @return value
    */
   public static String getString(String key) {
      return preferences.getString(key, "");
   }

   /**
    * Return value
    *
    * @param key      The name of the preference to modify.
    * @param defValue Value to return if this preference does not exist.
    * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
    */
   public static String getString(String key, String defValue) {
      return preferences.getString(key, defValue);
   }

   /**
    * Retrieve a set of String values from the preferences.
    * <p/>
    * <p>Note that you <em>must not</em> modify the set instance returned
    * by this call.  The consistency of the stored data is not guaranteed
    * if you do, nor is your ability to modify the instance at all.
    *
    * @param key       The name of the preference to retrieve.
    * @param defValues Values to return if this preference does not exist.
    * @return Return Returns the preference values if they exist, or defValues.
    * Throws ClassCastException if there is a preference with this name
    * that is not a Set.
    * @throws Exception Not supported before version API 11 (HONEYCOMB)
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public static Set<String> getStringSet(String key, Set<String> defValues) throws Exception {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
         return preferences.getStringSet(key, defValues);
      } else {
         throw new Exception("Not supported before version API 11 (HONEYCOMB)");
      }
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   public static void putBoolean(String key, boolean value) {
      editor.putBoolean(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   public static void putFloat(String key, float value) {
      editor.putFloat(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   public static void putInt(String key, int value) {
      editor.putInt(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   public static void putLong(String key, long value) {
      editor.putLong(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   public static void putString(String key, String value) {
      editor.putString(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key   The name of the preference to modify.
    * @param value The new value for the preference.
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public static void putStringSet(String key, Set<String> value) {
      editor.putStringSet(key, value);
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   public static void putBoolean(String key, boolean value, boolean isNeedToSave) {
      editor.putBoolean(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   public static void putFloat(String key, float value, boolean isNeedToSave) {
      editor.putFloat(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   public static void putInt(String key, int value, boolean isNeedToSave) {
      editor.putInt(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   public static void putLong(String key, long value, boolean isNeedToSave) {
      editor.putLong(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   public static void putString(String key, String value, boolean isNeedToSave) {
      editor.putString(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Set a new for this key
    *
    * @param key          The name of the preference to modify.
    * @param value        The new value for the preference.
    * @param isNeedToSave Go write the SharedPreferences or postpone.
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public static void putStringSet(String key, Set<String> value, boolean isNeedToSave) {
      editor.putStringSet(key, value);
      if (isNeedToSave) {
         save();
      }
   }

   /**
    * Go write the SharedPreferences.
    */
   public static void save() {
      editor.commit();
   }

   /**
    * Clear the SharedPreferences
    */
   public static void clear() {
      editor.clear();
      Log.i(">>> Shared preferenced CLEARED!!! <<<");
   }

   /**
    * Enable StrictMode in the app. Put call in the first called onCreate() method in Application or Activity.
    *
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public static void enableStrictMode() throws IllegalAccessException {
      try {
         int appFlags = 0;
         if (myContext != null) {
            appFlags = myContext.getApplicationInfo().flags;
         } else {
            throw new IllegalAccessException("AppConfig is not initialized");
         }
         if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectActivityLeaks().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
         }
         isStrictMode = true;
         Log.i("StrictMode is enabled.");
      } catch (Throwable throwable) {
         isStrictMode = false;
         Log.i("StrictMode is not supported. Skipping...");
      }
   }

   /**
    * Return is StrictMode is enabled.
    *
    * @return Return is StrictMode is enabled.
    */
   public static boolean isStrictModeEnabled() {
      return isStrictMode;
   }

   /**
    * Return true if application run in debug mode
    *
    * @return Return true if application run in debug mode
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static boolean isDebugMode() throws IllegalAccessException {
      if (myContext != null) {
         return (0 != (myContext.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
      } else {
         throw new IllegalAccessException("AppConfig is not initialized");
      }
   }

   /**
    * Print the KeyHash for this application or emty String if AppConfig is not initialized
    *
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static String getApplicationSignatureKeyHash() {
      if (myContext != null) {
         return Apps.getApplicationSignatureKeyHash(myContext.getApplicationInfo().packageName);
      } else {
         Log.e("AppConfig is not initialized. Can't get KeyHash for this application");
         return "";
      }

   }

   /**
    * Check evaluate date and finish activity if expired.
    *
    * @param activity current activity
    * @param year     evaluate year
    * @param month    evaluate month
    * @param day      evaluate day
    */
   @SuppressWarnings("deprecation")
   public static void finishIfTrialExpired(Activity activity, int year, int month, int day) {
      Date date  = new Date();
      Date today = new Date();
      date.setYear(year - 1900);
      date.setMonth(month - 1);
      date.setDate(day);
      if (date.before(today)) {
         Toast.makeText(activity, TRIAL_IS_EXPIRED + date.toGMTString(), Toast.LENGTH_LONG).show();
         Log.w(TRIAL_IS_EXPIRED + date.toGMTString());
         activity.finish();
      }
   }

   /**
    * Set Android Studio log format style.
    */
   public static void setAndroidStudioLogStyle() {
      Log.setAndroidStudioStyle();
   }

   /**
    * Set Eclipse log format style - default.
    */
   public static void setEclipseLogStyle() {
      Log.setEclipseStyle();
   }


}
