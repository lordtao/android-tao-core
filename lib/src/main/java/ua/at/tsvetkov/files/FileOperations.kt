/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
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
package ua.at.tsvetkov.files

import android.content.Context
import ua.at.tsvetkov.util.logger.Log
import java.io.*

/**
 * Methods for file and directory operations
 *
 * @author Alexandr Tsvetkov 2014
 */
class FileOperations {

    companion object {

        private const val BUFFER_SIZE = 8192

        /**
         * Copy file from source to destination
         *
         * @param srcFileName source file path
         * @param dstFileName destination file path
         * @return true if success
         */
        @JvmStatic
        fun copy(srcFileName: String, dstFileName: String): Boolean {
            if (srcFileName.isEmpty()) {
                Log.e("Source file name is empty.")
                return false
            }
            var inp: InputStream? = null
            var out: OutputStream? = null
            var isSuccess = false
            try {
                inp = BufferedInputStream(FileInputStream(srcFileName))
                out = BufferedOutputStream(FileOutputStream(dstFileName))
                val buffer = ByteArray(BUFFER_SIZE)
                var count: Int
                while (true) {
                    count = inp.read(buffer)
                    if (count < 0) break
                    out.write(buffer, 0, count)
                }
                out.flush()
            } catch (e: IOException) {
                Log.e("Can't copy file $srcFileName to $dstFileName", e)
            } finally {
                try {
                    inp?.close()
                    try {
                        out?.close()
                        isSuccess = true
                        Log.v("Success copied file $srcFileName to $dstFileName")
                    } catch (e: IOException) {
                        Log.e(e)
                    }
                } catch (e: IOException) {
                    Log.e(e)
                }
            }
            return isSuccess
        }

        /**
         * Copy file from source to destination
         *
         * @param srcFileName source file name
         * @param dstFile     destination file
         * @return true if success
         */
        @JvmStatic
        fun copy(srcFileName: String, dstFile: File): Boolean {
            if (srcFileName.isEmpty()) {
                Log.e("Source file name is empty.")
                return false
            }
            return copy(srcFileName, dstFile.absoluteFile)
        }

        /**
         * Copy file from source to destination
         *
         * @param srcFile     source file
         * @param dstFileName destination file name
         * @return true if success
         */
        @JvmStatic
        fun copy(srcFile: File, dstFileName: String): Boolean {
            if (!srcFile.exists()) {
                Log.e("Source file is null or not exist.")
                return false
            }
            return copy(srcFile.absoluteFile, dstFileName)
        }

        /**
         * Copy file from source to destination
         *
         * @param srcFile source file
         * @param dstFile destination file
         * @return true if success
         */
        @JvmStatic
        fun copy(srcFile: File, dstFile: File): Boolean {
            if (!srcFile.exists()) {
                Log.e("Source file is null or not exist.")
                return false
            }
            return copy(srcFile.absoluteFile, dstFile.absoluteFile)
        }

        /**
         * Copy file from assets source to destination
         *
         * @param context        base app context
         * @param assetsFileName assets file name
         * @param dstFileName    destination of copy
         * @return true if success
         */
        @JvmStatic
        fun copyAsset(context: Context, assetsFileName: String, dstFileName: String): Boolean {
            var inp: BufferedInputStream? = null
            var out: BufferedOutputStream? = null
            var isSuccess = false
            try {
                inp = BufferedInputStream(context.assets.open(assetsFileName))
                out = BufferedOutputStream(FileOutputStream(dstFileName))
                val buf = ByteArray(BUFFER_SIZE)
                var len: Int
                while (true) {
                    len = inp.read(buf)
                    if (len < 0) break
                    out.write(buf, 0, len)
                }
                out.flush()
            } catch (e: IOException) {
                Log.e("Can't copy file from assets $assetsFileName to $dstFileName", e)
                return false
            } finally {
                try {
                    inp?.close()
                    try {
                        out?.close()
                        isSuccess = true
                        Log.v("Success copy file $assetsFileName to $dstFileName")
                    } catch (e: IOException) {
                        Log.e(e)
                    }
                } catch (e: IOException) {
                    Log.e(e)
                }

            }
            return isSuccess
        }

        /**
         * Copy file from assets source to destination
         *
         * @param context        base app context
         * @param assetsFileName assets file name
         * @param dstFile        destination of copy
         * @return true if success
         */
        @JvmStatic
        fun copyAsset(context: Context, assetsFileName: String, dstFile: File): Boolean {
            if (assetsFileName.isEmpty()) {
                Log.e("Assets file name is empty.")
                return false
            }
            return copyAsset(context, assetsFileName, dstFile.absolutePath)
        }

        /**
         * Delete file
         *
         * @param fileName file for delete
         * @return true if success
         */
        @JvmStatic
        fun delete(fileName: String): Boolean {
            val file = File(fileName)
            val result = file.delete()
            if (result) {
                Log.v("File success deleted $fileName")
            } else {
                Log.w("Fail to delete file $fileName")
            }
            return result
        }

        /**
         * Delete content from directory
         *
         * @param pathName path for delete a content
         */
        @JvmStatic
        fun deleteDirContent(pathName: String) {
            val path = File(pathName)
            val files = path.list()
            if (files != null) {
                for (fileName in files) {
                    val file = File(fileName)
                    val result = file.delete()
                    if (result) {
                        Log.v("File success deleted $fileName")
                    } else {
                        Log.w("Fail to delete file $fileName")
                    }
                }
            }
        }

        /**
         * Rename file
         *
         * @param srcFileName source file name
         * @param dstFileName reamed file name
         * @return true if success
         */
        @JvmStatic
        fun rename(srcFileName: String, dstFileName: String): Boolean {
            val src = File(srcFileName)
            val dst = File(dstFileName)
            val result = src.renameTo(dst)
            if (result) {
                Log.v("File success renamed to $dstFileName")
            } else {
                Log.w("Fail to rename file $srcFileName")
            }
            return result
        }

        /**
         * Create a dirs in the working dir
         *
         * @param subDir sub directory
         * @return full path
         */
        @JvmStatic
        fun createDir(context: Context, subDir: String): String {
            val path = context.filesDir.toString() + subDir
            val dir = File(path)
            if (!dir.exists()) {
                val result = dir.mkdirs()
                if (result) {
                    Log.i("++ Created the Directory: $path")
                } else {
                    Log.w("-- Creating the Directory is failed: $path")
                }
                return ""
            }
            return path
        }

        /**
         * Call this method to delete any cache created by app
         */
        @JvmStatic
        fun clearCashedApplicationData(context: Context) {
            context.cacheDir.parent?.let {
                val appDir = File(it)
                if (appDir.exists()) {
                    appDir.list()?.forEach { child ->
                        val file = File(appDir, child)
                        if (deleteDir(file)) {
                            Log.i(String.format("**************** DELETED -> (%s) *******************", file.absolutePath))
                        }
                    }
                }
            }
        }

        /**
         * Delete directory with a subdirs and a files
         *
         * @param dir directory for delete
         * @return tue if success
         */
        @JvmStatic
        fun deleteDir(dir: File): Boolean {
            if (dir.isDirectory) {
                dir.list()?.forEach { child ->
                    if (!deleteDir(File(dir, child))) {
                        return false
                    }
                }
            }
            return dir.delete()
        }

    }
}
