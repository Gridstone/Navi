/*
 * Copyright (C) GRIDSTONE 2016
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