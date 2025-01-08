package com.canerture.network.di

import android.content.Context
import com.canerture.core.network.BuildConfig
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.NetworkManager
import com.canerture.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT = 60L

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context) = NetworkManager(context)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        userRepository: dagger.Lazy<DataStoreHelper>
    ): TokenAuthenticator = TokenAuthenticator(userRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticator: TokenAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        authenticator(authenticator)
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
        val mediaType = MediaType.get("application/json; charset=UTF8")
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()
    }
}