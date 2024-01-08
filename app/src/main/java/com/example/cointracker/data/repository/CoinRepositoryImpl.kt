package com.example.cointracker.data.repository

import com.example.cointracker.data.remote.CoinLayerApi
import com.example.cointracker.domain.model.CoinDetail
import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates
import com.example.cointracker.domain.repository.CoinRepository
import com.google.gson.JsonObject
import org.json.JSONObject
import javax.inject.Inject


class CoinRepositoryImpl @Inject constructor(
    private val coinLayerApi: CoinLayerApi
) : CoinRepository {
    override suspend fun getCoinsExchangePrice(target: String): CoinExchangeRates {
        val result: JsonObject = coinLayerApi.getCoinsExchangePrice(target).asJsonObject
        val _target = result.get("target").asString
        val time = result.get("timestamp").asLong
        val rates : HashMap<String, Double> = HashMap()
        result.get("rates").asJsonObject.keySet().forEach {
            rates[it] = result.get("rates").asJsonObject[it].toString().toDouble()
        }
        return CoinExchangeRates(_target, rates, time)
    }

    override suspend fun getCoinDetail(): CoinDetailsList {
        val result: JsonObject = coinLayerApi.getCoinDetail().asJsonObject
        val fiat = HashMap<String, String>()
        result.get("fiat").asJsonObject.keySet().forEach {
            fiat[it] = result.get("fiat").asJsonObject[it].asString
        }
        fiat.remove("INR")

        val crypto = ArrayList<CoinDetail>()
        result.get("crypto").asJsonObject.keySet().forEach {
            val coin = result.get("crypto").asJsonObject.get(it).asJsonObject
            crypto.add(
                CoinDetail(
                    symbol = coin["symbol"].asString,
                    name = coin["name_full"].asString,
                    iconUrl = coin["icon_url"].asString
                )
            )
        }
        return CoinDetailsList(fiat, crypto)
    }
}