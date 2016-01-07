/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.jokelist

import android.content.Context
import android.support.annotation.LayoutRes
import au.com.gridstone.navi.Presenter
import au.com.gridstone.navi.Screen
import com.example.navi.R

class JokeListScreen(val category: Category) : Screen<JokeListView>() {
  override val label = "Joke List"

  @LayoutRes override fun getLayoutRes(): Int = R.layout.joke_list

  override fun createPresenter(context: Context): Presenter<JokeListView> = JokeListPresenter(id, category)
}