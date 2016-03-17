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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import flow.Direction
import flow.Dispatcher
import flow.History
import flow.Traversal
import flow.TraversalCallback

internal class NaviDispatcher(val presenterStack: PresenterStack,
                              baseContext: Context,
                              activity: Activity,
                              @IdRes val containerId: Int,
                              val segue: Segue,
                              val listener: NaviListener?)
: Dispatcher, HandlesBack {
  private val inflater by lazy { LayoutInflater.from(activity) }
  private val container by lazy { activity.findViewById(containerId) as ViewGroup }
  private var currentHistory: History? = null

  init {
    val app = baseContext.applicationContext as Application
    app.registerActivityLifecycleCallbacks(object : ActivityLifeCycleAdapter() {
      override fun onActivityResumed(activity: Activity) {
        val currentPresenter = presenterStack.peek()
        val currentView: View? = container.getChildAt(0)

        if (currentPresenter != null && currentView != null) {
          currentHistory?.top<Screen<*>>()?.link(currentPresenter, currentView)
        }
      }

      override fun onActivityDestroyed(activity: Activity) {
        app.unregisterActivityLifecycleCallbacks(this)
      }
    })
  }

  override fun dispatch(traversal: Traversal, callback: TraversalCallback) {
    val destinationScreen: Screen<*> = traversal.destination.top()
    val currentView: View? = container.getChildAt(0)

    if (traversal.direction == Direction.REPLACE && destinationScreen.equals(
        currentHistory?.top()) && currentView != null) {
      // If we're just replacing to the current screen, and that view is visible...
      // don't bother doing anything.
      callback.onTraversalCompleted()
      return
    }

    currentHistory = traversal.destination

    listener?.onPreNavigate(destinationScreen, traversal.direction)

    presenterStack.update(traversal.destination)
    val destinationPresenter = presenterStack.peek()!!

    val destinationView = inflater.inflate(destinationScreen.getLayoutRes(), container, false)
    container.addView(destinationView)

    destinationScreen.link(destinationPresenter, destinationView)

    traversal.getState(traversal.destination.top()).restore(destinationView)

    val currentKey: Any? = traversal.origin?.top()

    if (currentView != null && traversal.direction == Direction.FORWARD && currentKey != null) {
      traversal.getState(currentKey).save(currentView)
    }

    if (currentView == null) {
      callback.onTraversalCompleted()
      listener?.onNavigate(destinationScreen, traversal.direction)
      return
    }

    currentView.waitForMeasure {
      destinationView.waitForMeasure {
        val animator = segue.createAnimation(currentView, destinationView, traversal.direction)
        animator.addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator?) {
            container.removeView(currentView)
            callback.onTraversalCompleted()
            listener?.onNavigate(destinationScreen, traversal.direction)
          }
        })
        animator.start()
      }
    }
  }

  override fun goBack(): Boolean {
    val currentView: View? = container.getChildAt(0)
    if (currentView is HandlesBack) return currentView.goBack()
    return false;
  }
}