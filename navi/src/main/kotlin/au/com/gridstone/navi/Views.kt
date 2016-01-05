/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.view.View
import android.view.ViewTreeObserver

internal inline fun View.waitForMeasure(crossinline action: (View) -> Unit) {
  if (width > 0 && height > 0) {
    action(this)
    return
  }

  val listener = object : View.OnAttachStateChangeListener, ViewTreeObserver.OnPreDrawListener {
    override fun onPreDraw(): Boolean {
      action(this@waitForMeasure)
      viewTreeObserver.removeOnPreDrawListener(this)
      return true
    }

    override fun onViewDetachedFromWindow(v: View?) {
      viewTreeObserver.removeOnPreDrawListener(this)
      removeOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View?) {
    }
  }

  viewTreeObserver.addOnPreDrawListener(listener)
  addOnAttachStateChangeListener(listener)
}
