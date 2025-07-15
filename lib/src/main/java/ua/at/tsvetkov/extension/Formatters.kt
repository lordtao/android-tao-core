package ua.at.tsvetkov.extension

import java.time.format.DateTimeFormatter

/**
 * Created by Alexandr Tsvetkov on 20.05.2025.
 */

val formatterHHmm = DateTimeFormatter.ofPattern("HH : mm")
val formatterddMMyyyy = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val formatterLong: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")