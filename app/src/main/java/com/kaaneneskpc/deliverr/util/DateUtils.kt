package com.kaaneneskpc.deliverr.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    /**
     * Converts a date string in ISO 8601 format to a user-friendly format
     *
     * @param isoDateString Date string in ISO 8601 format (ex: “2023-05-15T14:30:45.123Z”)
     * @param outputPattern Date format to use for output (default: “MMM dd, HH:mm”)
     * @return Formatted date string or original string in case of error
     */
    fun formatISODate(
        isoDateString: String, 
        outputPattern: String = "MMM dd, HH:mm"
    ): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(isoDateString)
            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            isoDateString
        }
    }

    /**
     * Converts a date string in ISO 8601 format to a Date object
     *
     * @param isoDateString Date string in ISO 8601 format
     * @return Date object or current time in case of error
     */
    fun parseISODate(isoDateString: String): Date {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.parse(isoDateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    /**
     * Converts the Date object to string in the specified format
     *
     * @param date Date object to convert
     * @param pattern Output format
     * @return Formatted date string
     */
    fun formatDate(date: Date, pattern: String = "MMM dd, HH:mm"): String {
        val outputFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return outputFormat.format(date)
    }
} 