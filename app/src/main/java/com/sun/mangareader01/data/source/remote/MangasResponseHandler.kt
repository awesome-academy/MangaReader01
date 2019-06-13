package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.MangasResponse
import org.json.JSONObject

class MangasResponseHandler : DataResponseHandler<MangasResponse> {
    override fun parseToObject(jsonString: String) = MangasResponse(JSONObject(jsonString))
}
