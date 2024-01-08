package com.example.cointracker.domain.usecase.get_coin_exchange

import com.example.cointracker.common.Resource
import com.example.cointracker.domain.model.CoinExchangeRates
import com.example.cointracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetCoinExchangeUseCase @Inject constructor(
    private val repository: CoinRepository
){
    operator fun invoke(target: String) : Flow<Resource<CoinExchangeRates>> = flow {
        try {
            emit(Resource.Loading())
            val coinExchangeRates = repository.getCoinsExchangePrice(target)
            emit(Resource.Success(coinExchangeRates))
        }
        catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage?.toString() ?: "An unexpected error occurred"))
        }
    }
}