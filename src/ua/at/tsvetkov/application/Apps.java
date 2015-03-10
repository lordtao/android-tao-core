/*******************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 *     TAO Core
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

import java.security.MessageDigest;
import java.util.List;

import ua.at.tsvetkov.util.Log;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.util.Base64;

/**
 * Return an informations about another applications
 * 
 * @author Alexandr Tsvetkov 2015
 */
public class Apps {

   /**
    * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
    * 
    * @param context
    * @return List<ResolveInfo>
    */
   public static List<ResolveInfo> getAllActivitiesInfo() {
      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      PackageManager pm = AppConfig.getContext().getPackageManager();
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

   /**
    * Checks for an installed application
    * 
    * @param context
    * @param packageName
    * @return
    */
   public static boolean isApplicationInstalled(String packageName) {
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
