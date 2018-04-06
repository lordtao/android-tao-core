/**
 *
 */
package ua.at.tsvetkov.sha;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ua.at.tsvetkov.util.Log;

/**
 * @author Alexandr Tsvetkov 2015
 */
public final class Md5 {

    private Md5() {

    }

    /**
     * Computes and returns the final hash value for this InputStream data
     *
     * @param in InputStream
     * @return null if has error or hash value
     */
    public static byte[] fromInputStream(InputStream in) {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            return digester.digest();
        } catch (Exception e) {
            Log.e(e);
        }
        return null;
    }

    /**
     * Computes and returns the final hash value for file data
     *
     * @param fileName path to file
     * @return null if has error or hash value
     */
    public static byte[] fromFile(String fileName) {
        try {
            InputStream in = new FileInputStream(fileName);
            return fromInputStream(in);
        } catch (FileNotFoundException e) {
            Log.e(e);
        }
        return null;
    }

    /**
     * Return hash sum String for given data
     *
     * @param data byte array
     * @return hash sum String
     */
    public static String getHashString(byte[] data) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        return hashToString(hash);
    }

    /**
     * Return hash sum String for given file data
     *
     * @param fileName path to file
     * @return hash sum String
     */
    public static String getHashString(String fileName) {
        byte[] hash = fromFile(fileName);
        return hashToString(hash);
    }

    /**
     * Return hash sum String for given data InputStream
     *
     * @param in InputStream
     * @return hash sum String
     */
    public static String getHashString(InputStream in) {
        byte[] hash = fromInputStream(in);
        return hashToString(hash);
    }

    /**
     * Return String representation of hash
     *
     * @param hash byte array of hash sum
     * @return hash sum String
     */
    private static String hashToString(byte[] hash) {
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
