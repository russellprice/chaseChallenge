package com.example.chasechallenge

import app.cash.turbine.test
import com.example.chasechallenge.db.CityGeoDataDao
import com.example.chasechallenge.main.MainFragmentViewModel
import com.example.chasechallenge.network.NetworkResult
import com.example.chasechallenge.network.repositories.GeoRepository
import com.example.chasechallenge.network.repositories.WeatherRepository
import com.example.chasechallenge.network.response.CityGeoResponse
import com.example.chasechallenge.network.response.CityWeatherResponse
import io.mockk.coEvery
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class MainActivityViewModelTest {

    // @get:Rule
    // val rule = InstantTaskExecutorRule()

     @get:Rule
     var coroutinesTestRule = CoroutineTestRule()

    val geoRepository = mockk<GeoRepository>()
    val weatherRepository = mockk<WeatherRepository>()
    val cityGeoDataDao = mockk<CityGeoDataDao>()

    @Before
    fun before() {
        coEvery { geoRepository.fetchCity(any(), any()) } returns NetworkResult.Success(
            listOf(VALID_GEO_CITY)
        )
        coEvery { geoRepository.fetchCity(any()) } returns NetworkResult.Success(
            listOf(VALID_GEO_CITY)
        )
        coEvery { cityGeoDataDao.get(any()) } returns null
        justRun { cityGeoDataDao.insert(any()) }
        coEvery { weatherRepository.fetchWeather(any(), any()) } returns NetworkResult.Success(
            VALID_WEATHER
        )
    }

    @Test
    fun `valid viewModel init`() = runTest {
        val viewModel = MainFragmentViewModel(geoRepository, weatherRepository, cityGeoDataDao)
        assert(viewModel.cities.value.isEmpty())
        viewModel.fetchCity(1.0, 1.0)
        Assert.assertEquals(VALID_GEO_CITY, viewModel.cities.value.first())
    }


    @Test
    fun `valid fetch weather`() = runTest {
        val viewModel = MainFragmentViewModel(geoRepository, weatherRepository, cityGeoDataDao)
        viewModel.cities.test {
            viewModel.fetchCity(1.0, 1.0)
            Assert.assertEquals(VALID_GEO_CITY, expectMostRecentItem().first().first)
            cancelAndIgnoreRemainingEvents()
        }
    }


    companion object {
        val VALID_GEO_CITY = CityGeoResponse(1, "US", 37.38, -122.08, "Mountain View", "California")
        val VALID_WEATHER = CityWeatherResponse(
            "base", CityWeatherResponse.Clouds(1), 1,
            CityWeatherResponse.Coord(37.38, -122.08), 1, 1,
            CityWeatherResponse.Main(1.0, 1, 1, 1, 1, 1.0, 2.0, 1.0),
            "name", CityWeatherResponse.Sys("US", 1, 1, 1, 1),
            1, 1, weather = listOf(), CityWeatherResponse.Wind(1, 1.0, 1.0)
        )
    }
}
