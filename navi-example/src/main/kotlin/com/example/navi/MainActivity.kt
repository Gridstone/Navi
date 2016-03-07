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

package com.example.navi

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import au.com.gridstone.navi.Navi
import com.example.navi.home.HomeScreen
import flow.Flow

class MainActivity : AppCompatActivity() {
  override fun attachBaseContext(newBase: Context) {
    val (dispatcher, naviContext) = Navi.configure(newBase, this)
        .containerId(R.id.main_container)
        .segue(SlideSegue())
        .install()

    val baseContext = Flow.configure(naviContext, this)
        .dispatcher(dispatcher)
        .defaultKey(HomeScreen())
        .keyParceler(MoshiParceler())
        .install()

    super.attachBaseContext(baseContext)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_container)
  }

  override fun onBackPressed() {
    if (Navi.goBack(this)) return
    if (Flow.get(this).goBack()) return
    super.onBackPressed()
  }
}
