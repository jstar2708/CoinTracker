package com.example.cointracker.presentation.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointracker.common.Resource
import com.example.cointracker.domain.model.Coin
import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates
import com.example.cointracker.domain.model.states.BaseState
import com.example.cointracker.domain.model.states.CoinExchangeState
import com.example.cointracker.domain.usecase.get_coin_detail_list.GetCoinDetailListUseCase
import com.example.cointracker.domain.usecase.get_coin_exchange.GetCoinExchangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getCoinExchangeUseCase: GetCoinExchangeUseCase,
    private val getCoinDetailsListUseCase: GetCoinDetailListUseCase
): ViewModel(){

    private val coinExchangeRates = MutableLiveData<CoinExchangeRates>()
    private val coinDetailsList = MutableLiveData<CoinDetailsList>()
    private val _state = MutableLiveData<BaseState>()
    val state: LiveData<BaseState> = _state

    private val _viewEvents = MutableLiveData<ViewEvents>()
    val viewEvents = _viewEvents
    var currentTabText = "INR"

    init {
        getCoinExchangeData()
        getCoinDetailData()
        timer()
    }

    fun getFiats() : List<String> {
        return coinDetailsList.value?.fiat?.keys?.toList() ?: ArrayList()
    }

    fun getCoinData(): ArrayList<Coin> {
        val list: ArrayList<Coin> = ArrayList()
        coinDetailsList.value?.crypto?.forEach { it ->
            val rate: Double = coinExchangeRates.value?.rates?.get(it.symbol) ?: 0.toDouble()

            list.add(
                Coin(
                    name = it.name,
                    icon = it.iconUrl,
                    symbol = it.symbol,
                    rate = (Math.round(rate * 1000000.toDouble()) / 1000000.toDouble()).toString()
                )
            )
        }
        return list
    }

    private fun timer() {
        viewModelScope.launch {
            object : CountDownTimer(180000, 1000){
                override fun onTick(millisUntilFinished: Long) {
                    _viewEvents.value = ViewEvents.UpdateTimer(millisUntilFinished)
                }

                override fun onFinish() {
                    getCoinExchangeData(currentTabText)
                    getCoinDetailData()
                    _viewEvents.value = ViewEvents.SetLastUpdateTime()
                    timer()
                }

            }.start()
        }
    }

    fun getCoinDetailData() {
        getCoinDetailsListUseCase().onEach {
            when (it) {
                is Resource.Loading -> {
                    _state.value = BaseState().apply {
                        isLoading = true
                    }
                }
                is Resource.Error -> {
                    _state.value = BaseState().apply {
                        error = it.message ?: "An unexpected error occurred"
                        isLoading = false
                    }
                }
                is Resource.Success -> {
                    coinDetailsList.value = it.data
                    if (coinExchangeRates.value != null) {
                        _state.value = BaseState().apply {
                            isLoading = false
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCoinExchangeData(target: String = "INR") {
        getCoinExchangeUseCase(target).onEach {
            when (it) {
                is Resource.Loading -> {
                    _state.value = BaseState().apply {
                        isLoading = true
                    }
                }
                is Resource.Error -> {
                    _state.value = BaseState().apply {
                        error = it.message ?: "An unexpected error occurred"
                        isLoading = false
                    }
                }
                is Resource.Success -> {
                    coinExchangeRates.value = it.data
                    if (coinDetailsList.value != null) {
                        _state.value = BaseState().apply {
                            isLoading = false
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

open class ViewEvents {
    class UpdateTimer(val time: Long): ViewEvents()
    class SetLastUpdateTime(): ViewEvents()
}