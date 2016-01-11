/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import flow.Flow
import flow.Flow.Direction
import flow.History

class Navi(val container: ViewGroup,
           val presenterStack: PresenterStack,
           val listener: Listener? = null,
           val segue: Segue = CrossFadeSegue())
: Flow.Dispatcher, HandlesBack {
  private val inflater = LayoutInflater.from(container.context)
  var currentHistory: History? = null

  fun onCreate(savedState: Bundle?) {
    presenterStack.onCreate(savedState)
  }

  override fun dispatch(traversal: Flow.Traversal, callback: Flow.TraversalCallback) {
    currentHistory = traversal.destination
    val destinationScreen: Screen<*> = traversal.destination.top()
    val currentView: View? = container.getChildAt(0)

    if (traversal.direction == Direction.REPLACE && destinationScreen.equals(traversal.origin.top()) && currentView != null) {
      // If we're just replacing to the current screen, and that view is visible...
      // don't bother doing anything.
      callback.onTraversalCompleted()
      return
    }

    listener?.onPreNavigate(destinationScreen, traversal.direction)

    presenterStack.update(traversal.destination)
    val destinationPresenter = presenterStack.peek()!!

    val destinationView = inflater.inflate(destinationScreen.getLayoutRes(), container, false)
    container.addView(destinationView)

    destinationScreen.link(destinationPresenter, destinationView)

    traversal.destination.currentViewState().restore(destinationView)

    if (currentView != null && traversal.direction == Direction.FORWARD) {
      traversal.origin.currentViewState().save(currentView)
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

  fun onPause() {
    presenterStack.peek()?.dropView()
  }

  fun onResume() {
    val currentPresenter = presenterStack.peek()
    val currentView: View? = container.getChildAt(0)

    if (currentPresenter != null && currentView != null) {
      currentHistory?.top<Screen<*>>()?.link(currentPresenter, currentView)
    }
  }

  fun onSaveInstanceState(outState: Bundle) {
    presenterStack.onSaveInstanceState(outState)
  }

  fun onDestroy(activity: Activity) {
    presenterStack.onDestroy(activity)
  }

  interface Listener {
    fun onPreNavigate(destination: Screen<*>, direction: Direction)
    fun onNavigate(destination: Screen<*>, direction: Direction)
  }
}

