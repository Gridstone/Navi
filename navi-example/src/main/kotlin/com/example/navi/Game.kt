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

data class Game(val id: Long,
                val name: String,
                val aliases: String?,
                val deck: String,
                val image: Image,
                val original_release_date: String,
                val platforms: List<Platform>) {
  data class Platform(val name: String)
  data class Image(val small_url: String, val super_url: String)
}