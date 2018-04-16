/**
 *
 */
package ua.at.tsvetkov.sha

import ua.at.tsvetkov.util.Log
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * @author Alexandr Tsvetkov 2015
 */
object Md5 {

    /**
     * Calculate and returns the final hash value for this InputStream data
     *
     * @param inp InputStream
     * @return null if has error or hash value
     */
    fun fromInputStream(inp: InputStream): ByteArray? {
        val digester: MessageDigest
        try {
            digester = MessageDigest.getInstance("MD5")
            val bytes = ByteArray(8192)
            var byteCount: Int
            while (true) {
                byteCount = inp.read(bytes)
                if (byteCount < 0) break
                digester.update(bytes, 0, byteCount)
            }
            return digester.digest()
        } catch (e: Exception) {
            Log.e(e)
        }

        return null
    }

    /**
     * Calculate and returns the final hash value for file data
     *
     * @param fileName path to file
     * @return null if has error or hash value
     */
    fun fromFile(fileName: String): ByteArray? {
        try {
            val inp = FileInputStream(fileName)
            return fromInputStream(inp)
        } catch (e: FileNotFoundException) {
            Log.e(e)
        }

        return null
    }

    /**
     * Calculate and returns the final hash value for file data
     *
     * @param file a file
     * @return null if has error or hash value
     */
    fun fromFile(file: File): ByteArray? {
        try {
            val inp = FileInputStream(file)
            return fromInputStream(inp)
        } catch (e: FileNotFoundException) {
            Log.e(e)
        }

        return null
    }

    /**
     * Calculate and returns the final hash value for file data
     *
     * @param fd a FileDescriptor
     * @return null if has error or hash value
     */
    fun fromFileDescriptor(fd: FileDescriptor): ByteArray? {
        val inp = FileInputStream(fd)
        return fromInputStream(inp)

    }

    /**
     * Return hash sum String for given data
     *
     * @param data byte array
     * @return hash sum String
     */
    fun getHashString(data: ByteArray): String {
        val hash: ByteArray
        try {
            hash = MessageDigest.getInstance("MD5").digest(data)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        }

        return hashToString(hash)
    }

    /**
     * Return hash sum String for given file data
     *
     * @param fileName path to file
     * @return hash sum String
     */
    fun getHashString(fileName: String): String {
        val hash = fromFile(fileName)
        return hashToString(hash!!)
    }

    /**
     * Return hash sum String for given data InputStream
     *
     * @param inp InputStream
     * @return hash sum String
     */
    fun getHashString(inp: InputStream): String {
        val hash = fromInputStream(inp)
        return hashToString(hash!!)
    }

    /**
     * Return String representation of hash
     *
     * @param hash byte array of hash sum
     * @return hash sum String
     */
    private fun hashToString(hash: ByteArray): String {
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if ((b and 0xff.toByte()) < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString((b and 0xff.toByte()).toInt()))
        }
        return hex.toString()
    }

}
