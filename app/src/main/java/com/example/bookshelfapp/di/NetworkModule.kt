package com.example.bookshelfapp.di

import com.example.bookshelfapp.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.bookshelfapp.data.features.books.repository.remote.api.BooksApiService
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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val clientBuilder = OkHttpClient
            .Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor)
        }
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://www.jsonkeeper.com/")
            .client(okHttpClient)
            .build()

    @Provides
    fun provideBooksApiService(retrofit: Retrofit): BooksApiService =
        retrofit.create(BooksApiService::class.java)
}
