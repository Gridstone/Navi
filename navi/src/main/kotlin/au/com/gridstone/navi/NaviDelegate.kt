/*
 * Copyright (C) GRIDSTONE 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
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
    defaultHistory: History,
    listener: Navi.Listener? = null,
    segue: Segue = CrossFadeSegue()
): NaviDelegate = NaviDelegate(container, presenterStack, savedInstanceState, nonConfig, intent, parceler, defaultHistory, listener, segue)

class NaviDelegate internal constructor(
    container: ViewGroup,
    presenterStack: PresenterStack,
    savedInstanceState: Bundle?,
    nonConfig: NonConfigurationInstance?,
    intent: Intent,
    parceler: StateParceler,
    defaultHistory: History,
    listener: Navi.Listener?,
    segue: Segue
) {
  val navi = Navi(container, presenterStack, listener, segue)
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
