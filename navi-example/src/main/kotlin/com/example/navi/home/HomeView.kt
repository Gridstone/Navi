/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.home

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.ViewAnimator
import butterknife.bindView
import com.example.navi.R
import com.example.navi.jokelist.Category
import com.example.navi.jokelist.JokeListScreen
import com.jakewharton.rxbinding.view.clicks
import flow.Flow

class HomeView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
  val uncategorisedButton: Button by bindView(R.id.home_uncategorised_button)
  val nerdyButton: Button by bindView(R.id.home_nerdy_button)
  val explicitButton: Button by bindView(R.id.home_explicit_button)
  val randomJokeAnimator: ViewAnimator by bindView(R.id.home_random_animator)
  val randomJokeView: TextView by bindView(R.id.home_random_joke)

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    uncategorisedButton.clicks().subscribe { Flow.get(this).set(JokeListScreen(Category.UNCATEGORISED)) }
    nerdyButton.clicks().subscribe { Flow.get(this).set(JokeListScreen(Category.NERDY)) }
    explicitButton.clicks().subscribe { Flow.get(this).set(JokeListScreen(Category.EXPLICIT)) }
  }

  fun displayRandomJoke(randomJoke: String) {
    randomJokeView.text = Html.fromHtml(randomJoke)
    randomJokeAnimator.displayedChild = 1
  }
}