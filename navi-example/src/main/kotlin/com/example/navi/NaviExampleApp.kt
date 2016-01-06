/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi

import android.app.Application
import android.content.Context
import au.com.gridstone.navi.PresenterStack

class NaviExampleApp : Application() {
  val presenterStack: PresenterStack

  init {
    presenterStack = PresenterStack(this)
  }
}

fun getApp(context: Context): NaviExampleApp = context.applicationContext as NaviExampleApp