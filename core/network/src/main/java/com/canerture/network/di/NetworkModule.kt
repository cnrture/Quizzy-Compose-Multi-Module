package com.canerture.network.di

import android.content.Context
import com.canerture.core.network.BuildConfig
import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.TokenAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    private const val TIMEOUT = 60L

    @Provides
    @Singleton
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor = ChuckerInterceptor(context)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        userRepository: dagger.Lazy<DataStoreHelper>,
        logoutDatasource: dagger.Lazy<LogoutDataSource>,
    ): TokenAuthenticator = TokenAuthenticator(userRepository, logoutDatasource)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticator: TokenAuthenticator,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        authenticator(authenticator)
        if (BuildConfig.DEBUG) {
            addInterceptor(chuckerInterceptor)
        }
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val mediaType = "application/json; charset=UTF8".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()
    }
}