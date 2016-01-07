/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi

import com.example.navi.model.JokeListResponse
import com.example.navi.model.JokeResponse
import retrofit2.GsonConverterFactory
import retrofit2.Result
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

object ChuckNorrisJokes {
  private const val API_URL = "http://api.icndb.com"

  const val CATEGORY_NERDY = "nerdy"
  const val CATEGORY_EXPLICIT = "explicit"

  val webApi = Retrofit.Builder()
      .baseUrl(API_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build()
      .create(Api::class.java)

  interface Api {
    @GET("/jokes/?exclude=[$CATEGORY_NERDY, $CATEGORY_EXPLICIT]")
    fun uncategorisedJokes(): Observable<Result<JokeListResponse>>

    @GET("/jokes")
    fun jokes(@Query("limitTo") limitTo: String? = null): Observable<Result<JokeListResponse>>

    @GET("jokes/random?limitTo=[$CATEGORY_NERDY]")
    fun randomNerdyJoke(): Observable<Result<JokeResponse>>
  }
}
