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

import okhttp3.OkHttpClient
import retrofit2.GsonConverterFactory
import retrofit2.Result
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import retrofit2.http.GET
import rx.Observable

object ZeldaGames {
  private const val API_URL = "http://www.giantbomb.com/api/"
  private const val API_KEY = "a05c078843cb3035a3cd057e046c4230636fe704"

  private val client = OkHttpClient.Builder()
      .addInterceptor { chain ->
        val url = chain.request().url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .build()

        chain.proceed(chain.request()
            .newBuilder()
            .url(url)
            .build())
      }.build()

  val webApi = Retrofit.Builder()
      .baseUrl(API_URL)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build()
      .create(WebApi::class.java)

  interface WebApi {
    @GET("games/?filter=name:zelda&field_list=id,name,image,aliases,deck,original_release_date,platforms")
    fun games(): Observable<Result<GamesResponse>>
  }

  data class GamesResponse(val results: List<Game>)
}
