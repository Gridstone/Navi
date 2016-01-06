/*
 * Copyright (C) GRIDSTONE 2016
 */

package com.example.navi

import com.example.navi.model.JokesResponse
import retrofit2.GsonConverterFactory
import retrofit2.Result
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

object ChuckNorrisApi {
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
    @GET("jokes/?exclude=[$CATEGORY_NERDY, $CATEGORY_EXPLICIT]")
    fun uncategorisedJokes(): Observable<Result<JokesResponse>>

    @GET("jokes/?limitTo[{category}]")
    fun categorisedJokes(@Path("category") category: String): Observable<Result<JokesResponse>>
  }
}
