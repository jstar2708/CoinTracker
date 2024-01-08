package com.example.cointracker.domain.model.states

import com.example.cointracker.domain.model.CoinExchangeRates

open class BaseState{
    var isLoading: Boolean = false
    var error: String = ""
}