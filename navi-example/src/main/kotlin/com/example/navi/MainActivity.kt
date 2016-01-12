/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi

import android.view.ViewGroup
import au.com.gridstone.navi.appcompat.AppCompatNaviActivity
import au.com.gridstone.navi.PresenterStack
import au.com.gridstone.navi.Segue
import com.example.navi.home.HomeScreen
import flow.History
import flow.StateParceler

class MainActivity : AppCompatNaviActivity() {
  override fun getPresenterStack(): PresenterStack = getApp(this).presenterStack

  override fun getContainer(): ViewGroup {
    setContentView(R.layout.main_container)
    return findViewById(R.id.main_container) as ViewGroup
  }

  override fun getParceler(): StateParceler = GsonParceler()

  override fun getDefaultHistory(): History = History.single(HomeScreen())

  override fun getSegue(): Segue = SlideSegue()
}
