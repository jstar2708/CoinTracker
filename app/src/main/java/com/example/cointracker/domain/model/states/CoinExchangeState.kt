package com.example.cointracker.domain.model.states

import com.example.cointracker.domain.model.CoinExchangeRates

data class CoinExchangeState(
    var coinExchange: CoinExchangeRates? = null,
): BaseState() {
}