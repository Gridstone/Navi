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
import au.com.gridstone.navi.Presenter
import au.com.gridstone.navi.Screen
import com.example.navi.Game
import com.example.navi.R

class GameScreen(val game: Game) : Screen<GameView>() {
  override val label = game.name

  override fun getLayoutRes(): Int = R.layout.game

  override fun createPresenter(context: Context): Presenter<GameView> = GamePresenter(id, game)
}