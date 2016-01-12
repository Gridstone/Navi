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

package com.example.navi.game

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import butterknife.bindView
import com.example.navi.Game
import com.example.navi.R
import com.squareup.picasso.Picasso

class GameView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
  val imageView: ImageView by bindView(R.id.game_image)
  val nameView: TextView by bindView(R.id.game_name)
  val summaryView: TextView by bindView(R.id.game_summary)
  val aliasesView: TextView by bindView(R.id.game_aliases)
  val releaseDateView: TextView by bindView(R.id.game_release_date)

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
  }

  fun displayGame(game: Game) {
    Picasso.with(context)
        .load(game.image.super_url)
        .fit()
        .centerCrop()
        .placeholder(R.color.colorPrimaryDark)
        .error(R.drawable.gfx_dead_link)
        .into(imageView)

    nameView.text = game.name
    summaryView.text = game.deck
    aliasesView.text = game.aliases
    releaseDateView.text = game.original_release_date
  }
}