package com.someshop.repository

import com.someshop.auth.AuthData
import com.someshop.database.*

class DatabaseRepository(val database: DatabaseManager) {

    //region Spot

    fun getAllSpots(): List<Spot> {
        return database.getAllSpots().map { Spot(it.id, it.name, it.cost, it.quantity, it.seller.id) }
    }

    fun getSpotsByUser(user: User): List<Spot> {
        return database.getSpotsByUser(user).map { Spot(it.id, it.name, it.cost, it.quantity, it.seller.id) }
    }

    fun getSpot(name: String): Spot? {
        return database.getSpot(name)?.let { Spot(it.id, it.name, it.cost, it.quantity, it.seller.id) }
    }

    fun getSpot(id: Int): Spot? {
        return database.getSpot(id)?.let { Spot(it.id, it.name, it.cost, it.quantity, it.seller.id) }
    }

    fun addSpot(draft: SpotDraft, seller: User): Spot? {
        return database.addSpot(draft, seller)?.let { Spot(it.id, it.name, it.cost, it.quantity, it.seller.id) }
    }

    fun updateSpot(id: Int, draft: SpotDraft):Boolean {
        return database.updateSpot(id, draft)
    }

    fun removeSpot(id: Int): Boolean {
        return database.deleteSpot(id)
    }

    //endregion

    //region Transaction

    fun getAllTransactions(): List<Transaction> {
        return database.getAllTransactions().map { Transaction(it.customer.id, it.spot.id, it.quantity) }
    }

    fun getTransactions(customer: User): List<Transaction> {
        return database.getTransactions(customer).map { Transaction(it.customer.id, it.spot.id, it.quantity) }
    }

    fun getTransactions(spot: Spot): List<Transaction> {
        return database.getTransactions(spot).map { Transaction(it.customer.id, it.spot.id, it.quantity) }
    }

    fun getTransaction(spot: Spot, customer: User): Transaction? {
        return database.getTransaction(spot, customer)?.let { Transaction(it.customer.id, it.spot.id, it.quantity) }
    }

    fun addTransaction(draft: TransactionDraft, customer: User): Transaction? {
        return database.addTransaction(draft, customer)?.let { Transaction(it.customer.id, it.spot.id, it.quantity) }
    }

    fun updateTransaction(spot: Spot, customer: User, draft: TransactionDraft): Boolean {
        return database.updateTransaction(spot, customer, draft)
    }

    fun deleteTransaction(spot: Spot, customer: User): Boolean {
        return database.deleteTransaction(spot, customer)
    }

    //endregion

    //region User

    fun getAllUsers(): List<User> {
        return database.getAllUsers().map { User(it.id, it.name) }
    }

    fun getUserById(id: Int): User? {
        return database.getUser(id)?.let { User(it.id, it.name) }
    }

    fun getUserByName(name: String): User? {
        return database.getUserByName(name)?.let { User(it.id, it.name) }
    }

    fun getUserAuthData(name: String): AuthData? {
        return database.getUserByName(name)?.let { AuthData(it.name, it.password) }
    }

    fun addUser(draft: UserDraft): User? {
        return database.addUser(draft)?.let { User(it.id, it.name) }
    }

    fun updateUser(id: Int, draft: UserDraft): Boolean {
        return database.updateUser(id, draft)
    }

    fun deleteUser(id: Int): Boolean {
        return database.deleteUser(id)
    }

    //endregion
}