package com.guilhermeantonio.mymusic

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

/**
 * Created by Guilherme on 15/08/2017.
 */

class CredentialsHandler {

    companion object{

        private val ACCESS_TOKEN_NAME = "webapi.credentials.access_token"
        private val ACCESS_TOKEN = "access_token"
        private val EXPIRES_AT = "expires_at"

        fun setToken(context: Context, token: String, expiresIn: Long, unit: TimeUnit) {
            val appContext = context.applicationContext

            val now = System.currentTimeMillis()
            val expiresAt = now + unit.toMillis(expiresIn)

            val sharedPref = getSharedPreferences(appContext)
            val editor = sharedPref.edit()
            editor.putString(ACCESS_TOKEN, token)
            editor.putLong(EXPIRES_AT, expiresAt)
            editor.apply()
        }


        private fun getSharedPreferences(appContext: Context): SharedPreferences {
            return appContext.getSharedPreferences(ACCESS_TOKEN_NAME, Context.MODE_PRIVATE)
        }

        fun getToken(context: Context): String? {
            val appContext = context.applicationContext
            val sharedPref = getSharedPreferences(appContext)

            val token = sharedPref.getString(ACCESS_TOKEN, null)
            val expiresAt = sharedPref.getLong(EXPIRES_AT, 0L)

            if (token == null || expiresAt < System.currentTimeMillis()) {
                return null
            }

            return token
        }

    }

}