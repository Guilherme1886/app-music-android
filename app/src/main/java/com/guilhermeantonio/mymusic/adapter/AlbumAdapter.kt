package com.guilhermeantonio.mymusic.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.guilhermeantonio.mymusic.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kaaes.spotify.webapi.android.models.Album

/**
 * Created by Guilherme on 19/08/2017.
 */

class AlbumAdapter(private val albumList: List<Album>,
                   private val context: Context,
                   private val listener: AlbumAdapter.OnItemClickListener) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClickFoto(itemAlbum: Album)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position], listener)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageViewFotoAlbum: ImageView = itemView.findViewById(R.id.foto_album) as ImageView
        var textViewNomeAlbum: TextView = itemView.findViewById(R.id.nome_album) as TextView

        fun bind(item: Album, listener: OnItemClickListener) {

            Picasso.with(context).load(item.uri).into(imageViewFotoAlbum, object : Callback {
                override fun onSuccess() {
                    textViewNomeAlbum.text = item.name
                }

                override fun onError() {
                }
            })

            textViewNomeAlbum.text = item.name

            imageViewFotoAlbum.setOnClickListener { listener.OnItemClickFoto(item) }
        }
    }
}