/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p>
 * Project:
 * TAO Core
 * <p>
 * License agreement:
 * <p>
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import ua.at.tsvetkov.util.Log;

/**
 * Return an informations about another applications
 *
 * @author Alexandr Tsvetkov 2015
 */
public final class Apps {

    /**
     * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
     *
     * @param context
     * @return List<ResolveInfo>
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    public static List<ResolveInfo> getAllActivitiesInfo(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        return pm.queryIntentActivities(intent, 0);
    }

    /**
     * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
     *
     * @return List<ResolveInfo>
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    @Deprecated
    public static List<ResolveInfo> getAllActivitiesInfo() throws IllegalAccessException {
        throw new UnsupportedOperationException("Use getAllActivitiesInfo(Context context) instead getAllActivitiesInfo().");
    }

    /**
     * Print installed apps classes names
     *
     * @param context
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    public static void printInstalledAppsPackageAndClass(Context context) {
        for (ResolveInfo info : getAllActivitiesInfo(context)) {
            Log.d("Package: " + info.activityInfo.packageName + " Class: " + info.activityInfo.name);
        }
    }

    /**
     * Print installed apps classes names
     *
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    @Deprecated
    public static void printInstalledAppsPackageAndClass() throws IllegalAccessException {
        throw new UnsupportedOperationException("Use printInstalledAppsPackageAndClass(Context context) instead printInstalledAppsPackageAndClass().");
    }

    /**
     * Checks for an installed application
     *
     * @param packageName app package name
     * @param context
     * @return is an installed application
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    public static boolean isApplicationInstalled(Context context, String packageName) throws IllegalAccessException {
        for (ResolveInfo info : getAllActivitiesInfo(context)) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for an installed application
     *
     * @param packageName app package name
     * @return is an installed application
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    @Deprecated
    public static boolean isApplicationInstalled(String packageName) throws IllegalAccessException {
        throw new UnsupportedOperationException("Use isApplicationInstalled(Context context) instead isApplicationInstalled().");
    }

    /**
     * Return the KeyHash for the application
     *
     * @param context
     * @param packageName
     */
    public static String getApplicationSignatureKeyHash(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
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
     * Return the KeyHash for the application
     *
     * @param packageName
     */
    @Deprecated
    public static String getApplicationSignatureKeyHash(String packageName) {
        throw new UnsupportedOperationException("Use getApplicationSignatureKeyHash(Context context) instead getApplicationSignatureKeyHash().");
    }

    /**
     * Return the app certificate's fingerprint
     *
     * @param context
     * @param packageName
     * @return certificate's fingerprint
     */
    public static String getSignatureFingerprint(Context context, String packageName) {
        MessageDigest md = null;
        Signature sig = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            sig = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0];
            return (toHexStringWithColons(md.digest(sig.toByteArray())));
        } catch (NoSuchAlgorithmException e) {
            Log.e(e);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "ERROR calculate the app certificate's fingerprint";
    }

    /**
     * Return the app certificate's fingerprint
     *
     * @param packageName
     * @return certificate's fingerprint
     */
    @Deprecated
    public static String getSignatureFingerprint(String packageName) {
        throw new UnsupportedOperationException("Use getSignatureFingerprint(Context context) instead getSignatureFingerprint().");
    }

    private static String toHexStringWithColons(byte[] bytes) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[(bytes.length * 3) - 1];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v / 16];
            hexChars[j * 3 + 1] = hexArray[v % 16];

            if (j < bytes.length - 1) {
                hexChars[j * 3 + 2] = ':';
            }
        }

        return new String(hexChars);
    }
}
