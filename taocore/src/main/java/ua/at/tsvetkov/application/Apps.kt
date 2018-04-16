/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 *
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 *
 *
 * Project:
 * TAO Core
 *
 *
 * License agreement:
 *
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.application

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Base64
import ua.at.tsvetkov.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Return an information about another applications
 *
 * @author Alexandr Tsvetkov 2015
 */
object Apps {

    /**
     * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
     *
     * @param context the application Context
     * @return List<ResolveInfo>
    </ResolveInfo> */
    fun getInstalledAppsInfo(context: Context): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = context.packageManager
        return pm.queryIntentActivities(intent, 0)
    }

    /**
     * Print installed apps classes names
     *
     * @param context the application Context
     */
    fun printInstalledAppsPackageAndClass(context: Context) {
        val appListStrs = ArrayList<String>()
        for (info in getInstalledAppsInfo(context)) {
            appListStrs.add("NAME:" + info.loadLabel(context.packageManager) + ", PACKAGE: " + info.activityInfo.packageName + ", LAUNCHER CLASS: " + info.activityInfo.name)
        }
        Log.list(appListStrs, "Installed application")
    }

    /**
     * Checks for an installed application
     *
     * @param packageName app package name
     * @param context     the application Context
     * @return is an installed application
     * @throws IllegalAccessException if AppConfig is not initialized
     */
    @Throws(IllegalAccessException::class)
    fun isApplicationInstalled(context: Context, packageName: String): Boolean {
        for (info in getInstalledAppsInfo(context)) {
            if (info.activityInfo.packageName == packageName) {
                return true
            }
        }
        return false
    }

    /**
     * Return the KeyHash for the application
     *
     * @param context     the application Context
     * @param packageName a package name
     */
    fun getApplicationSignatureKeyHash(context: Context, packageName: String): String {
        try {
            val info = context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.DEFAULT)
            }
        } catch (e: Exception) {
            Log.e(e)
        }

        return ""
    }

    /**
     * Return the app certificate's fingerprint
     *
     * @param context     the application Context
     * @param packageName a package name
     * @return certificate's fingerprint
     */
    fun getSignatureFingerprint(context: Context, packageName: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-1")
            val sig = context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0]
            return hex(md.digest(sig.toByteArray()))
        } catch (e: NoSuchAlgorithmException) {
            Log.e(e)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(e)
        }

        return "ERROR calculate the app certificate's fingerprint"
    }

    private fun hex(data: ByteArray?): String {
        if (data == null) {
            Log.v("null")
        }

        val sb = StringBuilder(data!!.size * 3)
        val var2 = data
        val var3 = data.size

        for (var4 in 0 until var3) {
            val element = var2[var4]
            sb.append(String.format("%02X ", element))
        }

        sb.trimToSize()
        return sb.toString()
    }

}
