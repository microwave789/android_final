

package com.example.expense_tracker.presentation.add_edit_transaction

data class DialogState(
    val state: Boolean = false,
    val text: String ="Do you want to discard this transaction?"
)