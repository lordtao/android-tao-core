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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
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
import android.provider.Settings;
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

    public static final String APP_NAME = "APP_NAME";
    public static final String APP_VERSION_NAME = "APP_VERSION_NAME";
    public static final String APP_VERSION_CODE = "APP_VERSION_CODE";
    public static final String APP_PACKAGE_NAME = "APP_PACKAGE_NAME";
    public static final String ANDROID_ID = "APP_ANDROID_ID";
    public static final String APP_WORKING_DIRECTORY = "APP_WORKING_DIRECTORY";
    public static final String IMEI = "IMEI";
    public static final String NEW_VERSION = "NEW_VERSION";
    public static final String NEW_INSTALL = "NEW_INSTALL";
    public static final String FRESH_INSTALL = "FRESH_INSTALL";
    public static final boolean SAVE = true;
    public static final boolean NOT_SAVE = false;

    private static final String PREFIX = "| ";
    private static final String LINE = "▪=====================▪";
    private static final String LINE_EMPTY = "▪                     ▪";
    private static final String LINE_DOUBLE = "==========================================================================================";
    private static final String DEFAULT_SETTINGS_STRING = "|                              Default Shared Preferences";
    private static final String CURRENT_SETTINGS_STRING = "|                                     Shared Data";
    private static final String TRIAL_IS_EXPIRED = "Sorry, the trial version has expired ";
    private static final String DIV_LEFT = "▪ ";
    private static final String DIV_RIGHT = " ▪";

    private static String mWorkingDirectory = "";
    private static String mAppName = "";
    private static SharedPreferences mPreferences = null;
    private static Editor mEditor = null;
    private static Diagonal mDiagonal = null;
    private static boolean isDebuggable = true;
    private static boolean isBeingDebugged = true;
    private static boolean isNewApplication = false;
    private static boolean isNewVersion = false;
    private static boolean isFreshInstallation = false;
    private static boolean isStrictMode = false;
    private static boolean isInitialized = false;
    private static String mAppSignatureKeyHash = null;
    private static String mAppSignatureFingerprint = null;
    private static String mPackageName = null;
    private static String mAppVersionName = null;
    private static int mAppVersionCode = 0;
    private static String mAndroidId;

    /**
     * Init configuration. Create the working dirs in standard dir "/Android/data/" + application package name.
     *
     * @param application the Application
     * @throws NumberFormatException
     */
    public static void init(Application application) {
        init(application, false);
    }

    /**
     * Init configuration.
     *
     * @param application      the Application
     * @param putWorkDirInRoot true - put the working dirs in SD root, false - put the working dirs in standard dir "/Android/data/" + app package name.
     * @throws NumberFormatException
     */
    @SuppressLint("CommitPrefEdits")
    public static void init(Application application, boolean putWorkDirInRoot) {
        isNewVersion = false;
        isNewApplication = false;
        isFreshInstallation = false;
        isInitialized = true;
        mPackageName = application.getApplicationInfo().packageName;
        PackageInfo appData = null;
        try {
            appData = application.getPackageManager().getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            Log.e("Package not found", e);
        }
        mPreferences = application.getSharedPreferences(appData.packageName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mAppName = application.getString(appData.applicationInfo.labelRes);
        String dirName = mAppName.replaceAll("[\\W&&\\D&&\\S]", "");
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            if (putWorkDirInRoot) {
                mWorkingDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName + File.separatorChar;
            } else {
                mWorkingDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + appData.applicationInfo.packageName + File.separatorChar;
            }
        } else {
            mWorkingDirectory = application.getFilesDir().getPath() + File.separator + dirName + File.separatorChar;
        }

        File path = new File(mWorkingDirectory);
        if (!path.exists()) {
            isFreshInstallation = true;
            if (path.mkdir()) {
                android.util.Log.i(DIV_LEFT + mAppName + DIV_RIGHT, "➧ Created the working directory: " + mWorkingDirectory);
            } else {
                android.util.Log.e(DIV_LEFT + mAppName + DIV_RIGHT, "➧ Creating of the working directory is failed.\nPlease check the permission android.permission.WRITE_EXTERNAL_STORAGE.");
            }
        } else {
            isFreshInstallation = false;
        }
        isNewApplication = (mPreferences.getInt(APP_VERSION_CODE, -1) < 0);
        if (appData.versionCode > mPreferences.getInt(APP_VERSION_CODE, 0)) {
            isNewVersion = true;
        }

        PointF pf = Screen.getSizeInInch(application);
        double diag = Math.sqrt(pf.x * pf.x + pf.y * pf.y);
        if (diag < 6) {
            mDiagonal = Diagonal.PHONE;
        } else if (diag >= 6 && diag < 8) {
            mDiagonal = Diagonal.TABLET_7;
        } else if (diag >= 8 && diag < 11) {
            mDiagonal = Diagonal.TABLET_10;
        } else {
            mDiagonal = Diagonal.TABLET_BIG;
        }

        mAppSignatureKeyHash = Apps.getApplicationSignatureKeyHash(application, mPackageName);
        mAppSignatureFingerprint = Apps.getSignatureFingerprint(application, mPackageName);

        mAppVersionName = appData.versionName;
        mAppVersionCode = appData.versionCode;
        mAndroidId = Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);

        mEditor.putString(ANDROID_ID, mAndroidId);
        mEditor.putString(APP_NAME, mAppName);
        mEditor.putString(APP_VERSION_NAME, mAppVersionName);
        mEditor.putString(APP_PACKAGE_NAME, mPackageName);
        mEditor.putInt(APP_VERSION_CODE, mAppVersionCode);
        mEditor.putString(APP_WORKING_DIRECTORY, mWorkingDirectory);
        mEditor.putBoolean(NEW_VERSION, isNewVersion);
        mEditor.putBoolean(NEW_INSTALL, isNewApplication);
        mEditor.putBoolean(FRESH_INSTALL, isFreshInstallation);
        save();

        isDebuggable = (0 != (application.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        isBeingDebugged = android.os.Debug.isDebuggerConnected();
        if (isDebuggable) {
            android.util.Log.i(DIV_LEFT + mAppName + DIV_RIGHT, "➧ Log enabled.");
        } else {
            android.util.Log.w(DIV_LEFT + mAppName + DIV_RIGHT, "➧ Log is prohibited because debug mode is disabled.");
            Log.setDisabled(true);
        }

    }

    /**
     * Name of the app package.
     *
     * @return the package name
     */
    public static String getPackageName() {
        return mPackageName;
    }

    /**
     * The version name of this package, as specified by the <manifest> tag's versionName attribute.
     *
     * @return the app version name
     */
    public static String getAppVersionName() {
        return mAppVersionName;
    }

    /**
     * The version number of this package, as specified by the <manifest> tag's versionCode attribute.
     *
     * @return the app version code
     */
    public static int getAppVersionCode() {
        return mAppVersionCode;
    }

    /**
     * A 64-bit number (as a hex string) that is randomly generated when the user first sets up the device
     * and should remain constant for the lifetime of the user's device.
     * The value may change if a factory reset is performed on the device.
     *
     * @return the android id
     */
    public static String getAndroidId() {
        return mAndroidId;
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
            throw new IllegalArgumentException("AppConfig is not initiated. Call ua.at.tsvetkov.application.AppConfig.init(application) in your Application code.");
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
     * Print the app data and shared mPreferences in to the LogCat
     *
     * @param context the app context
     */
    public static void printInfo(Context context) {
        if (!isDebuggable) {
            return;
        }
        android.util.Log.i(LINE, LINE_DOUBLE);

        android.util.Log.i(LINE_EMPTY, PREFIX + "Application name:      " + mAppName);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Android  device ID:    " + mAndroidId);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Application package:   " + mPackageName);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Signature Fingerprint: " + mAppSignatureFingerprint);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Signature SHA-1:       " + mAppSignatureKeyHash);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Working directory:     " + mWorkingDirectory);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Diagonal:              " + mDiagonal);
        android.util.Log.i(LINE_EMPTY, PREFIX + "First installation:    " + isNewVersion);
        android.util.Log.i(LINE_EMPTY, PREFIX + "Strict mode:           " + isStrictMode);
        android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
        android.util.Log.i(LINE_EMPTY, CURRENT_SETTINGS_STRING);
        android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
        int max = 0;
        for (Map.Entry<String, ?> setting : mPreferences.getAll().entrySet()) {
            int length = setting.getKey().length();
            if (max < length) {
                max = length;
            }
        }
        String formatString = PREFIX + "%-" + max + "s = %s";
        for (Map.Entry<String, ?> setting : mPreferences.getAll().entrySet()) {
            android.util.Log.i(LINE_EMPTY, String.format(formatString, setting.getKey(), setting.getValue()));
        }
        android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
        android.util.Log.i(LINE_EMPTY, DEFAULT_SETTINGS_STRING);
        android.util.Log.i(LINE_EMPTY, LINE_DOUBLE);
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(context);
        for (Map.Entry<String, ?> setting : defaultSharedPreferences.getAll().entrySet()) {
            android.util.Log.i(LINE_EMPTY, String.format(formatString, setting.getKey(), setting.getValue()));
        }
        android.util.Log.i(LINE, LINE_DOUBLE);
    }

    /**
     * Return default shared mPreferences (main Application settings)
     *
     * @param myContext default context
     * @return default SharedPreferences
     */
    public static SharedPreferences getDefaultSharedPreferences(Context myContext) {
        return PreferenceManager.getDefaultSharedPreferences(myContext);
    }

    /**
     * Checks whether the device is a telephone
     *
     * @return is the device is a telephone
     */
    public static boolean isPhone() {
        return mDiagonal == Diagonal.PHONE;
    }

    /**
     * Checks whether the device is a tablet
     *
     * @return is the device is a tablet
     */
    public static boolean isTablet() {
        return mDiagonal != Diagonal.PHONE;
    }

    /**
     * Return device mDiagonal enum constant
     *
     * @return Diagonal
     */
    public static Diagonal getDeviceDiagonal() {
        return mDiagonal;
    }

    /**
     * Return current working directory
     *
     * @return String - current working directory
     */
    public static String getApplicationWorkingDir() {
        return mWorkingDirectory;
    }

    /**
     * Return the app name without spaces.
     *
     * @return the app name without spaces.
     */
    public static String getAppName() {
        return mAppName;
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
     * Retrieve a boolean value from the mPreferences.
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    /**
     * Retrieve a float value from the mPreferences.
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
     */
    public static float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    /**
     * Retrieve a float value from the mPreferences, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
     */
    public static float getFloat(String key) {
        return mPreferences.getFloat(key, 0);
    }

    /**
     * Return int value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public static int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    /**
     * Return int value, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    public static int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    /**
     * Return long value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
     */
    public static long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    /**
     * Return long value, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    public static long getLong(String key) {
        return mPreferences.getLong(key, 0);
    }

    /**
     * Return value or "" if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    public static String getString(String key) {
        return mPreferences.getString(key, "");
    }

    /**
     * Return value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public static String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    /**
     * Retrieve a set of String values from the mPreferences.
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
            return mPreferences.getStringSet(key, defValues);
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
        mEditor.putBoolean(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putInt(String key, int value) {
        mEditor.putInt(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putLong(String key, long value) {
        mEditor.putLong(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public static void putString(String key, String value) {
        mEditor.putString(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(String key, Set<String> value) {
        mEditor.putStringSet(key, value);
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    public static void putBoolean(String key, boolean value, boolean isNeedToSave) {
        mEditor.putBoolean(key, value);
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
        mEditor.putFloat(key, value);
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
        mEditor.putInt(key, value);
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
        mEditor.putLong(key, value);
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
        mEditor.putString(key, value);
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
        mEditor.putStringSet(key, value);
        if (isNeedToSave) {
            save();
        }
    }

    /**
     * Go write the SharedPreferences.
     */
    public static void save() {
        mEditor.commit();
    }

    /**
     * Clear the SharedPreferences
     */
    public static void clear() {
        mEditor.clear();
        Log.i(">>> Shared mPreferences was CLEARED! <<<");
    }

    /**
     * Enable StrictMode in the app. Put call in the first called onCreate() method in Application or Activity.
     *
     * @param aContext the app Context
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode(Context aContext) {
        try {
            int appFlags = aContext.getApplicationInfo().flags;
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
     */
    public static boolean isDebugMode() {
        return isDebuggable;
    }

    /**
     * Return the app signature KeyHash for this application
     *
     * @return the app signature KeyHash
     */
    public static String getApplicationSignatureKeyHash() {
        return mAppSignatureKeyHash;
    }

    /**
     * Return the app signature fingerprint for this application
     *
     * @return the app signature fingerprint
     */
    public static String getSignatureFingerprint() {
        return mAppSignatureFingerprint;
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
        Date date = new Date();
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
