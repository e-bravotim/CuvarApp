package com.cuvar.bezbednost

package com.cuvar.bezbednost

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TelegramSender(
    private val botToken: String = "OVDE_STAVI_BOT_TOKEN",
    private val chatId: String = "OVDE_STAVI_CHAT_ID"
) {

    private val client = OkHttpClient()

    fun sendPhotoAndLocation(photoFile: File, latitude: Double, longitude: Double) {
        val googleMapsLink = "https://maps.google.com/?q=$latitude,$longitude"
        val caption = "⚠️ ALARM! Lokacija: $googleMapsLink"

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", chatId)
            .addFormDataPart("caption", caption)
            .addFormDataPart(
                "photo",
                photoFile.name,
                photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url("https://api.telegram.org/bot$botToken/sendPhoto")
            .post(requestBody)
            .build()

        Thread {
            try {
                client.newCall(request).execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
