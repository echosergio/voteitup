package upm.dam.voteitup

import android.support.v4.util.Pools
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.entities.Poll
import upm.dam.voteitup.entities.Poll_POST
import upm.dam.voteitup.entities.User

object ApiClient {

    var URL: String? = null
    var JWT: String? = null
    var User_Id:String = "1"

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
                if (result.value.content.isNullOrBlank()) { null }
                else Gson().fromJson(result.value.obj().toString(), Poll::class.java)
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

    fun submitPool(poll: Poll_POST): Any {
        ///api/v1/users/<ID>/polls

        val poll_json = Gson().toJson(poll)
        val (_, _, result) = Fuel
                .post("$URL/api/v1/users/$User_Id/polls")
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

    fun submitUser(submit_user: User): Any {
        //Todo: Fix the back to be able to perform the registration without token! :O
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.kHZQ03yhLOPC1c7f6CdItQbT2ljvMQLbucdJVkqwEKs"
        val user_json = Gson().toJson(submit_user)
        val (_, _, result) = Fuel
                .post("$URL/api/v1/users/")
                .header("Content-Type" to "application/json")
                .header("Authorization" to "bearer $token")
                .body(user_json)
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