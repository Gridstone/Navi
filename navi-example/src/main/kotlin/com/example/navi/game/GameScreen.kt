/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.game

import android.content.Context
import au.com.gridstone.navi.Presenter
import au.com.gridstone.navi.Screen
import com.example.navi.Game
import com.example.navi.R

class GameScreen(val game: Game) : Screen<GameView>() {
  override val label = game.name

  override fun getLayoutRes(): Int = R.layout.game

  override fun createPresenter(context: Context): Presenter<GameView> = GamePresenter(id, game)
}