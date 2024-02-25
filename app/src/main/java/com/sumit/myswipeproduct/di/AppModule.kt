package com.sumit.myswipeproduct.di

import android.content.Context
import androidx.room.Room
import com.sumit.myswipeproduct.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)

        return builder.build()
    }



//   dependency for room database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context,
            ProductDatabase::class.java, "my-swipe-product-room-db")
            .build()
    }

    companion object {
        const val apiBaseUrl = "https://app.getswipe.in"
    }


}