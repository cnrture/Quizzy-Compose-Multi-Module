package com.canerture.network.di

import com.canerture.core.network.BuildConfig
import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideTokenAuthenticator(
        userRepository: dagger.Lazy<DataStoreHelper>,
        logoutDatasource: dagger.Lazy<LogoutDataSource>,
    ): TokenAuthenticator = TokenAuthenticator(userRepository, logoutDatasource)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticator: TokenAuthenticator,
        dataStoreHelper: DataStoreHelper,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        authenticator(authenticator)
        if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
        }
        addInterceptor {
            val token = runBlocking {
                dataStoreHelper.getToken().firstOrNull().orEmpty()
            }
            val request = it.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("X-API-Key", BuildConfig.API_KEY)
                .apply {
                    if (token.isNotEmpty()) {
                        header("Authorization", "Bearer $token")
                    }
                }
                .build()
            it.proceed(request)
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