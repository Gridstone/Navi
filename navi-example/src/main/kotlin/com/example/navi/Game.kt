/*
 * Copyright (C) GRIDSTONE 2016
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