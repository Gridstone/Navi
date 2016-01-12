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

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import au.com.gridstone.navi.Segue
import flow.Flow

class SlideSegue : Segue {
  val interpolator = FastOutSlowInInterpolator()

  override fun createAnimation(from: View, to: View, direction: Flow.Direction): Animator {
    val backward = direction == Flow.Direction.BACKWARD
    val fromTranslation = if (backward) from.width else -from.width
    val toTranslation = if (backward) -to.width else to.width

    val set = AnimatorSet()

    val fromAnim = ObjectAnimator.ofFloat(from, View.TRANSLATION_X, fromTranslation.toFloat())
    val toAnim = ObjectAnimator.ofFloat(to, View.TRANSLATION_X, toTranslation.toFloat(), 0f)
    fromAnim.interpolator = interpolator
    toAnim.interpolator = interpolator

    set.play(fromAnim)
    set.play(toAnim)

    return set
  }
}