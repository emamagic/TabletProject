package com.example.brightcity.di

import android.content.Context
import com.example.brightcity.util.Constance.SHARED_PREFERENCES_NAME
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context) =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    @Singleton
    @Provides
    fun providePicasso(@ApplicationContext context: Context, client: OkHttpClient): Picasso {
        return Picasso.Builder(context)
            .downloader(OkHttp3Downloader(client))
            .build()
    }

}