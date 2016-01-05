/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.os.Bundle

abstract class Presenter<V>(val screenId: Int) {
  var view: V? = null

  open fun onCreate(savedState: Bundle?) {
  }

  fun takeView(view: V) {
    this.view = view
    onViewReady(view)
  }

  fun dropView() {
    val hadView = view != null
    view = null
    if (hadView) onViewDropped()
  }

  abstract fun onViewReady(view: V)

  open fun onSaveState(outState: Bundle) {
  }

  open fun onViewDropped() {
  }

  open fun onDestroy() {
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is Presenter<*>) return false

    return screenId == other.screenId
  }

  override fun hashCode(): Int = screenId
}
