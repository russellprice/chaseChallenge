package com.example.chasechallenge.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chasechallenge.network.response.CityGeoResponse

@Database(entities = [CityGeoResponse::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cityDataDao(): CityGeoDataDao
}