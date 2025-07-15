package ua.at.tsvetkov.extension

import ua.at.tsvetkov.util.logger.Log
import java.time.LocalDateTime
import java.util.Date

/**
 * Created by Alexandr Tsvetkov on 19.02.2025.
 */

/**
 * Return LocalDateTime or current date and time if string is null or corrupted
 */
fun String.toLocalDateTime(): LocalDateTime {
    return try {
        LocalDateTime.parse(this, formatterLong)
    } catch (e: Exception) {
        Log.e(e)
        Date().toLocalDateTime()
    }
}