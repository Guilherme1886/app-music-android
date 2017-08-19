package com.guilhermeantonio.mymusic.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.TextView

import com.guilhermeantonio.mymusic.R
import com.guilhermeantonio.mymusic.adapter.AlbumAdapter
import com.guilhermeantonio.mymusic.adapter.TrackAdapter
import com.spotify.sdk.android.player.*
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Track
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_musicas_album.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MusicasAlbum : AppCompatActivity(), SpotifyPlayer.InitializationObserver, ConnectionStateCallback {

    private var mPlayer: Player? = null
    private var mAdapter: TrackAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicas_album)

        configToolbar()
        configPlayer()
        requestSpotify()

    }

    private fun configPlayer() {

        val playerConfig = Config(this, intent.getStringExtra("EXTRA_TOKEN"), LoginActivity.CLIENT_ID)
        Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
            override fun onInitialized(spotifyPlayer: SpotifyPlayer) {
                mPlayer = spotifyPlayer
                mPlayer?.addConnectionStateCallback(this@MusicasAlbum)

            }

            override fun onError(throwable: Throwable) {

            }
        })

    }

    private fun configToolbar() {
        nome_album_toolbar.text = intent.getStringExtra("nome_album")
    }

    private fun requestSpotify() {

        val api = SpotifyApi()
        val token = intent.getStringExtra("EXTRA_TOKEN")
        api.setAccessToken(token)
        val spotify = api.service

        spotify.getAlbumTracks(intent.getStringExtra("id"), object : Callback<Pager<Track>> {
            override fun failure(error: RetrofitError?) {

            }

            override fun success(t: Pager<Track>?, response: Response?) {

                val trackList = ArrayList<Track>()
                var objTrack: Track

                t?.items?.forEachIndexed { index, track ->

                    objTrack = Track()
                    objTrack.type = track.artists[0].name
                    objTrack.uri = track.uri
                    objTrack.name = track.name

                    trackList.add(objTrack)

                }

                mAdapter = TrackAdapter(trackList, this@MusicasAlbum, object : TrackAdapter.OnItemClickListener {
                    override fun OnItemClickTrack(itemAlbum: Track, textView: TextView) {


                        mPlayer?.playUri(null, itemAlbum.uri, 0, 0)
                    }


                })

                progressBar_musicas.visibility = View.INVISIBLE
                recycler_view_musicas.layoutManager = LinearLayoutManager(this@MusicasAlbum, LinearLayoutManager.VERTICAL, false)
                recycler_view_musicas.adapter = mAdapter


            }

        })


    }

    override fun onInitialized(p0: SpotifyPlayer?) {


    }

    override fun onError(p0: Throwable?) {

    }

    override fun onDestroy() {
        Spotify.destroyPlayer(this)
        super.onDestroy()
    }

    override fun onLoggedIn() {
        Log.v("TAG", "User logged in")


    }

    override fun onLoggedOut() {
        Log.v("TAG", "User logged out")
    }

    override fun onLoginFailed(error: Error) {
        Log.v("TAG", "Login failed")
    }

    override fun onTemporaryError() {
        Log.v("TAG", "Temporary error occurred")
    }

    override fun onConnectionMessage(message: String) {
        Log.v("TAG", "Received connection message: " + message)
    }

}
