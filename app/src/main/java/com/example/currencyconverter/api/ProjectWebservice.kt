package com.example.currencyconverter.api

import com.example.currencyconverter.model.GetCurrenciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectWebservice {

    @GET("fawazahmed0/currency-api/1/latest/currencies/{currencyFrom}.json")
    suspend fun getCurrencies(
        @Path("currencyFrom") currencyFrom: String
    ): Response<GetCurrenciesResponse>
}