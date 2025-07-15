package ua.at.tsvetkov.extension

import ua.at.tsvetkov.util.logger.Log
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Alexandr Tsvetkov on 19.02.2025.
 */

private val userTimeZone = ZoneId.systemDefault()

fun LocalDateTime.toStringHHMM(): String = try {
    format(formatterHHmm)
} catch (e: DateTimeException) {
    Log.e("Wrong date format")
    ""
}

fun LocalDateTime.toStringddMMyyyy(): String = try {
    format(formatterddMMyyyy)
} catch (e: DateTimeException) {
    Log.e("Wrong date format")
    ""
}

fun LocalDateTime.isToday(): Boolean {
    val today = LocalDate.now()
    return toLocalDate() == today
}

fun LocalDateTime.isYesterday(): Boolean {
    val yesterday = LocalDate.now(ZoneId.systemDefault()).minusDays(1).atStartOfDay()
    return this.toLocalDate() == yesterday.toLocalDate()
}

fun LocalDateTime.convertUtcToLocal(): LocalDateTime {
    val utcZonedDateTime = atZone(ZoneId.of("UTC"))
    val userZonedDateTime = utcZonedDateTime.withZoneSameInstant(userTimeZone)
    return userZonedDateTime.toLocalDateTime()
}

fun LocalDateTime.toLongDateString(): String {
    return this.format(formatterLong)
}