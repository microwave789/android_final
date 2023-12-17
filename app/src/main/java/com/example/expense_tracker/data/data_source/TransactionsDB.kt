

package com.example.expense_tracker.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expense_tracker.domain.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionsDB : RoomDatabase() {
    abstract val transactionDao: TransactionDao

    companion object {
        const val DATABASE_NAME = "transactions_db"
    }
}