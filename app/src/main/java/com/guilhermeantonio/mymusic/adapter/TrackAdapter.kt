package com.guilhermeantonio.mymusic.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guilhermeantonio.mymusic.R
import kaaes.spotify.webapi.android.models.Track

/**
 * Created by Guilherme on 19/08/2017.
 */
class TrackAdapter(private val trackList: List<Track>,
                   private val context: Context,
                   private val listener: TrackAdapter.OnItemClickListener) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClickTrack(itemAlbum: Track, textView: TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_musica, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], listener)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewNomeMusica: TextView = itemView.findViewById(R.id.nome_musica) as TextView
        var textViewNomeArtista: TextView = itemView.findViewById(R.id.nome_artista) as TextView

        fun bind(item: Track, listener: OnItemClickListener) {

            textViewNomeMusica.text = item.name
            textViewNomeArtista.text = item.type

            itemView.setOnClickListener { listener.OnItemClickTrack(item, textViewNomeMusica) }
        }
    }
}