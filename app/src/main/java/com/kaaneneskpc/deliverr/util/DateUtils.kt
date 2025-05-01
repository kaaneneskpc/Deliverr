package com.kaaneneskpc.deliverr.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    
    /**
     * ISO 8601 formatındaki bir tarih stringini kullanıcı dostu formata dönüştürür
     * 
     * @param isoDateString ISO 8601 formatında tarih string'i (örn: "2023-05-15T14:30:45.123Z")
     * @param outputPattern Çıktı için kullanılacak tarih formatı (varsayılan: "MMM dd, HH:mm")
     * @return Formatlanmış tarih string'i veya hata durumunda orijinal string
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
            // Hata durumunda orijinal string'i dön
            isoDateString
        }
    }
    
    /**
     * ISO 8601 formatındaki bir tarih stringini Date nesnesine dönüştürür
     * 
     * @param isoDateString ISO 8601 formatında tarih string'i
     * @return Date nesnesi veya hata durumunda şu anki zaman
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
     * Date nesnesini belirtilen formatta string'e dönüştürür
     * 
     * @param date Dönüştürülecek Date nesnesi
     * @param pattern Çıktı formatı
     * @return Formatlanmış tarih string'i
     */
    fun formatDate(date: Date, pattern: String = "MMM dd, HH:mm"): String {
        val outputFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return outputFormat.format(date)
    }
} 