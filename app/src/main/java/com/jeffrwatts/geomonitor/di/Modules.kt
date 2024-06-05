package com.jeffrwatts.geomonitor.di

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.jeffrwatts.geomonitor.GeoServiceDatabase
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeEventDao
import com.jeffrwatts.geomonitor.data.earthquakeevent.EarthquakeRepository
import com.jeffrwatts.geomonitor.network.USGSEarthquakeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideUSGSEarthquakeApi(): USGSEarthquakeApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(USGSEarthquakeApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideEarthQuakeEventDao(@ApplicationContext context: Context): EarthquakeEventDao {
        return GeoServiceDatabase.getDatabase(context).earthQuakeEventDao()
    }
}


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideEarthQuakeRepository(
        earthquakeEventDao: EarthquakeEventDao,
        usgsEarthquakeApi: USGSEarthquakeApi
    ): EarthquakeRepository {
        return EarthquakeRepository(earthquakeEventDao, usgsEarthquakeApi)
    }
}