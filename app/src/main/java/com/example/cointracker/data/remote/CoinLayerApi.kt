package com.example.cointracker.data.remote

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinLayerApi {

    @GET("live?access_key=d3574045f389a36d1058c68ff495bea0")
    suspend fun getCoinsExchangePrice(@Query("target") target: String = "INR"): JsonElement

    @GET("list?access_key=d3574045f389a36d1058c68ff495bea0")
    suspend fun getCoinDetail() : JsonElement
}