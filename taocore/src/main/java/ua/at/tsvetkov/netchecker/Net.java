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
package ua.at.tsvetkov.netchecker;

import ua.at.tsvetkov.application.AppConfig;
import ua.at.tsvetkov.util.Log;

import android.app.Activity;
import android.widget.Toast;

/**
 * Check a web site connection.
 *
 * @version 2.0
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class Net {

   private static final String URL_OF_THE_SERVER_IS_FAULTY = "Url of the server is faulty";
   private static final String USER_HAS_ENABLED_NETWORK_BUT_IT_S_NOT_WORKING = "User has enabled network, but it's not working";
   private static final String USER_SHOULD_ENABLE_INTERNET = "User should enable WiFi/3g etc";
   private static final String NET_IS_OK = "The network is connected. The site is available.";
   private static final String NET_STATUS_IS_NOT_DEFINED = "Network Status is not defined.";

   /**
    * Check a web site connection, print result to log.
    *
    * @param urlStr web site url
    * @return is site accessible
    */
   public static boolean isAccessible(String urlStr) {
      switch (NetChecker.checkNet(AppConfig.getContext(), urlStr)) {
         case CONNECTION_MISSING:
            Log.w(USER_SHOULD_ENABLE_INTERNET);
            return false;
         case NO_NET:
            Log.w(USER_HAS_ENABLED_NETWORK_BUT_IT_S_NOT_WORKING);
            return false;
         case FAULTY_URL:
            Log.w(URL_OF_THE_SERVER_IS_FAULTY);
            return false;
         case NET_OK:
            Log.v(NET_IS_OK);
            return true;
         case NOT_DEFINED_YET:
            Log.wtf(NET_STATUS_IS_NOT_DEFINED);
            break;
      }
      return false;
   }

   /**
    * Check a web site connection, print result to log and show Toast notification.
    *
    * @param activity current Activity
    * @param urlStr web site url
    * @return is site accessible
    */
   public static boolean isAccessible(Activity activity, String urlStr) {
      switch (NetChecker.checkNet(AppConfig.getContext(), urlStr)) {
         case CONNECTION_MISSING:
            message(activity, USER_SHOULD_ENABLE_INTERNET);
            return false;
         case NO_NET:
            message(activity, USER_HAS_ENABLED_NETWORK_BUT_IT_S_NOT_WORKING);
            return false;
         case FAULTY_URL:
            message(activity, URL_OF_THE_SERVER_IS_FAULTY);
            return false;
         case NET_OK:
            return true;
         case NOT_DEFINED_YET:
            message(activity, URL_OF_THE_SERVER_IS_FAULTY);
            break;
      }
      return false;
   }

   /**
    * Check a web site connection, print result to log.
    *
    * @param urlStr web site url
    * @param timeout timeout, ms.
    * @return is site accessible
    */
   public static boolean isAccessible(String urlStr, int timeout) {
      NetChecker.setTimeout(timeout);
      boolean result = isAccessible(urlStr);
      NetChecker.setTimeout(NetChecker.DEFAULT_TIMEOUT);
      return result;
   }

   /**
    * Check a web site connection, print result to log and show Toast notification.
    *
    * @param activity current Activity
    * @param urlStr web site url
    * @param timeout timeout, ms.
    * @return is site accessible
    */
   public static boolean isAccessible(Activity activity, String urlStr, int timeout) {
      NetChecker.setTimeout(timeout);
      boolean result = isAccessible(activity, urlStr);
      NetChecker.setTimeout(NetChecker.DEFAULT_TIMEOUT);
      return result;
   }

   /**
    * Show error message in toast and print to error log.
    *
    * @param activity current Activity
    * @param mes message to toast and log
    */
   private static void message(final Activity activity, final String mes) {
      activity.runOnUiThread(new Runnable() {

         @Override
         public void run() {
            Toast.makeText(activity, mes, Toast.LENGTH_SHORT).show();
            Log.e("Connection Error - " + mes);
         }
      });
   }
}
