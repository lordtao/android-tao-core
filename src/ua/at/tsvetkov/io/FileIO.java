/**
 * 
 */
package ua.at.tsvetkov.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ua.at.tsvetkov.util.Log;
import android.content.Context;

/**
 * @author Alexandr Tsvetkov 2014
 */
public class FileIO {

   private static final int BUFFER_SIZE = 8192;

   /**
    * Copy file from source to destination
    * 
    * @param srcFileName source file path
    * @param dstFileName destination file path
    * @return true if success
    */
   public static boolean copy(String srcFileName, String dstFileName) {
      if (srcFileName == null || srcFileName.length() == 0) {
         Log.e("Source file name is empty.");
         return false;
      }
      if (dstFileName == null) {
         Log.e("Destination file name is empty.");
         return false;
      }
      try {
         InputStream in = new BufferedInputStream(new FileInputStream(srcFileName));
         OutputStream out = new BufferedOutputStream(new FileOutputStream(dstFileName));

         byte[] buffer = new byte[BUFFER_SIZE];
         int count = 0;

         while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
         }

         in.close();
         out.flush();
         out.close();
      } catch (IOException e) {
         Log.e("Can't copy file " + srcFileName + " to " + dstFileName, e);
         return false;
      }
      Log.v("Success copy file " + srcFileName + " to " + dstFileName);
      return true;
   }

   /**
    * Copy file from source to destination
    * 
    * @param srcFileName source file name
    * @param dstFile destination file
    * @return true if success
    */
   public static boolean copy(String srcFileName, File dstFile) {
      if (srcFileName == null || srcFileName.length() == 0) {
         Log.e("Source file name is empty.");
         return false;
      }
      if (dstFile == null) {
         Log.e("Destination file is null.");
         return false;
      }
      return copy(srcFileName, dstFile.getAbsoluteFile());
   }

   /**
    * Copy file from source to destination
    * 
    * @param srcFile source file
    * @param dstFileName destination file name
    * @return true if success
    */
   public static boolean copy(File srcFile, String dstFileName) {
      if (srcFile == null || !srcFile.exists()) {
         Log.e("Source file is null or not exist.");
         return false;
      }
      if (dstFileName == null) {
         Log.e("Destination file name is empty.");
         return false;
      }
      return copy(srcFile.getAbsoluteFile(), dstFileName);
   }

   /**
    * Copy file from source to destination
    * 
    * @param srcFile source file
    * @param dstFile destination file
    * @return true if success
    */
   public static boolean copy(File srcFile, File dstFile) {
      if (srcFile == null || !srcFile.exists()) {
         Log.e("Source file is null or not exist.");
         return false;
      }
      if (dstFile == null) {
         Log.e("Destination file is null.");
         return false;
      }
      return copy(srcFile.getAbsoluteFile(), dstFile.getAbsoluteFile());
   }

   /**
    * Copy file from assets source to destination
    * 
    * @param context
    * @param assetsFileName
    * @param dstFileName
    * @return true if success
    */
   public static boolean copyAsset(Context context, String assetsFileName, String dstFileName) {
      try {
         BufferedInputStream in = new BufferedInputStream(context.getAssets().open(assetsFileName));
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dstFileName));

         byte[] buf = new byte[BUFFER_SIZE];
         int len;

         while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
         }

         in.close();
         out.flush();
         out.close();
      } catch (IOException e) {
         Log.e("Can't copy file from assets " + assetsFileName + " to " + dstFileName, e);
         return false;
      }
      Log.v("Success copy file " + assetsFileName + " to " + dstFileName);
      return true;
   }

   /**
    * Copy file from assets source to destination
    * 
    * @param context
    * @param assetsFileName
    * @param dstFile
    * @return true if success
    */
   public static boolean copyAsset(Context context, String assetsFileName, File dstFile) {
      if (assetsFileName == null || assetsFileName.length() == 0) {
         Log.e("Assets file name is empty.");
         return false;
      }
      if (dstFile == null) {
         Log.e("Destination file is null.");
         return false;
      }
      return copyAsset(context, assetsFileName, dstFile.getAbsolutePath());
   }

   /**
    * Delete file
    * 
    * @param fileName
    * @return true if success
    */
   public static boolean delete(String fileName) {
      File file = new File(fileName);
      boolean result = file.delete();
      if (result) {
         Log.v("File success deleted " + fileName);
      } else {
         Log.w("Fail to delete file " + fileName);
      }
      return result;
   }

}
