package com.example.chamsocthucung2.data.model.user.Profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.*

@Dao
interface ProfileUserDao {

    @Query("SELECT * FROM profile_user WHERE uid = :uid LIMIT 1")
    suspend fun getProfileByUid(uid: String): ProfileUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileUserEntity)

    @Update
    suspend fun updateProfile(profile: ProfileUserEntity)

    @Query("UPDATE profile_user SET isDarkMode = :isDarkMode WHERE uid = :uid")
    suspend fun updateDarkMode(uid: String, isDarkMode: Boolean)

    @Query("UPDATE profile_user SET themeColor = :themeColor WHERE uid = :uid")
    suspend fun updateThemeColor(uid: String, themeColor: String)

    @Query("UPDATE profile_user SET fontSize = :fontSize WHERE uid = :uid")
    suspend fun updateFontSize(uid: String, fontSize: String)
}
