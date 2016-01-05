/*
 * Copyright (C) GRIDSTONE 2016
 */

package au.com.gridstone.navi

import android.animation.Animator
import android.view.View
import flow.Flow

interface Segue {
  fun createAnimation(from: View, to: View, direction: Flow.Direction): Animator
}
