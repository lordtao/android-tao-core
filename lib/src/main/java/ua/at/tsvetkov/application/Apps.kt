/**
 * ****************************************************************************
 * Copyright (c) 2014-2025 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 * TAO Core
 *
 * License agreement:
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
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import ua.at.tsvetkov.util.logger.Log
import ua.at.tsvetkov.util.logger.LogLong
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * Return an information about another applications
 *
 * @author Alexandr Tsvetkov 2015
 */
class Apps {
    companion object {

        const val SHA_1 = "SHA-1"
        const val SHA_256 = "SHA-256"

        /**
         * Return info about installed on this device apps with CATEGORY_LAUNCHER (usual apps)
         *
         * @param context the application Context
         * @return List<ResolveInfo>
        </ResolveInfo> */
        @JvmStatic
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
        @JvmStatic
        fun printInstalledAppsPackageAndClass(context: Context) {
            val appListStrs = ArrayList<String>()
            for (info in getInstalledAppsInfo(context)) {
                appListStrs.add(
                    "${
                        info.loadLabel(context.packageManager).padEnd(35)
                    }${info.activityInfo.packageName.padEnd(45)}${info.activityInfo.name}"
                )
            }
            LogLong.list(appListStrs, "Installed application")
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
        @JvmStatic
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
         * @param algorithm a message digest algorithm, such as SHA-1 or SHA-256.
         * Default is SHA-1. Use "SHA-256" for SHA-256
         */
        @JvmStatic
        fun getApplicationSignatureKeyHash(context: Context, packageName: String, algorithm: String = SHA_1): String {
            getSignatures(context, packageName)?.firstOrNull()?.let { signature ->
                val md = MessageDigest.getInstance(algorithm)
                md.update(signature.toByteArray())
                val signatureHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                return signatureHash
            }
            return ""
        }

        /**
         * Return the app certificate's fingerprint
         *
         * @param context     the application Context
         * @param packageName a package name
         * @param separator a char which separate a portions oh hex codes
         * @return certificate's fingerprint
         */
        @JvmStatic
        fun getSignatureFingerprint(context: Context, packageName: String, separator: Char = ':', algorithm: String = SHA_1): String {
            getSignatures(context, packageName)?.firstOrNull()?.let { signature ->
                val md = MessageDigest.getInstance("SHA-1")
                val digest = md.digest(signature.toByteArray())
                return hex(digest, separator)
            }
            return ""
        }

        fun getSignatures(context: Context, packageName: String, algorithm: String = SHA_1): List<Signature>? {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val packageInfo = context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )

                    val signingInfo = packageInfo.signingInfo ?: run {
                        Log.e("SigningInfo is null for package: $packageName")
                        return null
                    }

                    val signatures: Array<Signature> = if (signingInfo.hasMultipleSigners()) {
                        signingInfo.apkContentsSigners
                    } else {
                        signingInfo.signingCertificateHistory
                    }

                    if (signatures.isEmpty()) {
                        Log.e("No signatures found for package: $packageName")
                    }

                    return signatures.toList()
                } else {
                    @Suppress("DEPRECATION")
                    val packageInfo = context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES
                    )
                    @Suppress("DEPRECATION")
                    return packageInfo.signatures?.toList() ?: emptyList()
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("Package not found: $packageName", e)
            } catch (e: NoSuchAlgorithmException) {
                Log.e("Hashing algorithm not found $algorithm", e)
            } catch (e: Exception) {
                Log.e("An unexpected error occurred: ${e.message}", e)
            }
            return null
        }

        @JvmStatic
        private fun hex(data: ByteArray, separator: Char): String {
            val sb = StringBuilder(data.size * 3)

            for (byte in data) {
                sb.append(String.format("%02X$separator", byte))
            }

            return sb.dropLast(1).toString()
        }

    }

}
