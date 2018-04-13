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

import android.content.Context
import ua.at.tsvetkov.util.Log
import java.io.*

/**
 * Methods for file and directory operations
 *
 * @author Alexandr Tsvetkov 2014
 */
object FileOperations {

    private val BUFFER_SIZE = 8192

    /**
     * Copy file from source to destination
     *
     * @param srcFileName source file path
     * @param dstFileName destination file path
     * @return true if success
     */
    fun copy(srcFileName: String, dstFileName: String): Boolean {
        if (srcFileName == null || srcFileName.length == 0) {
            Log.e("Source file name is empty.")
            return false
        }
        if (dstFileName == null) {
            Log.e("Destination file name is empty.")
            return false
        }
        var inp: InputStream? = null
        var out: OutputStream? = null
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
            return false
        } finally {
            try {
                if (inp != null)
                    inp.close()
            } catch (e: IOException) {
                Log.e(e)
                return false
            }

            try {
                if (out != null)
                    out.close()
            } catch (e: IOException) {
                Log.e(e)
                return false
            }

        }
        Log.v("Success copied file $srcFileName to $dstFileName")
        return true
    }

    /**
     * Copy file from source to destination
     *
     * @param srcFileName source file name
     * @param dstFile     destination file
     * @return true if success
     */
    fun copy(srcFileName: String, dstFile: File): Boolean {
        if (srcFileName == null || srcFileName.length == 0) {
            Log.e("Source file name is empty.")
            return false
        }
        if (dstFile == null) {
            Log.e("Destination file is null.")
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
    fun copy(srcFile: File, dstFileName: String): Boolean {
        if (srcFile == null || !srcFile.exists()) {
            Log.e("Source file is null or not exist.")
            return false
        }
        if (dstFileName == null) {
            Log.e("Destination file name is empty.")
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
    fun copy(srcFile: File, dstFile: File): Boolean {
        if (srcFile == null || !srcFile.exists()) {
            Log.e("Source file is null or not exist.")
            return false
        }
        if (dstFile == null) {
            Log.e("Destination file is null.")
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
    fun copyAsset(context: Context, assetsFileName: String, dstFileName: String): Boolean {
        var inp: BufferedInputStream? = null
        var out: BufferedOutputStream? = null
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
                if (inp != null)
                    inp.close()
            } catch (e: IOException) {
                Log.e(e)
                return false
            }

            try {
                if (out != null)
                    out.close()
            } catch (e: IOException) {
                Log.e(e)
                return false
            }

        }
        Log.v("Success copy file $assetsFileName to $dstFileName")
        return true
    }

    /**
     * Copy file from assets source to destination
     *
     * @param context        base app context
     * @param assetsFileName assets file name
     * @param dstFile        destination of copy
     * @return true if success
     */
    fun copyAsset(context: Context, assetsFileName: String, dstFile: File): Boolean {
        if (assetsFileName == null || assetsFileName.length == 0) {
            Log.e("Assets file name is empty.")
            return false
        }
        if (dstFile == null) {
            Log.e("Destination file is null.")
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
    fun clearCashedApplicationData(context: Context) {
        val cache = context.cacheDir
        val appDir = File(cache.parent)
        if (appDir.exists()) {
            val children = appDir.list()
            for (s in children) {
                val f = File(appDir, s)
                if (deleteDir(f)) {
                    Log.i(String.format("**************** DELETED -> (%s) *******************", f.absolutePath))
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
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            Log.e("Directory eq null - can't delete.")
            return false
        }
        if (dir.isDirectory) {
            val children = dir.list()
            for (element in children) {
                val success = deleteDir(File(dir, element))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

}
