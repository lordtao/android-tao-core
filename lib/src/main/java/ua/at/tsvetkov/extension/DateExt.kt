package ua.at.tsvetkov.extension

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * Created by Alexandr Tsvetkov on 19.02.2025.
 */

fun Date.toLocalDateTime(): LocalDateTime {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

