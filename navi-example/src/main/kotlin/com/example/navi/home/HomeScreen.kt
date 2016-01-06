/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.home

import android.content.Context
import au.com.gridstone.navi.Presenter
import au.com.gridstone.navi.Screen
import com.example.navi.R

class HomeScreen : Screen<HomeView>() {
  override val label: String = "Home"

  override fun getLayoutRes(): Int = R.layout.home

  override fun createPresenter(context: Context): Presenter<HomeView> = HomePresenter(id)
}
