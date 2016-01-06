/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import flow.FlowDelegate
import flow.FlowDelegate.NonConfigurationInstance
import flow.History
import flow.StateParceler

fun onCreateForNavi(
    container: ViewGroup,
    presenterStack: PresenterStack,
    savedInstanceState: Bundle?,
    nonConfig: NonConfigurationInstance?,
    intent: Intent,
    parceler: StateParceler,
    defaultHistory: History
): NaviDelegate = NaviDelegate(container, presenterStack, savedInstanceState, nonConfig, intent, parceler, defaultHistory)

class NaviDelegate internal constructor(
    container: ViewGroup,
    presenterStack: PresenterStack,
    savedInstanceState: Bundle?,
    nonConfig: NonConfigurationInstance?,
    intent: Intent,
    parceler: StateParceler,
    defaultHistory: History
) {
  val navi = Navi(container, presenterStack)
  val flowDelegate: FlowDelegate

  init {
    navi.onCreate(savedInstanceState)
    flowDelegate = FlowDelegate.onCreate(nonConfig, intent, savedInstanceState, parceler, defaultHistory, navi)
  }

  fun onResume() {
    flowDelegate.onResume()
    navi.onResume()
  }

  fun onPause() {
    flowDelegate.onPause()
    navi.onPause()
  }

  fun onNewIntent(intent: Intent) {
    flowDelegate.onNewIntent(intent)
  }

  fun onBackPressed(): Boolean {
    if (navi.goBack()) return true
    if (flowDelegate.onBackPressed()) return true
    return false
  }

  fun onSaveInstanceState(outState: Bundle) {
    flowDelegate.onSaveInstanceState(outState)
    navi.onSaveInstanceState(outState)
  }

  fun onDestroy(activity: Activity) {
    navi.onDestroy(activity)
  }

  fun onRetainCustomNonConfigurationInstance(): FlowDelegate.NonConfigurationInstance =
      flowDelegate.onRetainNonConfigurationInstance()

  fun getFlowSystemService(name: String) = flowDelegate.getSystemService(name)
}
