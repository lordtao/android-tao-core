/**
 * 
 */
package ua.at.tsvetkov.io;

import java.io.File;

import ua.at.tsvetkov.util.Log;

/**
 * @author Alexandr Tsvetkov 2015
 */
public class FilePath {

   /**
    * Return file name with extension without directories.
    * 
    * @param fullPath.
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
    * @param fullPath.
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
    * @param fullPath
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

}
