/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.common.truth.Truth.assertThat
import flow.History
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

const val SAVED_STATE_KEY = "SAVED_STATE_CALLED"

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP), manifest = "src/main/AndroidManifest.xml")
class PresenterStackTest {
  lateinit var presenterStack: PresenterStack
  lateinit var screen1: TestScreen
  lateinit var screen2: TestScreen
  lateinit var history: History
  lateinit var presenter1: TestPresenter
  lateinit var presenter2: TestPresenter

  class TestPresenter(screenId: Int) : Presenter<View>(screenId) {
    var destroyed = false
    var viewDropped = false
    var onCreateCalled = false
    var onCreateCalledWithSavedBundle = false

    override fun onCreate(savedState: Bundle?) {
      onCreateCalled = true
      onCreateCalledWithSavedBundle = savedState != null && savedState.getBoolean(SAVED_STATE_KEY)
    }

    override fun onViewDropped() {
      viewDropped = true
    }

    override fun onViewReady(view: View) {
    }

    override fun onSaveState(outState: Bundle) {
      outState.putBoolean(SAVED_STATE_KEY, true)
    }

    override fun onDestroy() {
      destroyed = true
    }
  }

  class TestScreen : Screen<View>() {
    override val label: String = "testScreen"

    override fun getLayoutRes(): Int {
      throw UnsupportedOperationException()
    }

    override fun createPresenter(context: Context): Presenter<View> = TestPresenter(id)
  }

  @Before fun setup() {
    presenterStack = PresenterStack(RuntimeEnvironment.application)
    screen1 = TestScreen()
    screen2 = TestScreen()
    history = History.emptyBuilder().push(screen1).push(screen2).build()

    presenterStack.update(history)
    presenter1 = presenterStack.currentStack().last as TestPresenter
    presenter2 = presenterStack.currentStack().first as TestPresenter
  }

  @Test fun populateFromHistory() {
    assertThat(presenterStack.currentStack()).containsExactly(presenter1, presenter2)
    assertThat(presenterStack.peek()).isEqualTo(presenter2)
  }

  @Test fun propagateSaveState() {
    val testBundle = Bundle()
    presenterStack.onSaveInstanceState(testBundle)

    val stackBundle = testBundle.getBundle(STATES_KEY)
    val presenter1Bundle = stackBundle.getBundle(presenter1.screenId.toString())
    val presenter2Bundle = stackBundle.getBundle(presenter2.screenId.toString())

    assertThat(presenter1Bundle.getBoolean(SAVED_STATE_KEY)).isTrue()
    assertThat(presenter2Bundle.getBoolean(SAVED_STATE_KEY)).isTrue()
  }

  @Test fun propagateDestroy() {
    val nonFinishingActivity = object : Activity() {
      override fun isFinishing(): Boolean = false
    }

    val finishingActivity = object : Activity() {
      override fun isFinishing(): Boolean = true
    }

    presenterStack.onDestroy(nonFinishingActivity)
    assertThat(presenter1.destroyed).isFalse()
    assertThat(presenter2.destroyed).isFalse()

    presenterStack.onDestroy(finishingActivity)
    assertThat(presenter1.destroyed).isTrue()
    assertThat(presenter2.destroyed).isTrue()
  }

  @Test fun dropAndDestroyRemovedPresenter() {
    presenter2.takeView(View(RuntimeEnvironment.application))
    presenterStack.update(History.single(screen1))
    assertThat(presenter2.viewDropped).isTrue()
    assertThat(presenter2.destroyed).isTrue()
    assertThat(presenterStack.currentStack()).doesNotContain(presenter2)
  }

  @Test fun onCreateOnlyForNewPresenters() {
    presenter1.onCreateCalled = false
    presenter2.onCreateCalled = false

    val screen3 = TestScreen()
    val newHistory = history.buildUpon().push(screen3).build()
    presenterStack.update(newHistory)
    val presenter3 = presenterStack.peek() as TestPresenter

    assertThat(presenter1.onCreateCalled).isFalse()
    assertThat(presenter2.onCreateCalled).isFalse()
    assertThat(presenter3.onCreateCalled).isTrue()
  }

  @Test fun restoreState() {
    val testBundle = Bundle()
    presenterStack.onSaveInstanceState(testBundle)
    presenterStack.clear()

    assertThat(presenterStack.currentStack()).isEmpty()

    presenterStack.onCreate(testBundle)
    presenterStack.update(history)

    val newPresenter1 = presenterStack.currentStack().last as TestPresenter
    val newPresenter2 = presenterStack.currentStack().first as TestPresenter

    assertThat(newPresenter1.onCreateCalledWithSavedBundle).isTrue()
    assertThat(newPresenter2.onCreateCalledWithSavedBundle).isTrue()
  }
}