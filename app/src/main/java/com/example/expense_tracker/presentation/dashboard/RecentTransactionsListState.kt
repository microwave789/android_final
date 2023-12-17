
package com.example.expense_tracker.presentation.dashboard

import com.example.expense_tracker.domain.model.Transaction

data class RecentTransactionsListState(
    val list: List<Transaction> = mutableListOf()
)
