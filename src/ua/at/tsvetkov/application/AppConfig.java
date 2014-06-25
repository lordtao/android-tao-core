/*******************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 *     TAO Util
 *
 * License agreement:
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 *    caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 *    permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 *    this license agreement will remain unchanged.
 ******************************************************************************/
package ua.at.tsvetkov.application;

import java.io.File;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ua.at.tsvetkov.util.Log;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Toast;

/**
 * Class for initialize the storage and restoration of essential parameters and simple data for app. Data saves and loads in to the Shared
 * Preferences with name equals the app packages name. Automatically create the working directory for the application in the standard place
 * and help to create subdirs. Checks is the app is new or is new version and whether it was previously installed. You can print Shared
 * Preferences in to the LogCat. Offers a variety of static methods to manage an application.
 * 
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class AppConfig {

	public static final String			APP_NAME						= "APP_NAME";
	public static final String			APP_VERSION_NAME			= "APP_VERSION_NAME";
	public static final String			APP_VERSION_CODE			= "APP_VERSION_CODE";
	public static final String			APP_WORKING_DIRECTORY	= "APP_WORKING_DIRECTORY";
	public static final String			IMEI							= "IMEI";

	public static final String			NEW_VERSION					= "NEW_VERSION";
	public static final String			NEW_INSTALL					= "NEW_INSTALL";
	public static final String			FRESH_INSTALL				= "FRESH_INSTALL";

	private static final String		PREFIX						= "| ";
	private static final String		LINE							= "============================";
	private static final String		LINE_DOUBLE					= "================================================================================";
	private static final String		DEFAULT_SETTINGS_STRING	= "|                          Default Shared Preferences";
	private static final String		CURRENT_SETTINGS_STRING	= "|                                  Shared Data";

	private static final String		TRIAL_IS_EXPIRED			= "Sorry, the trial version has expired ";

	public static final boolean		SAVE							= true;
	public static final boolean		NOT_SAVE						= false;

	private static SharedPreferences	preferences;
	private static Editor				editor;

	private static Context				myContext;
	private static String				workingDirectory			= "";
	private static String				appName						= "";
	private static String				tmpWorkingDir				= "";
	private static boolean				isNewApplication			= false;
	private static boolean				isNewVersion				= false;
	private static boolean				isFreshInstallation		= false;
	private static boolean				isStrictMode				= false;
	private static boolean				isInitialized				= false;

	/**
	 * Init configuration. Use in class extended Application class. Create the working dirs in standard dir "/Android/data/" +
	 * application package name.
	 * 
	 * @param context
	 * @throws NumberFormatException
	 */
	public static void init(Context context) {
		init(context, false);
	}

	/**
	 * Init configuration. Usually in class extended Application class.
	 * 
	 * @param context
	 * @param putWorkDirInRoot true - put the working dirs in SD root, false - put the working dirs in standart dir "/Android/data/" +
	 *           appData.applicationInfo.packageName.
	 * @throws NumberFormatException
	 */
	public static void init(Context context, boolean putWorkDirInRoot) {
		myContext = context;
		isNewVersion = false;
		isNewApplication = false;
		isFreshInstallation = false;
		isInitialized = true;
		PackageInfo appData = null;
		try {
			appData = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
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

		tmpWorkingDir = workingDirectory;
		File path = new File(workingDirectory);
		if (!path.exists()) {
			isFreshInstallation = true;
			if (path.mkdir()) {
				Log.i("Created the working directory: " + workingDirectory);
			} else {
				Log.e("Creating of the working directory is failed.\nPlease check the permission android.permission.WRITE_EXTERNAL_STORAGE.");
			}
		} else {
			isFreshInstallation = false;
		}
		isNewApplication = (preferences.getInt(APP_VERSION_CODE, -1) < 0);
		if (appData.versionCode > preferences.getInt(APP_VERSION_CODE, 0)) {
			isNewVersion = true;
		}
		editor.putString(APP_NAME, appName);
		editor.putString(APP_VERSION_NAME, String.valueOf(appData.versionName));
		editor.putInt(APP_VERSION_CODE, Integer.valueOf(appData.versionCode));
		editor.putString(APP_WORKING_DIRECTORY, workingDirectory);
		editor.putBoolean(NEW_VERSION, isNewVersion);
		editor.putBoolean(NEW_INSTALL, isNewApplication);
		editor.putBoolean(FRESH_INSTALL, isFreshInstallation);
		save();
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
	 * Print stored SharedPreferences in to the LogCat
	 */
	public static void printInfo() {
		android.util.Log.i(LINE, LINE_DOUBLE);
		android.util.Log.i("", PREFIX + "App name:                    " + appName);
		android.util.Log.i("", PREFIX + "First installation:          " + isNewVersion);
		android.util.Log.i("", PREFIX + "Working directory:           " + workingDirectory);
		android.util.Log.i("", PREFIX + "Strict mode:                 " + isStrictMode);
		android.util.Log.i("", PREFIX + "Application signature SHA-1: " + getApplicationSignatureKeyHash());
		android.util.Log.i("", LINE_DOUBLE);
		android.util.Log.i("", CURRENT_SETTINGS_STRING);
		android.util.Log.i("", LINE_DOUBLE);
		int max = 0;
		for (Map.Entry<String, ?> setting : preferences.getAll().entrySet()) {
			int length = setting.getKey().length();
			if (max < length)
				max = length;
		}
		String fomatString = PREFIX + "%-" + max + "s = %s";
		for (Map.Entry<String, ?> setting : preferences.getAll().entrySet()) {
			android.util.Log.i("", String.format(fomatString, setting.getKey(), setting.getValue()));
		}
		android.util.Log.i("", LINE_DOUBLE);
		android.util.Log.i("", DEFAULT_SETTINGS_STRING);
		android.util.Log.i("", LINE_DOUBLE);
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		for (Map.Entry<String, ?> setting : defaultSharedPreferences.getAll().entrySet()) {
			android.util.Log.i("", String.format(fomatString, setting.getKey(), setting.getValue()));
		}
		android.util.Log.i(LINE, LINE_DOUBLE);
	}

	/**
	 * Default working dir with File.separatorChar at the end of string
	 * 
	 * @return
	 */
	public static String getDir() {
		return workingDirectory;
	}

	/**
	 * Switch to the custom working directory
	 * 
	 * @param newDir
	 */
	public static void setNewDir(String newDir) {
		workingDirectory = newDir;
		File path = new File(workingDirectory);
		if (!path.exists())
			if (path.mkdir()) {
				Log.i("Created new working directory: " + workingDirectory);
			} else {
				Log.e("Creating of the working directory is failed.\nPlease check the permission android.permission.WRITE_EXTERNAL_STORAGE.");
			}
	}

	/**
	 * Restore standard working directory
	 */
	public static void restoreStandardDir() {
		workingDirectory = tmpWorkingDir;
		Log.i("Restore the working directory: " + workingDirectory);
	}

	/**
	 * Create a dirs in the working dir
	 * 
	 * @param subdir
	 * @return full path
	 */
	public static String createDir(String subdir) {
		String path = workingDirectory + subdir;
		File dir = new File(path);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();
			if (result)
				Log.i("++ Created the Directory: " + path);
			else
				Log.w("-- Creating the Directory is failed: " + path);
			return "";
		}
		Log.i("-- The Directory already exist: " + path);
		return path;
	}

	/**
	 * Return the app name without spaces.
	 * 
	 * @return
	 */
	public static String getAppName() {
		return appName;
	}

	/**
	 * Return boolean value
	 * 
	 * @param key
	 * @param def value by default
	 * @return
	 */
	public static boolean getBoolean(String key, boolean def) {
		return preferences.getBoolean(key, def);
	}

	/**
	 * Return float value
	 * 
	 * @param key
	 * @param def value by default
	 * @return
	 */
	public static float getFloat(String key, float def) {
		return preferences.getFloat(key, def);
	}

	/**
	 * Return float value, 0 if not found
	 * 
	 * @param key
	 * @return
	 */
	public static float getFloat(String key) {
		return preferences.getFloat(key, 0);
	}

	/**
	 * Return int value
	 * 
	 * @param key
	 * @param def value by default
	 * @return
	 */
	public static int getInt(String key, int def) {
		return preferences.getInt(key, def);
	}

	/**
	 * Return int value, 0 if not found
	 * 
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		return preferences.getInt(key, 0);
	}

	/**
	 * Return long value
	 * 
	 * @param key
	 * @param def value by default
	 * @return
	 */
	public static long getLong(String key, long def) {
		return preferences.getLong(key, def);
	}

	/**
	 * Return long value, 0 if not found
	 * 
	 * @param key
	 * @return
	 */
	public static long getLong(String key) {
		return preferences.getLong(key, 0);
	}

	/**
	 * Return value or "" if not found
	 * 
	 * @param key
	 */
	public static String getString(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * Return value
	 * 
	 * @param key
	 * @param def value by default
	 * @return
	 */
	public static String getString(String key, String def) {
		return preferences.getString(key, def);
	}

	/**
	 * Return value or null if compiled for min SDK version < 11 (HONEYCOMB)
	 * 
	 * @param key
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Set<String> getStringSet(String key, Set<String> set) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return preferences.getStringSet(key, set);
		} else {
			try {
				throw new Exception("Not supported before version API 11 (HONEYCOMB)");
			} catch (Exception tr) {
				Log.e(tr);
			}
		}
		return null;
	}

	/**
	 * Return true if new version of app is started
	 * 
	 * @return
	 */
	public static boolean isNewVersion() {
		return isNewVersion;
	}

	/**
	 * Return true if app is absolutely new
	 * 
	 * @return
	 */
	public static boolean isNewApplication() {
		return isNewApplication;
	}

	/**
	 * Return true if app is totally fresh (any old app dirs is absent in to the working dir)
	 * 
	 * @return
	 */
	public static boolean isFreshInstallation() {
		return isFreshInstallation;
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	public static void putSetting(String key, boolean value) {
		editor.putBoolean(key, value);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	public static void putSetting(String key, float value) {
		editor.putFloat(key, value);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	public static void putSetting(String key, int value) {
		editor.putInt(key, value);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	public static void putSetting(String key, long value) {
		editor.putLong(key, value);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	public static void putSetting(String key, String value) {
		editor.putString(key, value);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putSetting(String key, Set<String> values) {
		editor.putStringSet(key, values);
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	public static void putSetting(String key, boolean value, boolean isNeedToSave) {
		editor.putBoolean(key, value);
		if (isNeedToSave) {
			save();
		}
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	public static void putSetting(String key, float value, boolean isNeedToSave) {
		editor.putFloat(key, value);
		if (isNeedToSave) {
			save();
		}
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	public static void putSetting(String key, int value, boolean isNeedToSave) {
		editor.putInt(key, value);
		if (isNeedToSave) {
			save();
		}
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	public static void putSetting(String key, long value, boolean isNeedToSave) {
		editor.putLong(key, value);
		if (isNeedToSave) {
			save();
		}
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	public static void putSetting(String key, String value, boolean isNeedToSave) {
		editor.putString(key, value);
		if (isNeedToSave) {
			save();
		}
	}

	/**
	 * Set a new for this key
	 * 
	 * @param key
	 * @param value
	 * @param isNeedToSave Go write the SharedPreferences or postpone.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putSetting(String key, Set<String> values, boolean isNeedToSave) {
		editor.putStringSet(key, values);
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
	 * @param context
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void enableStrictMode() {
		try {
			int appFlags = getContext().getApplicationInfo().flags;
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
	 * @return
	 */
	public static boolean isStrictModeEnabled() {
		return isStrictMode;
	}

	/**
	 * Return true if application run in debug mode
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDebugMode() {
		return (0 != (getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	/**
	 * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
	 * 
	 * @param context
	 * @return List<ResolveInfo>
	 */
	public static List<ResolveInfo> getAllActivitiesInfo() {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = getContext().getPackageManager();
		return pm.queryIntentActivities(intent, 0);
	}

	/**
	 * Print installed apps classes names
	 * 
	 * @param context
	 * @return
	 */
	public static void printInstalledAppsPackageAndClass() {
		for (ResolveInfo info : getAllActivitiesInfo()) {
			Log.d("Package: " + info.activityInfo.packageName + " Class: " + info.activityInfo.name);
		}
	}

	public static boolean checkInstalledPackage(String packageName) {
		for (ResolveInfo info : getAllActivitiesInfo()) {
			if (info.activityInfo.packageName.equals(packageName))
				return true;
		}
		return false;
	}

	/**
	 * Checks for an installed application
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isExistApp(String packageName) {
		for (ResolveInfo info : getAllActivitiesInfo()) {
			if (info.activityInfo.packageName.equals(packageName))
				return true;
		}
		return false;
	}

	/**
	 * Return application Context
	 * 
	 * @return
	 */
	public static Context getContext() {
		if (isInitializing())
			return myContext;
		else
			return null;
	}

	/**
	 * Call this method to delete any cache created by app
	 * 
	 * @param context context for your application
	 */
	public static void clearCashedApplicationData() {
		File cache = getContext().getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				File f = new File(appDir, s);
				if (deleteDir(f))
					Log.i(String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * Print the KeyHash for this application
	 */
	public static String getApplicationSignatureKeyHash() {
		try {
			PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getApplicationInfo().packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				return Base64.encodeToString(md.digest(), Base64.DEFAULT);
			}
		} catch (Exception e) {
			Log.e(e);
		}
		return "";
	}

	/**
	 * Check evaluate date and finish activity if expired.
	 * 
	 * @param activity
	 * @param year
	 * @param month
	 * @param day
	 */
	@SuppressWarnings("deprecation")
	public static void checkTrial(Activity activity, int year, int month, int day) {
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

}
