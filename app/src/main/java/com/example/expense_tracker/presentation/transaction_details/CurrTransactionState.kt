

package com.example.expense_tracker.presentation.transaction_details

import com.example.expense_tracker.domain.model.Transaction


data class CurrTransactionState(
    val transaction: Transaction? = Transaction(
        id = -1,
        transactionType = "",
        title = "",
        amount = 0,
        tags = "",
        date = "",
        note = ""
    )

)