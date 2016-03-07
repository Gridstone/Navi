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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import flow.Direction

private const val FADE_DURATION = 150L

class CrossFadeSegue : Segue {
  override fun createAnimation(from: View, to: View, direction: Direction): Animator {
    from.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    to.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    to.alpha = 0f

    val fromAnim = ObjectAnimator.ofFloat(from, View.ALPHA, 0f)
    val toAnim = ObjectAnimator.ofFloat(to, View.ALPHA, 1f)

    fromAnim.duration = FADE_DURATION
    toAnim.duration = FADE_DURATION

    val set = AnimatorSet()
    set.playSequentially(fromAnim, toAnim)
    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
        from.setLayerType(View.LAYER_TYPE_NONE, null)
        to.setLayerType(View.LAYER_TYPE_NONE, null)
      }
    })

    return set
  }
}