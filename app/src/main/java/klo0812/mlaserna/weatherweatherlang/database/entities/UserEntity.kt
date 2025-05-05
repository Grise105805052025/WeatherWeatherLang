package klo0812.mlaserna.weatherweatherlang.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val id: String
)