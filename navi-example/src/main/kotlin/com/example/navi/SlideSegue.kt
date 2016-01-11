/*
 * Copyright (C) GRIDSTONE 2016
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