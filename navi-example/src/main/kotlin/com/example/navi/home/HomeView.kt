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

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.ViewAnimator
import butterknife.bindView
import com.example.navi.Game
import com.example.navi.R
import com.example.navi.game.GameScreen
import flow.Flow

class HomeView(context: Context, attrs: AttributeSet) : ViewAnimator(context, attrs) {
  val recyclerView: RecyclerView by bindView(R.id.home_recycler)
  val adapter = GamesAdapter()

  override fun onFinishInflate() {
    super.onFinishInflate()
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    adapter.clicks().subscribe {
      Flow.get(this).set(GameScreen(it))
    }
  }

  fun displayGames(games: List<Game>) {
    adapter.setGames(games)
    displayedChild = 1
  }

  fun displayError() {
    displayedChild = 2
  }
}