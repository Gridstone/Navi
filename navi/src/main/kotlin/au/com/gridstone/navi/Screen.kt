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

import android.content.Context
import android.view.View
import java.util.concurrent.atomic.AtomicInteger

abstract class Screen<V> {
  val id = nextId.incrementAndGet()

  abstract val label: String

  abstract fun getLayoutRes(): Int

  abstract fun createPresenter(context: Context): Presenter<V>

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is Screen<*>) return false

    return this.id == other.id
  }

  override fun hashCode(): Int = id

  @Suppress("UNCHECKED_CAST")
  fun link(presenter: Presenter<*>, view: View) {
    try {
      if (presenter.view == null) {
        val castPresenter = presenter as Presenter<V>
        view.tag = presenter
        castPresenter.takeView(view as V)
      }
    } catch (e: Exception) {
      throw IllegalArgumentException("screen, presenter, and view must all be for the same view type.", e)
    }
  }

  private companion object {
    private val nextId = AtomicInteger()
  }
}