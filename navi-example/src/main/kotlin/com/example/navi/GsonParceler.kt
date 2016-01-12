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
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import flow.StateParceler
import java.io.StringReader
import java.io.StringWriter

class GsonParceler : StateParceler {
  val gson: Gson = Gson()

  override fun wrap(instance: Any): Parcelable = Wrapper(encode(instance))

  override fun unwrap(parcelable: Parcelable): Any {
    val wrapper = parcelable as Wrapper
    return decode(wrapper.json)
  }

  private fun encode(instance: Any): String {
    val stringWriter = StringWriter()
    val writer = JsonWriter(stringWriter)
    val type = instance.javaClass

    writer.beginObject()
    writer.name(type.name)
    gson.toJson(instance, type, writer)
    writer.endObject();

    return stringWriter.toString()
  }

  private fun decode(json: String): Any {
    val reader = JsonReader(StringReader(json))
    reader.beginObject()
    val type = Class.forName(reader.nextName())
    return gson.fromJson(reader, type)
  }

  private data class Wrapper(val json: String) : Parcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
      dest.writeString(json)
    }

    override fun describeContents(): Int = 0

    companion object {
      val CREATOR = object : Parcelable.Creator<Wrapper> {
        override fun newArray(size: Int): Array<Wrapper?> = arrayOfNulls(size)

        override fun createFromParcel(source: Parcel): Wrapper = Wrapper(source.readString())
      }
    }
  }
}
