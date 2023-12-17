

package com.example.expense_tracker.presentation.transactions

import com.example.expense_tracker.domain.model.Transaction

data class TransactionsState(
    val list: List<Transaction> = mutableListOf()
)