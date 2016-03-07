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

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Moshi
import flow.KeyParceler

class MoshiParceler() : KeyParceler {
  val moshi = Moshi.Builder().build()

  override fun toParcelable(instance: Any): Parcelable {
    val type = instance.javaClass
    val adapter = moshi.adapter(type)
    val json = adapter.toJson(instance)
    return Wrapper(type, json)
  }

  override fun toKey(parcelable: Parcelable): Any {
    val wrapper = parcelable as Wrapper
    val adapter = moshi.adapter(wrapper.type)
    return adapter.fromJson(wrapper.json)
  }

  private data class Wrapper(val type: Class<*>, val json: String) : Parcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
      dest.writeString(type.name)
      dest.writeString(json)
    }

    override fun describeContents() = 0

    companion object {
      @JvmField val CREATOR: Parcelable.Creator<Wrapper> = object : Parcelable.Creator<Wrapper> {
        override fun createFromParcel(source: Parcel): Wrapper {
          val className = source.readString()
          val type = Class.forName(className)
          val json = source.readString()
          return Wrapper(type, json)
        }

        override fun newArray(size: Int): Array<Wrapper?> = arrayOfNulls(size)
      }
    }
  }
}