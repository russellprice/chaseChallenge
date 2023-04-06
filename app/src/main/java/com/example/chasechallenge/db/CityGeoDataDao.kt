package com.example.chasechallenge.db

import androidx.room.*
import com.example.chasechallenge.network.response.CityGeoResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface CityGeoDataDao {

    @Query("SELECT * FROM CityGeoResponse")
    fun getAll(): List<CityGeoResponse>

    @Query("SELECT * FROM CityGeoResponse WHERE uid = :uid")
    fun get(uid: Int = 1): CityGeoResponse?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cityGeoResponse: CityGeoResponse)

    @Delete
    fun delete(cityGeoResponse: CityGeoResponse)

    @Query("DELETE FROM CityGeoResponse")
    fun deleteAll()

}