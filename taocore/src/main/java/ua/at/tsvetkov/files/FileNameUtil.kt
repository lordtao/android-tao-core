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
import java.io.File

/**
 * Methods for dismantling the filename
 *
 * @author Alexandr Tsvetkov 2015
 */
object FileNameUtil {

    /**
     * Return file name with extension without directories.
     *
     * @param fullPath full path to file
     * @return file name or empty string if fullPath have not include the correct filename.
     */
    fun getFileName(fullPath: String?): String {
        var fileName = ""
        if (fullPath == null) {
            Log.w("The path to file is null")
            return fileName
        }
        if (fullPath.length == 0) {
            Log.w("The path to file is empty")
            return fileName
        }
        val indexOf = fullPath.lastIndexOf(File.separator)
        if (indexOf == -1) {
            return fullPath
        }
        try {
            fileName = fullPath.substring(indexOf + 1, fullPath.length)
        } catch (e: IndexOutOfBoundsException) {
            Log.wtf("IndexOutOfBoundsException in $fullPath")
        }

        return fileName
    }

    /**
     * Return file name without extension.
     *
     * @param fullPath full path to file
     * @return file name without extension or empty string if fullPath have not include the correct filename or have empty extension.
     */
    fun getFileNameWithoutExtension(fullPath: String): String {
        var fileName = getFileName(fullPath)
        if (fileName.length == 0) {
            Log.w("The file name is empty")
            return ""
        }
        val pos = fileName.lastIndexOf(".")
        if (pos != -1) {
            fileName = fileName.substring(0, pos)
        }
        return fileName
    }

    /**
     * Return file extension
     *
     * @param fullPath full path to file
     * @return file extension or empty string if fullPath have not include the correct filename or have empty extension.
     */
    fun getFileExtension(fullPath: String?): String {
        var fileName = ""
        if (fullPath == null) {
            Log.w("The path to file is null")
            return fileName
        }
        if (fullPath.length == 0) {
            Log.w("The path to file is empty")
            return fileName
        }
        val pos = fullPath.lastIndexOf(".")
        if (pos != -1) {
            fileName = fullPath.substring(pos + 1, fullPath.length)
        }
        return fileName
    }

    /**
     * Return path for this file without file name
     *
     * @param fullPath full path to file
     * @return path without file name
     */
    fun getFilePath(fullPath: String?): String {
        var fileName = ""
        if (fullPath == null) {
            Log.w("The path to file is null")
            return fileName
        }
        if (fullPath.length == 0) {
            Log.w("The path to file is empty")
            return fileName
        }
        val pos = fullPath.lastIndexOf(File.separator)
        if (pos != -1) {
            fileName = fullPath.substring(0, pos + 1)
            return fileName
        }
        return ""
    }


    /**
     * Return full path to file from given file name in work dir.
     *
     * @param fileName file name
     * @return full path to file
     */
    fun getFileName(context: Context, fileName: String): String {
        return getFileName(context, fileName)

    }

    /**
     * Return full path to file from given file name in work dir.
     *
     * @param subdir   subdir in work dir, possible to be empty or null.
     * @param fileName file name
     * @return full path to file
     */
    fun getFileName(context: Context, subdir: String, fileName: String): String {
        val dir: String
        if (subdir != null && subdir.length > 0) {
            dir = context.filesDir.toString() + subdir
        } else {
            dir = context.filesDir.absolutePath
        }
        return dir + fileName
    }

    /**
     * Return file name without extension
     *
     * @param path full path to file
     * @return file name without extension
     */
    fun getFileNameNoExtension(path: String): String {
        var name = ""
        try {
            val pos = path.lastIndexOf(File.separator) + 1
            val dotPos = path.lastIndexOf(".") + 1
            name = path.substring(pos, dotPos)
        } catch (e: Exception) {
            Log.e(e)
        }

        return name
    }

}
