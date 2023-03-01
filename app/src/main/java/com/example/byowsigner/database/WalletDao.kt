package com.example.byowsigner.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT id, name, created_at FROM wallets")
    fun getAll(): Flow<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getById(id: Int): Wallet

    @Insert
    suspend fun insertAll(vararg wallets: Wallet)
}