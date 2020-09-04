/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cha.n.googlemap.util

/**
 * Extension functions and Binding Adapters.
 */

import android.app.Activity
import android.util.Log
import cha.n.googlemap.retrofit.RetrofitClient
import cha.n.googlemap.retrofit.RetrofitService

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Activity.showLog(tag:String, logText: String) {
    Log.d(tag, logText)
}

fun getRetrofitService() : RetrofitService = RetrofitClient.getInstnace().create(RetrofitService::class.java)