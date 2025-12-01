package com.example.levelupapp.data.remote

import android.util.Log
import com.example.levelupapp.data.remote.SupabaseConfig.API_KEY
import com.example.levelupapp.data.remote.SupabaseConfig.STORAGE_BUCKET
import com.example.levelupapp.data.remote.SupabaseConfig.STORAGE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object SupabaseStorageClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }
        .build()

    /**
     * Sube una imagen a Supabase Storage y devuelve la URL pública, o null si falla.
     */
    suspend fun uploadImage(
        bytes: ByteArray,
        mimeType: String,
        fileName: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val body = bytes.toRequestBody(mimeType.toMediaType())

            val url = "$STORAGE_URL/object/$STORAGE_BUCKET/$fileName"
            Log.d("SupabaseStorage", "Uploading to $url")

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("SupabaseStorage", "Upload response: ${response.code} $responseBody")

                if (!response.isSuccessful) {
                    Log.e("SupabaseStorage", "Upload failed: ${response.code}")
                    return@withContext null
                }
            }

            val publicUrl = "$STORAGE_URL/object/public/$STORAGE_BUCKET/$fileName"
            Log.d("SupabaseStorage", "Public URL: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e("SupabaseStorage", "Error uploading image", e)
            null
        }
    }
}
