package com.someshop.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

data class User(
    val id: Int,
    val name: String
)

data class UserDraft(
    val name: String,
    val password: String
)

interface UserEntity : Entity<UserEntity> {
    companion object : Entity.Factory<UserEntity>()

    val id: Int
    val name: String
    val password: String
}

object UserTable : Table<UserEntity>("user") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val password = varchar("password").bindTo { it.password }
}
