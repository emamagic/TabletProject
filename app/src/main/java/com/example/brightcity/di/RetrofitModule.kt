package com.example.brightcity.di

import com.example.brightcity.api.MyApi
import com.example.brightcity.api.safe.EMAuthenticator
import com.example.brightcity.api.safe.EMInterceptor
import com.example.brightcity.util.Constance.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    fun provideLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttp(emInterceptor: EMInterceptor ,emAuthenticator: EMAuthenticator ,loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(8000, TimeUnit.SECONDS)
            .writeTimeout(8000, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(emInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(emAuthenticator)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit.Builder): MyApi {
        return retrofit.build().create(MyApi::class.java)
    }


}