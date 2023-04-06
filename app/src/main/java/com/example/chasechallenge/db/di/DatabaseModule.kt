package com.example.chasechallenge.db.di

import android.content.Context
import androidx.room.Room
import com.example.chasechallenge.db.AppDatabase
import com.example.chasechallenge.db.CityGeoDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(context,
    AppDatabase::class.java, "city-database").build()

    @Provides
    fun provideCityDao(database: AppDatabase): CityGeoDataDao = database.cityDataDao()
}