package com.etrack.myapplication.controller

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Hussain on 19-02-2018.
 */


class ApiClient {
    private var retrofit: Retrofit? = null
    private val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build()

    fun BaseUrl(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(Apis.baseUrl).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }

 /*   fun BaseStringUrl(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl("http://next.json-generator.com/api/")
                    .build()
        }

        return retrofit

    }*/

}