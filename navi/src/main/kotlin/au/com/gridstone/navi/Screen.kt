/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.content.Context
import android.view.View
import java.util.concurrent.atomic.AtomicInteger

private val nextId = AtomicInteger()

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
}