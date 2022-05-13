package com.someshop.database

import org.ktorm.entity.Entity
import org.ktorm.schema.*

data class Spot(
    val id: Int,
    val name: String,
    val cost: Double,
    val quantity: Int,
    val sellerId: Int
)

data class SpotDraft(
    val name: String,
    val cost: Double,
    val quantity: Int
)

interface SpotEntity : Entity<SpotEntity> {
    companion object : Entity.Factory<SpotEntity>()

    val id: Int
    val name: String
    val cost: Double
    val quantity: Int
    val seller: UserEntity
}

object SpotTable : Table<SpotEntity>("spot") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val cost = double("cost").bindTo { it.cost }
    val quantity = int("quantity").bindTo { it.quantity }
    val seller = int("seller").references(UserTable) { it.seller }
}
