/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.game

import au.com.gridstone.navi.Presenter
import com.example.navi.Game

class GamePresenter(screenId: Int, val game: Game) : Presenter<GameView>(screenId) {
  override fun onViewReady(view: GameView) {
    view.displayGame(game)
  }
}