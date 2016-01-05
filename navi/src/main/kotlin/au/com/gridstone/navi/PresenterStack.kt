/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.app.Activity
import android.app.Application
import android.os.Bundle
import flow.History
import java.util.ArrayDeque
import java.util.Deque

const val STATES_KEY = "PRESENTER_STACK_STATES"

class PresenterStack(val app: Application) {
  private var lastSavedState: Bundle? = null
  private val presenters: Deque<Presenter<*>> = ArrayDeque()

  fun onCreate(savedState: Bundle?) {
    lastSavedState = savedState?.getBundle(STATES_KEY)
  }

  fun onSaveInstanceState(outState: Bundle) {
    val stackBundle = Bundle()

    presenters.forEach {
      val presenterBundle = Bundle()
      it.onSaveState(presenterBundle)

      val key = it.screenId.toString()
      stackBundle.putBundle(key, presenterBundle)
    }

    outState.putBundle(STATES_KEY, stackBundle)
  }

  fun onDestroy(activity: Activity) {
    if (activity.isFinishing) {
      presenters.forEach { it.onDestroy() }
    }
  }

  fun peek(): Presenter<*>? = presenters.peek()

  fun update(history: History) {
    presenters.removeAll(fun(presenter: Presenter<*>): Boolean {
      presenter.dropView()
      val remove = !history.map { it as Screen<*> }.any { it.id == presenter.screenId }
      if (remove) presenter.onDestroy()
      return remove
    })

    val newPresenters: Deque<Presenter<*>> = ArrayDeque()

    // Reverse history, so we can go from oldest to newest.
    history.reversed().forEach {
      val screen = it as Screen<*>
      val existingPresenter = presenters.singleOrNull { it.screenId == screen.id }
      val alreadyExists = existingPresenter != null
      val presenter = existingPresenter ?: screen.createPresenter(app)

      if (!alreadyExists) {
        val savedState = lastSavedState?.getBundle(screen.id.toString())
        presenter.onCreate(savedState)
      }

      presenter.dropView()
      newPresenters.push(presenter)
    }

    presenters.clear()
    presenters.addAll(newPresenters)
  }

  fun clear() {
    presenters.forEach {
      it.dropView()
      it.onDestroy()
    }

    presenters.clear()
    lastSavedState = null
  }

  fun currentStack(): Deque<Presenter<*>> {
    return ArrayDeque(presenters)
  }
}

