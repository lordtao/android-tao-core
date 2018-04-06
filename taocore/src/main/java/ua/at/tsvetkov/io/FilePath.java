/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
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
package ua.at.tsvetkov.io;

import java.io.File;

import ua.at.tsvetkov.util.Log;

/**
 * Methods for dismantling the filename
 *
 * @author Alexandr Tsvetkov 2015
 */
public final class FilePath {

    private FilePath() {

    }

    /**
     * Return file name with extension without directories.
     *
     * @param fullPath full path to file
     * @return file name or empty string if fullPath have not include the correct filename.
     */
    public static String getFileName(String fullPath) {
        String fileName = "";
        if (fullPath == null) {
            Log.w("The path to file is null");
            return fileName;
        }
        if (fullPath.length() == 0) {
            Log.w("The path to file is empty");
            return fileName;
        }
        int indexOf = fullPath.lastIndexOf(File.separator);
        if (indexOf == -1) {
            return fullPath;
        }
        try {
            fileName = fullPath.substring(indexOf + 1, fullPath.length());
        } catch (IndexOutOfBoundsException e) {
            Log.wtf("IndexOutOfBoundsException in " + fullPath);
        }
        return fileName;
    }

    /**
     * Return file name without extension.
     *
     * @param fullPath full path to file
     * @return file name without extension or empty string if fullPath have not include the correct filename or have empty extension.
     */
    public static String getFileNameWithoutExtension(String fullPath) {
        String fileName = getFileName(fullPath);
        if (fileName.length() == 0) {
            Log.w("The file name is empty");
            return "";
        }
        int pos = fileName.lastIndexOf(".");
        if (pos != -1) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    /**
     * Return file extension
     *
     * @param fullPath full path to file
     * @return file extension or empty string if fullPath have not include the correct filename or have empty extension.
     */
    public static String getFileExtension(String fullPath) {
        String fileName = "";
        if (fullPath == null) {
            Log.w("The path to file is null");
            return fileName;
        }
        if (fullPath.length() == 0) {
            Log.w("The path to file is empty");
            return fileName;
        }
        int pos = fullPath.lastIndexOf(".");
        if (pos != -1) {
            fileName = fullPath.substring(pos + 1, fullPath.length());
        }
        return fileName;
    }

    /**
     * Return path for this file without file name
     *
     * @param fullPath full path to file
     * @return path without file name
     */
    public static String getFilePath(String fullPath) {
        String fileName = "";
        if (fullPath == null) {
            Log.w("The path to file is null");
            return fileName;
        }
        if (fullPath.length() == 0) {
            Log.w("The path to file is empty");
            return fileName;
        }
        int pos = fullPath.lastIndexOf(File.separator);
        if (pos != -1) {
            fileName = fullPath.substring(0, pos + 1);
            return fileName;
        }
        return "";
    }

}
