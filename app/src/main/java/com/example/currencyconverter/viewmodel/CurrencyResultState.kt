package com.example.currencyconverter.viewmodel

sealed class CurrencyResultState {
    class Success(val result: String) : CurrencyResultState()
    class Error(val message: String) : CurrencyResultState()
    object Loading : CurrencyResultState()
    object Nothing : CurrencyResultState()
}
