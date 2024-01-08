package com.example.cointracker.domain.usecase.get_coin_detail_list

import com.example.cointracker.common.Resource
import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates
import com.example.cointracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetCoinDetailListUseCase @Inject constructor(
    private val repository: CoinRepository
){
    operator fun invoke() : Flow<Resource<CoinDetailsList>> = flow {
        try {
            emit(Resource.Loading())
            val coinDetailsList = repository.getCoinDetail()
            emit(Resource.Success(coinDetailsList))
        }
        catch (e: Exception) {
//            emit(Resource.Error(e.localizedMessage?.toString() ?: "An unexpected error occurred"))
        }
    }
}