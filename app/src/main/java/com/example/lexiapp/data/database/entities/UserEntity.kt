package com.example.lexiapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "mail") val mail: String,
    @ColumnInfo(name = "birth") val birth: String,
    @ColumnInfo(name = "gender") val gender: String
)
