/**
 *
 */
package ua.at.tsvetkov.security

import ua.at.tsvetkov.util.logger.Log
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * @author Alexandr Tsvetkov 2015
 */
class HashUtils {

    companion object {

        private const val HEX_CHARS = "0123456789ABCDEF"
        private const val MD_5 = "MD5"

        /**
         * Calculate and returns the final hash value for an InputStream data
         *
         * @param inp InputStream
         * @param algorithm  MD5 by default, or SHA-1, SHA-256, SHA-384, SHA-512
         * @return the array of bytes for the resulting hash value.
         */
        @JvmStatic
        fun getHash(inp: InputStream, algorithm: String = MD_5): ByteArray {
            try {
                val digester = MessageDigest.getInstance(algorithm)
                val bytes = ByteArray(8192)
                var byteCount: Int
                while (true) {
                    byteCount = inp.read(bytes)
                    if (byteCount < 0) break
                    digester.update(bytes, 0, byteCount)
                }
                return digester.digest()
            } catch (e: NoSuchAlgorithmException) {
                Log.e(RuntimeException("Huh, $algorithm should be supported?", e))
            } catch (e: Exception) {
                Log.e(e)
            }
            return ByteArray(0)
        }

        /**
         * Calculate and returns the final hash value for a file data
         *
         * @param fileName path to file
         * @return the array of bytes for the resulting hash value.
         */
        @JvmStatic
        fun getHash(fileName: String, algorithm: String = MD_5): ByteArray {
            try {
                val inp = FileInputStream(fileName)
                return getHash(inp, algorithm)
            } catch (e: FileNotFoundException) {
                Log.e(e)
            } catch (e: Exception) {
                Log.e(e)
            }

            return ByteArray(0)
        }

        /**
         * Calculate and returns the final hash value for a file data
         *
         * @param file a file
         * @return the array of bytes for the resulting hash value.
         */
        @JvmStatic
        fun getHash(file: File, algorithm: String = MD_5): ByteArray {
            try {
                val inp = FileInputStream(file)
                return getHash(inp, algorithm)
            } catch (e: FileNotFoundException) {
                Log.e(e)
            } catch (e: Exception) {
                Log.e(e)
            }

            return ByteArray(0)
        }

        /**
         * Calculate and returns the final hash value for a file data
         *
         * @param fd a FileDescriptor
         * @param algorithm  MD5 by default, or SHA-1, SHA-256, SHA-384, SHA-512
         * @return the array of bytes for the resulting hash value.
         */
        @JvmStatic
        fun getHash(fd: FileDescriptor, algorithm: String = MD_5): ByteArray {
            val inp = FileInputStream(fd)
            return getHash(inp, algorithm)

        }

        /**
         * Return hash String for a data
         *
         * @param data byte array
         * @param algorithm  MD5 by default, or SHA-1, SHA-256, SHA-384, SHA-512
         * @return the array of bytes for the resulting hash value.
         */
        @JvmStatic
        fun getHash(data: ByteArray, algorithm: String = MD_5): ByteArray {
            val inp = data.inputStream()
            return getHash(inp, algorithm)
        }


        /**
         * Return hash String for a data
         *
         * @param data byte array
         * @param algorithm  MD5 by default, or SHA-1, SHA-256, SHA-384, SHA-512
         * @return hash String
         */
        @JvmStatic
        fun getHashString(data: ByteArray, algorithm: String = MD_5): String {
            val digest = getHash(data, algorithm)
            return hexString(digest)
        }

        /**
         * Return hash String for a data from file
         *
         * @param fileName path to file
         * @return hash String
         */
        @JvmStatic
        fun getHashString(fileName: String, algorithm: String = MD_5): String {
            val digest = getHash(fileName, algorithm)
            return hexString(digest)
        }

        /**
         * Return hash String for a data from InputStream
         *
         * @param inp InputStream
         * @return hash String
         */
        @JvmStatic
        fun getHashString(inp: InputStream, algorithm: String = MD_5): String {
            val digest = getHash(inp, algorithm)
            return hexString(digest)
        }

        /**
         * Return hash String for a data from InputStream
         *
         * @param inp InputStream
         * @return hash String
         */
        @JvmStatic
        fun getHashString(file: File, algorithm: String = MD_5): String {
            val digest = getHash(file, algorithm)
            return hexString(digest)
        }

        /**
         * Return String representation of hash
         *
         * @param bytes byte array
         * @return hash String
         */
        @JvmStatic
        private fun hexString(bytes: ByteArray): String {
            val sb = StringBuilder(bytes.size * 2)
            bytes.forEach {
                val i = it.toInt()
                sb.append(HEX_CHARS[i shr 4 and 0x0f])
                sb.append(HEX_CHARS[i and 0x0f])
            }
            return sb.toString()
        }
    }

}
