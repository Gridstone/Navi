/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import flow.Flow

private const val FADE_DURATION = 150L

class CrossFadeSegue : Segue {
  override fun createAnimation(from: View, to: View, direction: Flow.Direction): Animator {
    from.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    to.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    to.alpha = 0f

    val fromAnim = ObjectAnimator.ofFloat(from, View.ALPHA, 0f)
    val toAnim = ObjectAnimator.ofFloat(to, View.ALPHA, 1f)

    fromAnim.setDuration(FADE_DURATION)
    toAnim.setDuration(FADE_DURATION)

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