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
