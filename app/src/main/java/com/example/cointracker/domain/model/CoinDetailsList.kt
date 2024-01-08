package com.example.cointracker.domain.model

data class CoinDetailsList(
    val fiat: HashMap<String, String>,
    val crypto: List<CoinDetail>
)