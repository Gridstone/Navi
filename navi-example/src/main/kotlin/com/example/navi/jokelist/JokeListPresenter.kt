/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.jokelist

import android.text.Html
import au.com.gridstone.navi.Presenter
import com.example.navi.ChuckNorrisJokes
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.plusAssign
import rx.lang.kotlin.subscribeWith
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class JokeListPresenter(screenId: Int, val category: Category) : Presenter<JokeListView>(screenId) {
  private val viewSubscriptions = CompositeSubscription()
  private val jokes = when (category) {
    Category.NERDY -> ChuckNorrisJokes.webApi.jokes(ChuckNorrisJokes.CATEGORY_NERDY)
    Category.EXPLICIT -> ChuckNorrisJokes.webApi.jokes(ChuckNorrisJokes.CATEGORY_EXPLICIT)
    else -> ChuckNorrisJokes.webApi.uncategorisedJokes()
  }.flatMapIterable { result -> result.response().body().value }
      .map { it -> Html.fromHtml(it.joke) }
      .toList()
      .cache()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

  override fun onViewReady(view: JokeListView) {
    viewSubscriptions += jokes.subscribeWith {
      onError { view.displayError() }
      onNext { jokes -> view.displayJokes(jokes) }
    }
  }

  override fun onViewDropped() {
    viewSubscriptions.clear()
  }
}