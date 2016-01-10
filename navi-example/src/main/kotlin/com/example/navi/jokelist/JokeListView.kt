/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.jokelist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.ViewAnimator
import butterknife.bindView
import com.example.navi.R

class JokeListView(context: Context, attrs: AttributeSet) : ViewAnimator(context, attrs) {
  val recyclerView: RecyclerView by bindView(R.id.joke_list_recycler)

  override fun onFinishInflate() {
    super.onFinishInflate()
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  fun displayError() {
    displayedChild = 2
  }

  fun displayJokes(jokes: List<CharSequence>) {
    recyclerView.adapter = JokeAdapter(jokes)
    displayedChild = 1
  }
}