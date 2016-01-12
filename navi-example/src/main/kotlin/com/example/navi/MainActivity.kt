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

import android.view.ViewGroup
import au.com.gridstone.navi.appcompat.AppCompatNaviActivity
import au.com.gridstone.navi.PresenterStack
import au.com.gridstone.navi.Segue
import com.example.navi.home.HomeScreen
import flow.History
import flow.StateParceler

class MainActivity : AppCompatNaviActivity() {
  override fun getPresenterStack(): PresenterStack = getApp(this).presenterStack

  override fun getContainer(): ViewGroup {
    setContentView(R.layout.main_container)
    return findViewById(R.id.main_container) as ViewGroup
  }

  override fun getParceler(): StateParceler = GsonParceler()

  override fun getDefaultHistory(): History = History.single(HomeScreen())

  override fun getSegue(): Segue = SlideSegue()
}
