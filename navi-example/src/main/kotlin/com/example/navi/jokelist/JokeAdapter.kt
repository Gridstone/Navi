/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi.jokelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.navi.R

class JokeAdapter(val jokes: List<CharSequence>) : RecyclerView.Adapter<JokeAdapter.ViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.joke, parent, false) as TextView
    return ViewHolder(view)
  }

  override fun getItemCount(): Int = jokes.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindTo(jokes[position])
  }

  class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {
    fun bindTo(joke: CharSequence) {
      view.text = joke
    }
  }
}