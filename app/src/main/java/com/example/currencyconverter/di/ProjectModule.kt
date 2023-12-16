package com.example.currencyconverter.di

import androidx.viewbinding.BuildConfig
import com.example.currencyconverter.api.ProjectWebservice
import com.example.currencyconverter.repository.BaseRepository
import com.example.currencyconverter.repository.BaseRepositoryImpl
import com.example.currencyconverter.util.DispatchersProvider
import com.example.currencyconverter.util.ResourcesProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProjectModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(com.example.currencyconverter.BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideProjectWebservice(
        retrofit: Retrofit
    ): ProjectWebservice = retrofit.create(ProjectWebservice::class.java)

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor()
            .setLevel(
                when {
                    BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BASIC
                    else -> HttpLoggingInterceptor.Level.NONE
                }
            )

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideBaseRepository(
        projectWebservice: ProjectWebservice,
        resourcesProvider: ResourcesProvider
    ): BaseRepository = BaseRepositoryImpl(
        projectWebservice,
        resourcesProvider
    )

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider =
        object : DispatchersProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val unconfined: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }
}