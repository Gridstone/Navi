/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.ViewGroup
import flow.Flow
import flow.FlowDelegate
import flow.FlowDelegate.NonConfigurationInstance
import flow.History
import flow.StateParceler

fun onCreateForNavi(
    activity: Activity,
    presenterStack: PresenterStack,
    @LayoutRes containerId: Int,
    savedInstanceState: Bundle?,
    nonConfig: NonConfigurationInstance?,
    intent: Intent,
    parceler: StateParceler,
    defaultHistory: History
): NaviDelegate {
  if (activity.findViewById(containerId) == null) {
    activity.setContentView(containerId)
  }

  val container = activity.findViewById(containerId)

  if (container !is ViewGroup) {
    throw IllegalArgumentException("Provided containerId must be for a ViewGroup layout.")
  }

  return NaviDelegate(presenterStack, container, savedInstanceState, nonConfig, intent, parceler, defaultHistory)
}

class NaviDelegate internal constructor(
    presenterStack: PresenterStack,
    container: ViewGroup,
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

  fun getSystemService(activity: Activity, name: String): Any {
    if (Flow.isFlowSystemService(name)) {
      return flowDelegate.getSystemService(name)
    }

    return activity.getSystemService(name)
  }
}
