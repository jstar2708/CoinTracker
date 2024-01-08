package com.example.cointracker.domain.model.states

import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates

data class CoinDetailState(
    var coinExchange: CoinDetailsList? = null,
): BaseState() {
}