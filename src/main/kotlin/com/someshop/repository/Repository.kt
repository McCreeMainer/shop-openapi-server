package com.someshop.repository

import com.someshop.database.*

interface Repository {

    fun getAllSpots(): List<Spot>

    fun getSpots(name: String): List<Spot>

    fun getSpots(user: User): List<Spot>

    fun getSpot(id: Int): Spot?

    fun addSpot(draft: SpotDraft, seller: User): Spot?

    fun updateSpot(id: Int, draft: SpotDraft): Boolean

    fun removeSpot(id: Int): Boolean

    fun getAllTransactions(): List<Transaction>

    fun getTransactions(customer: User): List<Transaction>

    fun getTransactions(spot: Spot): List<Transaction>

    fun getTransaction(spot: Spot, customer: User): Transaction?

    fun addTransaction(draft: TransactionDraft, customer: User)

    fun updateTransaction(spot: Spot, customer: User, draft: TransactionDraft): Boolean

    fun deleteTransaction(spot: Spot, customer: User): Boolean

    fun getAllUsers(): List<User>

    fun getUsers(name: String): List<User>

    fun getUser(id: Int): User?

    fun getUserByName(name: String): User?

    fun addUser(draft: UserDraft): User?

    fun updateUser(id: Int, draft: UserDraft): Boolean

    fun deleteUser(id: Int): Boolean
}