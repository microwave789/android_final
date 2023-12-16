package com.example.currencyconverter.repository

import com.example.currencyconverter.api.NetworkResult
import com.example.currencyconverter.model.GetCurrenciesResponse

interface BaseRepository {

    suspend fun getCurrencies(
        currencyFrom: String = Util.EUR
    ): NetworkResult<GetCurrenciesResponse>
}