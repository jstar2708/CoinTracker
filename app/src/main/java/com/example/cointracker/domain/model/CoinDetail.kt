package com.example.cointracker.domain.model

import com.google.gson.annotations.SerializedName

data class CoinDetail(
    @SerializedName("icon_url")
    val iconUrl: String,
    val name: String,
    val symbol: String
)