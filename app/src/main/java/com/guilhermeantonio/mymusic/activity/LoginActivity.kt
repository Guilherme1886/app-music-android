package com.guilhermeantonio.mymusic.activity


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.guilhermeantonio.mymusic.CredentialsHandler
import com.guilhermeantonio.mymusic.R
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    companion object {
         val TAG = "TAG"
         val CLIENT_ID = "9ba95dd8a104438696483a6d3662111e"
         val REDIRECT_URI = "http://localhost:8888/callback"
         val REQUEST_CODE = 1337
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifiyIfTokenExists()


    }

    private fun verifiyIfTokenExists() {

        val token = CredentialsHandler.getToken(this)
        if (token == null) {
            setContentView(R.layout.activity_login)
        } else {
            startMainActivity(token)
        }

    }


    fun loginSpotify(view: View) {

        val request = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(arrayOf("playlist-read"))
                .build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {

                AuthenticationResponse.Type.TOKEN -> {
                    logMessage("Got token: " + response.accessToken)
                    CredentialsHandler.setToken(this, response.accessToken, response.expiresIn.toLong(), TimeUnit.SECONDS)
                    startMainActivity(response.accessToken)


                }


                AuthenticationResponse.Type.ERROR -> logError("Auth error: " + response.error)


                else -> logError("Auth result: " + response.type)
            }
        }
    }


    private fun startMainActivity(token: String) {

        startActivity<MainActivity>("EXTRA_TOKEN" to token)
        finish()
    }

    private fun logError(msg: String) {
        toast(msg)
        Log.e(TAG, msg)
    }

    private fun logMessage(msg: String) {
        toast(msg)
        Log.d(TAG, msg)
    }
}
