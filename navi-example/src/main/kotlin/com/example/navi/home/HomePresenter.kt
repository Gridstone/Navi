/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.home

import au.com.gridstone.navi.Presenter
import com.example.navi.ChuckNorrisJokes
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.plusAssign
import rx.lang.kotlin.subscribeWith
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class HomePresenter(screenId: Int) : Presenter<HomeView>(screenId) {
  private val viewSubscriptions = CompositeSubscription()
  private val randomJoke = ChuckNorrisJokes.webApi.randomNerdyJoke()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .cache()

  override fun onViewReady(view: HomeView) {
    viewSubscriptions += randomJoke.subscribeWith {
      onError { view.displayRandomJoke("Error receiving random joke from server.") }
      onNext { result ->
        when {
          result.isError -> view.displayRandomJoke(result.error().message ?: "Error receiving random joke from server.")
          result.response().isSuccess -> view.displayRandomJoke(result.response().body().value.joke)
          else -> view.displayRandomJoke(result.response().errorBody().string())
        }
      }
    }
  }

  override fun onViewDropped() {
    viewSubscriptions.clear()
  }
}