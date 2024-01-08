package com.example.cointracker.domain.repository

import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinRepository {

    suspend fun getCoinsExchangePrice(target: String = "INR"): CoinExchangeRates


    suspend fun getCoinDetail() : CoinDetailsList
}