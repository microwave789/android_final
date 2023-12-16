package com.example.currencyconverter.repository

import com.example.currencyconverter.R
import com.example.currencyconverter.api.NetworkResult
import com.example.currencyconverter.api.ProjectWebservice
import com.example.currencyconverter.model.GetCurrenciesResponse
import com.example.currencyconverter.util.ResourcesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseRepositoryImpl @Inject constructor(
    private val projectWebservice: ProjectWebservice,
    private val resourcesProvider: ResourcesProvider
) : BaseRepository {

    override suspend fun getCurrencies(
        currencyFrom: String
    ): NetworkResult<GetCurrenciesResponse> {
        return try {
            val response = projectWebservice.getCurrencies(currencyFrom)
            when {
                response.isSuccessful && response.body() != null -> {
                    val getCurrenciesResponse = response.body()!!
                    NetworkResult.Success(getCurrenciesResponse)
                }
                else -> NetworkResult.Failure(
                    "${response.code()} ${response.message() ?: resourcesProvider.getString(R.string.error_occurred)}"
                )
            }
        } catch (e: Exception) {
            NetworkResult.Failure(resourcesProvider.getString(R.string.error_occurred))
        }
    }
}