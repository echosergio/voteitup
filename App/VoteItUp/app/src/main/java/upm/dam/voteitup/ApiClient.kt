package upm.dam.voteitup

import android.support.v4.util.Pools
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.entities.Poll

object ApiClient {

    var URL: String? = null
    var JWT: String? = null

    suspend fun getAuthToken(email: String, password: String): String? {

        val (_, _, result) = Fuel
                .post("$URL/api/v1/auth/token")
                .header("Content-Type" to "application/json")
                .body("{ \"email\" : \"$email\", \"password\" : \"$password\"}")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                result.get().obj()["token"].toString()
            }
        }
    }

    suspend fun getPoll(id: Int): Poll? {

        val polls = mutableListOf<Poll>()

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls/$id")
                .header("Authorization" to "bearer $JWT")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                Gson().fromJson(result.value.obj().toString(), Poll::class.java)
            }
        }
    }

    suspend fun getPolls(): List<Poll>? {

        val polls = mutableListOf<Poll>()

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls")
                .header("Authorization" to "bearer $JWT")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                (0 until result.value.array().length()).mapTo(polls) { Gson().fromJson(result.value.array()[it].toString(), Poll::class.java) }
            }
        }
    }

    suspend fun getPolls(keyword: String): List<Poll>? {

        val polls = mutableListOf<Poll>()

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls?keyword=$keyword")
                .header("Authorization" to "bearer $JWT")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                (0 until result.value.array().length()).mapTo(polls) { Gson().fromJson(result.value.array()[it].toString(), Poll::class.java) }
            }
        }
    }

    fun submitPool(poll: Poll): Any {
        ///api/v1/users/<ID>/polls

        val poll_json = Gson().toJson(poll)
        val (_, _, result) = Fuel
                .post("$URL/api/v1/users/$poll.UserId/polls")
                .header("Content-Type" to "application/json")
                .header("Authorization" to "bearer $JWT")
                .body(poll_json)
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                false
            }
            is Result.Success -> {
                true
            }
        }
    }

}