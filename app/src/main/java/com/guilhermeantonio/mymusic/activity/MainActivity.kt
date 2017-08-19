package com.guilhermeantonio.mymusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.guilhermeantonio.mymusic.R
import com.guilhermeantonio.mymusic.adapter.AlbumAdapter
import com.spotify.sdk.android.player.*
import kaaes.spotify.webapi.android.SpotifyApi
import kotlinx.android.synthetic.main.activity_main.*
import com.spotify.sdk.android.player.SpotifyPlayer
import com.spotify.sdk.android.player.Spotify
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response


open class MainActivity : AppCompatActivity() {

    private var mAdapter: AlbumAdapter? = null
    private var objAlbum: Album? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        requestSpotify()
    }

    private fun requestSpotify() {

        val api = SpotifyApi()
        val token = intent.getStringExtra("EXTRA_TOKEN")
        api.setAccessToken(token)
        val spotify = api.service

        spotify.getArtistAlbums("6XyY86QOPPrYVGvF9ch6wz", object : Callback<Pager<Album>> {

            override fun success(t: Pager<Album>?, response: Response?) {

                val albumList = ArrayList<Album>()

                t?.items?.forEach {

                    objAlbum = Album()
                    objAlbum?.uri = it.images[1].url
                    objAlbum?.id = it.id
                    if (it.name.length >= 20) {
                        objAlbum?.name = it.name.replaceRange(20, it.name.length, "...")
                    } else {
                        objAlbum?.name = it.name
                    }

                    albumList.add(objAlbum!!)
                }

                mAdapter = AlbumAdapter(albumList, this@MainActivity, object : AlbumAdapter.OnItemClickListener {
                    override fun OnItemClickFoto(itemAlbum: Album) {

                        startActivity<MusicasAlbum>("id" to itemAlbum.id, "nome_album" to itemAlbum.name, "EXTRA_TOKEN" to intent.getStringExtra("EXTRA_TOKEN"))


                    }

                })

                progressBar_albums.visibility = View.INVISIBLE
                recycler_view_albums.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                recycler_view_albums.adapter = mAdapter

            }

            override fun failure(error: RetrofitError?) {

            }

        })


    }

}
