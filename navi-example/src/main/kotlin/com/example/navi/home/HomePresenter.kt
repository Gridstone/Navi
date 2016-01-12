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

import au.com.gridstone.navi.Presenter
import com.example.navi.ZeldaGames
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.plusAssign
import rx.lang.kotlin.subscribeWith
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class HomePresenter(screenId: Int) : Presenter<HomeView>(screenId) {
  private val viewSubscriptions = CompositeSubscription()
  private val games = ZeldaGames.webApi
      .games()
      .map { it.response().body().results }
      .cache()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

  override fun onViewReady(view: HomeView) {
    viewSubscriptions += games.subscribeWith {
      onError { view.displayError() }
      onNext { view.displayGames(it) }
    }
  }

  override fun onViewDropped() {
    viewSubscriptions.clear()
  }
}