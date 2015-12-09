/**
 * ****************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
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
package ua.at.tsvetkov.netchecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ua.at.tsvetkov.util.Log;

/**
 * Check a network status.
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class Net {

    /**
     * Reports the current coarse-grained state of the network.
     * @param context
     * @return coarse-grained state
     */
    public static NetworkInfo.State getState(Context context) {
        NetworkInfo netInfo = getNetworkInfo(context);
        return netInfo.getState();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo netInfo = getNetworkInfo(context);
        return netInfo.isConnected();
    }

    public static void printState(Context context) {
        Log.v("Network state: " + getState(context));

    }

    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

}
