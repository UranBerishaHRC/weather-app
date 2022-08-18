package com.uranihrc.weatherapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.uranihrc.weatherapp.data.remote.WeatherApi
import com.uranihrc.weatherapp.data.remote.malformedWeatherDtoResponse
import com.uranihrc.weatherapp.data.remote.validWeatherDtoResponse
import com.uranihrc.weatherapp.domain.util.Resource
import com.uranihrc.weatherapp.domain.weather.WeatherInfo
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert

import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class WeatherRepositoryImplTest{

    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okkHttpClient: OkHttpClient
    private lateinit var api: WeatherApi

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        okkHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1,TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(WeatherApi::class.java)

        repository = WeatherRepositoryImpl(api = api)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `Get weather, valid response, returns results`()= runBlocking{

        Assert.assertEquals(4, 2 + 2)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validWeatherDtoResponse)
        )

       val result = repository.getWeatherData(33.0,40.0)

        assertThat(result.data).isNotNull()
    }

    @Test
    fun `Get weather, invalid response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(malformedWeatherDtoResponse)
        )
        val result = repository.getWeatherData(33.0,40.0)
        assertThat(result.message).isNotNull()
    }

}