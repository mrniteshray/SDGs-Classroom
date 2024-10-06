package com.nitesh.app.sdgsclassroom.aichatbot

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/completions")
    fun askQuestion(
        @Body requestBody: RequestBody,
        @Header("Authorization") authHeader: String
    ): Call<ResponseBody>
}
