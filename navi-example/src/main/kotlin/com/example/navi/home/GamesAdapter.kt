/*
 * Copyright (C) GRIDSTONE 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.navi.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.example.navi.Game
import com.example.navi.R
import com.jakewharton.rxbinding.view.clicks
import com.squareup.picasso.Picasso
import rx.Observable
import rx.lang.kotlin.PublishSubject

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
  private val clickSubject = PublishSubject<Game>()
  private var games = emptyList<Game>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.game_list_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindTo(games[position])
  }

  override fun getItemCount(): Int = games.count()

  fun setGames(games: List<Game>) {
    if (this.games != games) {
      this.games = games
      notifyDataSetChanged()
    }
  }

  fun clicks(): Observable<Game> = clickSubject

  inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView by bindView(R.id.list_entry_image)
    val nameView: TextView by bindView(R.id.list_entry_name)

    var game: Game? = null

    init {
      view.clicks().subscribe { clickSubject.onNext(game!!) }
    }

    fun bindTo(game: Game) {
      this.game = game
      nameView.text = game.name
      Picasso.with(view.context)
          .load(game.image.small_url)
          .fit()
          .centerCrop()
          .placeholder(R.drawable.gfx_triforce)
          .error(R.drawable.gfx_dead_link_small)
          .into(imageView)
    }
  }
}
