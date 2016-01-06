/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import flow.Flow
import flow.FlowDelegate.NonConfigurationInstance
import flow.History
import flow.StateParceler

abstract class AppCompatNaviActivity : AppCompatActivity() {
  lateinit var naviDelegate: NaviDelegate

  abstract fun getPresenterStack(): PresenterStack
  abstract fun getContainer(): ViewGroup
  abstract fun getParceler(): StateParceler
  abstract fun getDefaultHistory(): History

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val nonConfig = lastCustomNonConfigurationInstance as NonConfigurationInstance?

    naviDelegate = onCreateForNavi(
        getContainer(),
        getPresenterStack(),
        savedInstanceState,
        nonConfig,
        intent,
        getParceler(),
        getDefaultHistory()
    )
  }

  override fun onResume() {
    super.onResume()
    naviDelegate.onResume()
  }

  override fun onPause() {
    super.onPause()
    naviDelegate.onPause()
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    naviDelegate.onNewIntent(intent)
  }

  override fun onBackPressed() {
    if (naviDelegate.onBackPressed()) return
    super.onBackPressed()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    naviDelegate.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    naviDelegate.onDestroy(this)
  }

  override fun onRetainCustomNonConfigurationInstance() = naviDelegate.onRetainCustomNonConfigurationInstance()

  override fun getSystemService(name: String): Any {
    if (Flow.isFlowSystemService(name)) {
      return naviDelegate.getFlowSystemService(name)
    }

    return super.getSystemService(name)
  }
}