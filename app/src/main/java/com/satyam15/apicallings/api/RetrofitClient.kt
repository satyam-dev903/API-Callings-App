package com.satyam15.apicallings.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AuthInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1️⃣ Original request (before modification)
        val originRequest = chain.request()
        // 2️⃣ Modify the request
        val newRequest=originRequest.newBuilder()
            .addHeader(
                "Authorization",
                "Bearer demo_token_123"
            ).build()
        // 3️⃣ Continue request to server
        return chain.proceed(newRequest)
    }
}



object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


}
/**
.client(okHttpClient) → optional. Attaches your custom HTTP client (with headers, logging, etc.).



 */