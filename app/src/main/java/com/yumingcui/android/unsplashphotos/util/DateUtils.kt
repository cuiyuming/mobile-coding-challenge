package com.yumingcui.android.unsplashphotos.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val SERVER_TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss-ss:ss"
    val GMT_TIME_ZONE = "GMT"
    val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

    fun getDateFromServerTimeStamp(timeStamp: String): Date? {
        val simpleFormat = SimpleDateFormat(SERVER_TIME_STAMP_FORMAT)
        simpleFormat.timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE)

        try {
            return simpleFormat.parse(timeStamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun formatDate(date: Date): String {
        val dateTimeFormatter = DateTimeFormat.forPattern(DATE_PATTERN)
        val dateTime = DateTime(date)
        return dateTimeFormatter.print(dateTime)
    }
}
