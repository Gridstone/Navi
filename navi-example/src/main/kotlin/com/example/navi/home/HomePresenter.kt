/*
 * Copyright (C) GRIDSTONE 2016
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