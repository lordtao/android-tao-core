/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ua.at.tsvetkov.util.Log;
import ua.at.tsvetkov.util.LogFormatter;

/**
 * Return an informations about another applications
 *
 * @author Alexandr Tsvetkov 2015
 */
public final class Apps {

   private Apps() {

   }

   /**
    * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
    *
    * @param context the application Context
    * @return List<ResolveInfo>
    */
   public static List<ResolveInfo> getInstalledAppsInfo(Context context) {
      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      PackageManager pm = context.getPackageManager();
      return pm.queryIntentActivities(intent, 0);
   }

   /**
    * Print installed apps classes names
    *
    * @param context the application Context
    */
   public static void printInstalledAppsPackageAndClass(Context context) {
      List<String> appListStrs = new ArrayList<>();
      for (ResolveInfo info : getInstalledAppsInfo(context)) {
         appListStrs.add("NAME:" + info.loadLabel(context.getPackageManager()) + ", PACKAGE: " + info.activityInfo.packageName + ", LAUNCHER CLASS: " + info.activityInfo.name);
      }
      Log.list("Installed application", appListStrs);
   }

   /**
    * Checks for an installed application
    *
    * @param packageName app package name
    * @param context     the application Context
    * @return is an installed application
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static boolean isApplicationInstalled(Context context, String packageName) throws IllegalAccessException {
      for (ResolveInfo info : getInstalledAppsInfo(context)) {
         if (info.activityInfo.packageName.equals(packageName)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Return the KeyHash for the application
    *
    * @param context     the application Context
    * @param packageName a package name
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
    * Return the app certificate's fingerprint
    *
    * @param context     the application Context
    * @param packageName a package name
    * @return certificate's fingerprint
    */
   public static String getSignatureFingerprint(@NonNull Context context, @NonNull String packageName) {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-1");
         Signature sig = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0];
         return (LogFormatter.hexWithColons(md.digest(sig.toByteArray())));
      } catch (NoSuchAlgorithmException e) {
         Log.e(e);
      } catch (PackageManager.NameNotFoundException e) {
         Log.e(e);
      }
      return "ERROR calculate the app certificate's fingerprint";
   }

   /**
    * Return the app certificate's fingerprint
    *
    * @param packageName a package name
    * @return certificate's fingerprint
    */
   @Deprecated
   public static String getSignatureFingerprint(String packageName) {
      throw new UnsupportedOperationException("Use getSignatureFingerprint(Context context) instead getSignatureFingerprint().");
   }

}
