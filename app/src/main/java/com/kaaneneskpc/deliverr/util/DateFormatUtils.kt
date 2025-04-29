package com.kaaneneskpc.deliverr.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatUtils {
    fun formatDate(dateString: String): String {
        try {
            val regex = """(\d{4}-\d{2}-\d{2})T?(\d{2}:\d{2}).*""".toRegex()
            val matchResult = regex.find(dateString)

            return if (matchResult != null && matchResult.groupValues.size >= 3) {
                val date = matchResult.groupValues[1]
                val time = matchResult.groupValues[2]
                "$date $time"
            } else {

                try {

                    val possibleFormats = listOf(
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()),
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()),
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    )

                    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                    for (format in possibleFormats) {
                        try {
                            val date = format.parse(dateString)
                            if (date != null) {
                                return outputFormat.format(date)
                            }
                        } catch (_: Exception) {
                        }
                    }
                    dateString
                } catch (e: Exception) {
                    dateString
                }
            }
        } catch (e: Exception) {
            return dateString
        }
    }
}