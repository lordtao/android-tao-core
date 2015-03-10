/*******************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 *     TAO Core
 *
 * License agreement:
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 *    caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 *    permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 *    this license agreement will remain unchanged.
 ******************************************************************************/
package ua.at.tsvetkov.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ua.at.tsvetkov.util.Log;

/**
 * @author A.Tsvetkov 2014 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class FileInet {

	public static final int	TIMEOUT	= 10000;
	public static final int	BUFFER	= 8192;

	/**
	 * Returns the content length in bytes specified by the response header field content-length or -1 if this field is not set.
	 * 
	 * @param url
	 * @return the value of the response header field content-length
	 * @throws IOException
	 */
	static public int getFileLength(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT);
			conn.connect();
			return conn.getContentLength();
		} catch (Exception e) {
			Log.e(e);
		}
		return 0;
	}

	   /**
    * Async downloads a remote file and stores it locally.
    * 
    * @param url Remote URL of the file to download
    * @param pathAndFileName Local path with file name where to store the file
    * @param rewrite If TRUE and file exist - rewrite the file. If FALSE and file exist and his length > 0 - not download and rewrite the
    *           file.
    * @param complete listener
    * @throws IOException
    * @throws MalformedURLException
    * @throws Exception Read/write exception
    */
   static public void download(final String url, final String pathAndFileName, final boolean rewrite, final CompleteListener listener) {
      new Thread(new Runnable() {

         @Override
         public void run() {
            boolean result = download(url, pathAndFileName, rewrite);
            listener.complete(url, result);
         }
      }, "Download thread: " + url).start();
	}

	/**
	 * Downloads a remote file and stores it locally.
	 * 
	 * @param url Remote URL of the file to download. If the string contains spaces, they are replaced by "%20".
	 * @param pathAndFileName Local path with file name where to store the file
	 * @param rewrite If TRUE and file exist - rewrite the file. If FALSE and file exist and his length > 0 - not download and rewrite the file.
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws Exception Read/write exception
	 */
	static public boolean download(String url, String pathAndFileName, boolean rewrite) {
		if (url == null || url.trim().length() == 0) {
			Log.w("Wrong url: " + url);
			return false;
		}
		url = url.replace(" ", "%20");
		File f = new File(pathAndFileName);
		if (!rewrite && f.exists() && f.length() > 0) {
			Log.w("File exist: " + pathAndFileName);
			return true;
		}
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT);
			conn.connect();
			int fileLength = conn.getContentLength();
			if (fileLength == 0) {
				Log.v("File length = 0 > " + url);
			}
			InputStream input = conn.getInputStream();
			FileOutputStream fOut = new FileOutputStream(pathAndFileName);
			byte[] buffer = new byte[BUFFER];
			int bytesRead = -1;
			while ((bytesRead = input.read(buffer)) != -1) {
				fOut.write(buffer, 0, bytesRead);
			}
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			Log.w("Download error " + url, e);
			if (f.exists() && f.length() > 0) {
				Log.w("But file exist: " + pathAndFileName);
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Downloads a remote file and stores it locally. If file exist - rewrite the file.
	 * 
	 * @param url Remote URL of the file to download
	 * @param pathAndFileName Local path with file name where to store the file
	 * @return
	 */
	static public boolean download(String url, String pathAndFileName) {
		return download(url, pathAndFileName, true);
	}

	public abstract class CompleteListener {

	   public abstract void complete(String fileName, boolean result);
	}
}
