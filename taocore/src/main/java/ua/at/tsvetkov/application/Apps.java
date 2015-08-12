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
import android.util.Base64;

import java.security.MessageDigest;
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
    * @return List<ResolveInfo>
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static List<ResolveInfo> getAllActivitiesInfo() throws IllegalAccessException {
      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      Context context = AppConfig.getContext();
      if(context != null) {
         PackageManager pm = context.getPackageManager();
         return pm.queryIntentActivities(intent, 0);
      } else {
         throw new IllegalAccessException("AppConfig is not initialized");
      }
   }

   /**
    * Print installed apps classes names
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static void printInstalledAppsPackageAndClass() throws IllegalAccessException {
      for (ResolveInfo info : getAllActivitiesInfo()) {
         Log.d("Package: " + info.activityInfo.packageName + " Class: " + info.activityInfo.name);
      }
   }

   /**
    * Checks for an installed application
    *
    * @param packageName app package name
    * @return is an installed application
    * @throws IllegalAccessException if AppConfig is not initialized
    */
   public static boolean isApplicationInstalled(String packageName) throws IllegalAccessException {
      for (ResolveInfo info : getAllActivitiesInfo()) {
         if (info.activityInfo.packageName.equals(packageName)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Return the KeyHash for the application
    */
   public static String getApplicationSignatureKeyHash(String packageName) {
      try {
         PackageInfo info = AppConfig.getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
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

}
