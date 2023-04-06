package com.example.chasechallenge.network.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityGeoResponse(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lon: Double,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "state") val state: String
)