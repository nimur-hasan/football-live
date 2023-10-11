package com.nehalappstudio.footballlive.internet

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MyApiClient(context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun fetchDataFromApi(url: String, callback: (String?) -> Unit) {
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener<JSONObject> { response ->
                // Handle the successful response here
                val responseData = response.toString()
                callback(responseData)
            },
            Response.ErrorListener { error ->
                // Handle errors here
                callback(null)
            }
        )

        requestQueue.add(request)
    }
}
