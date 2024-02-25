package com.sumit.myswipeproduct.di

import android.content.Context
import com.sumit.myswipeproduct.data.local.ProductDatabase
import com.sumit.myswipeproduct.data.remote.ApiServices
import com.sumit.myswipeproduct.data.repository.HomeScreenRepositoryImpl
import com.sumit.myswipeproduct.domain.repository.HomeScreenRepository
import com.sumit.myswipeproduct.responsehandler.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideNetworkOperations(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ApiServices {
        return retrofitBuilder.client(okHttpClient).build().create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeScreenRepository(
        apiServices: ApiServices,
        responseHandler: ResponseHandler,
        productDatabase: ProductDatabase,
        @ApplicationContext context: Context
    ): HomeScreenRepository {
        return HomeScreenRepositoryImpl(
            apiServices,
            responseHandler,
            productDatabase.productItemDao(),
            context = context
        )
    }

}