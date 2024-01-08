package com.example.cointracker.domain.model

import com.example.cointracker.domain.model.states.BaseState

data class CoinExchangeRates(
    val target: String,
    val rates: HashMap<String, Double>,
    val time: Long
): BaseState()