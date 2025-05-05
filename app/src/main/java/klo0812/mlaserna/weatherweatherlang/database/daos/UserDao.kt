package klo0812.mlaserna.weatherweatherlang.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity WHERE userId = :userId")
    fun get(userId: String) : UserEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg weathers: UserEntity)

}