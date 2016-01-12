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
