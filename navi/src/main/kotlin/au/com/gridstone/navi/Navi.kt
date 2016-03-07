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
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.support.annotation.IdRes
import flow.Dispatcher

object Navi {
  private const val STACK_SERVICE = "navi.STACK_SERVICE"
  private const val DISPATCHER_SERVICE = "navi.DISPATCHER_SERVICE"

  fun appBaseContext(baseContext: Context, app: Application): Context {
    val presenterStack: PresenterStack = PresenterStack(app)

    app.registerActivityLifecycleCallbacks(object : ActivityLifeCycleAdapter() {
      override fun onActivityPaused(activity: Activity) {
        presenterStack.peek()?.dropView()
      }

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        presenterStack.onSaveInstanceState(outState)
      }

      override fun onActivityDestroyed(activity: Activity) {
        presenterStack.onDestroy(activity)
      }
    })

    return AppContextWrapper(baseContext, presenterStack)
  }

  private class AppContextWrapper(baseContext: Context, val presenterStack: PresenterStack)
  : ContextWrapper(baseContext) {
    override fun getSystemService(name: String?): Any? {
      if (STACK_SERVICE == name) {
        return presenterStack
      }

      return super.getSystemService(name)
    }
  }

  fun configure(baseContext: Context, activity: Activity): Installer {
    val presenterStack: PresenterStack = baseContext.applicationContext.getSystemService(
        STACK_SERVICE) as PresenterStack?
        ?: throw IllegalStateException(
        "Navi not configured correctly in Application. Are you using Navi.appBaseContext()?")

    return Installer(baseContext, activity, presenterStack)
  }

  data class Installation(val dispatcher: Dispatcher, val newContext: Context)

  class Installer(val baseContext: Context,
                  val activity: Activity,
                  val presenterStack: PresenterStack) {
    private var containerId: Int? = null
    private var segue: Segue? = null
    private var listener: NaviListener? = null

    fun containerId(@IdRes containerId: Int): Installer {
      this.containerId = containerId
      return this
    }

    fun segue(segue: Segue): Installer {
      this.segue = segue
      return this
    }

    fun listener(listener: NaviListener): Installer {
      this.listener = listener
      return this
    }

    fun install(): Installation {
      val containerId = this.containerId ?: throw NullPointerException(
          "You must call containerId() when configuring Navi.")
      val segue = this.segue ?: CrossFadeSegue()
      val dispatcher = NaviDispatcher(presenterStack, baseContext, activity, containerId, segue,
          listener)
      val wrappedContext = ActivityContextWrapper(baseContext, dispatcher)

      return Installation(dispatcher, wrappedContext)
    }
  }

  private class ActivityContextWrapper(baseContext: Context, val dispatcher: NaviDispatcher)
  : ContextWrapper(baseContext) {
    override fun getSystemService(name: String?): Any? {
      if (DISPATCHER_SERVICE == name) {
        return dispatcher
      }

      return super.getSystemService(name)
    }
  }

  fun goBack(activity: Activity): Boolean {
    val dispatcher = activity.getSystemService(
        DISPATCHER_SERVICE) as NaviDispatcher? ?: throw IllegalStateException(
        "Navi has not been configured for this Activity.")

    return dispatcher.goBack()
  }
}
