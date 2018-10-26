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
package ua.at.tsvetkov.files

import ua.at.tsvetkov.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Methods for downloading files from the Internet
 *
 * @author A.Tsvetkov 2013 http://tsvetkov.at.ua mailto:al@ukr.net
 */
class FileDownloader {

    abstract inner class CompleteListener {

        abstract fun complete(fileName: String, result: Boolean)

    }

    companion object {

        val TIMEOUT = 10000
        val BUFFER = 8192

        /**
         * Returns the content length in bytes specified by the response header field content-length or -1 if this field is not set.
         *
         * @param url url path to file
         * @return the value of the response header field content-length
         */
        @JvmStatic
        fun getFileLength(url: String): Int {
            var conn: HttpURLConnection? = null
            var length = 0
            try {
                conn = URL(url).openConnection() as HttpURLConnection
                conn.setRequestProperty("keep-alive", "false")
                conn.doInput = true
                conn.connectTimeout = TIMEOUT
                conn.connect()
                length = conn.contentLength
            } catch (e: Exception) {
                Log.e(e)
            } finally {
                if (conn != null)
                    conn.disconnect()
            }
            return length
        }

        /**
         * Async downloads a remote file and stores it locally.
         *
         * @param url             Remote URL of the file to download
         * @param pathAndFileName Local path with file name where to store the file
         * @param rewrite         If TRUE and file exist - rewrite the file. If FALSE and file exist and his length > 0 - not download and rewrite the
         * file.
         */
        @JvmStatic
        fun download(url: String, pathAndFileName: String, rewrite: Boolean, listener: CompleteListener) {
            Thread(Runnable {
                val result = download(url, pathAndFileName, rewrite)
                listener.complete(url, result)
            }, "Download thread: $url").start()
        }

        /**
         * Downloads a remote file and stores it locally.
         *
         * @param url             Remote URL of the file to download. If the string contains spaces, they are replaced by "%20".
         * @param pathAndFileName Local path with file name where to store the file
         * @param rewrite         If TRUE and file exist - rewrite the file. If FALSE and file exist and his length > 0 - not download and rewrite the file.
         * @return true if success
         */
        @JvmOverloads
        @JvmStatic
        fun download(url: String, pathAndFileName: String, rewrite: Boolean = true): Boolean {
            var urlEnc: String? = null
            var conn: HttpURLConnection? = null
            val input: InputStream
            var output: FileOutputStream? = null

            try {
                urlEnc = URLEncoder.encode(url, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                Log.e("Wrong url", e)
            }

            val f = File(pathAndFileName)
            if (!rewrite && f.exists() && f.length() > 0) {
                Log.w("File exist: $pathAndFileName")
                return true
            }

            try {
                conn = URL(urlEnc).openConnection() as HttpURLConnection
                conn.setRequestProperty("keep-alive", "false")
                conn.doInput = true
                conn.connectTimeout = TIMEOUT
                conn.connect()
                val fileLength = conn.contentLength
                if (fileLength == 0) {
                    Log.v("File is empty, length = 0 > $url")
                }
                input = conn.inputStream
                output = FileOutputStream(pathAndFileName)
                val buffer = ByteArray(BUFFER)
                var bytesRead: Int
                while (true) {
                    bytesRead = input.read(buffer)
                    if (bytesRead < 0) break
                    output.write(buffer, 0, bytesRead)
                }
                output.flush()
            } catch (e: IOException) {
                Log.w("Download error $url", e)
                return false
            } finally {
                if (conn != null)
                    conn.disconnect()
                try {
                    output!!.close()
                } catch (e: IOException) {
                    Log.e("Error closing file > $pathAndFileName", e)
                    return false
                }

            }
            return true
        }
    }
}
