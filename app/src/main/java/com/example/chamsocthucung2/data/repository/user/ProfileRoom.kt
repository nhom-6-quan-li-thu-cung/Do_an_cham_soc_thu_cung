package com.example.chamsocthucung2.data.repository.user

import com.example.chamsocthucung2.data.model.user.Profile.ProfileUserDao
import com.example.chamsocthucung2.data.model.user.Profile.ProfileUserEntity


class ProfileRoom(private val profileUserDao: ProfileUserDao) {

    suspend fun getProfile(uid: String): ProfileUserEntity? {
        return profileUserDao.getProfileByUid(uid)
    }

    suspend fun insertOrUpdateProfile(profile: ProfileUserEntity) {
        profileUserDao.insertProfile(profile)
    }

    suspend fun updateDarkMode(uid: String, isDarkMode: Boolean) {
        profileUserDao.updateDarkMode(uid, isDarkMode)
    }

    suspend fun updateThemeColor(uid: String, themeColor: String) {
        profileUserDao.updateThemeColor(uid, themeColor)
    }

    suspend fun updateFontSize(uid: String, fontSize: String) {
        profileUserDao.updateFontSize(uid, fontSize)
    }

}
