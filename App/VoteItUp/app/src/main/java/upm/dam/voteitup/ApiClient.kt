package upm.dam.voteitup

import android.view.View
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import upm.dam.voteitup.entities.Poll

object ApiClient {

    private val APIURL: String = "https://polar-oasis-43680.herokuapp.com"
    private var APIKEY: String = ""

    fun setApiKey(apiKey: String) {
        APIKEY = apiKey
    }

    fun getPolls(): List<Poll>? {
        return null
    }

}