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
package ua.at.tsvetkov.netchecker;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ua.at.tsvetkov.util.Log;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Check a web site status.
 * 
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class NetChecker {

	public static final int		TIMEOUT_DEFAULT	= 5000;
	public static int				timeout				= TIMEOUT_DEFAULT;
	private static long			beginTime;
	private static NetStatus	result;

	/**
	 * Check a web site status.
	 * 
	 * @param context current Context
	 * @param urlStr web site URL
	 * @return {@link NetStatus}
	 */
	public static NetStatus checkNet(Context context, final String urlStr) {
		if (isOnline(context) == false) {
			return NetStatus.CONNECTION_MISSING;
		}
		beginTime = System.currentTimeMillis();
		result = null;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Log.w("Interrupted!");
					}
					long timeReplay = System.currentTimeMillis() - beginTime;
					if (result != null) {
						Log.v("Checked Server response time " + timeReplay + " ms. [" + urlStr + " ]");
						break;
					} else if (timeReplay > timeout) {
						result = NetStatus.NO_NET;
						Log.w("Timeout! Server [ " + urlStr + " ] response time exceeds " + timeReplay + "ms.");
						break;
					}
				}
			}

		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(urlStr);

					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(timeout); // Timeout is in seconds
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						result = NetStatus.NET_OK;
					}
					urlc.disconnect();
					urlc = null;
				} catch (MalformedURLException e1) {
					result = NetStatus.FAULTY_URL;
				} catch (Exception e) {
					result = NetStatus.NO_NET;
				}
			}
		}).start();
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.w("Interrupted!");
			}
			if (result != null) {
				break;
			}
		}
		return result;
	}

	private static boolean isOnline(Context context) {
		if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
			return false;
		} else {
			return true;
		}
	}

}
