package upm.dam.voteitup

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import upm.dam.voteitup.entities.Poll
import upm.dam.voteitup.entities.Poll_POST
import upm.dam.voteitup.entities.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.SigningKeyResolverAdapter
import upm.dam.voteitup.entities.UserActivity

object ApiClient {

    var URL: String? = null
    var TOKEN: String? = null

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

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls/$id")
                .header("Authorization" to "bearer $TOKEN")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                if (result.value.content.isBlank()) { null }
                else Gson().fromJson(result.value.obj().toString(), Poll::class.java)
            }
        }
    }

    suspend fun getPolls(): List<Poll>? {

        val polls = mutableListOf<Poll>()

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls")
                .header("Authorization" to "bearer $TOKEN")
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
                .header("Authorization" to "bearer $TOKEN")
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

        val poll_json = Gson().toJson(poll)
        val userId = getCurrentUserId()

        val (_, _, result) = Fuel
                .post("$URL/api/v1/users/$userId/polls")
                .header("Content-Type" to "application/json")
                .header("Authorization" to "bearer $TOKEN")
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

    suspend fun getUser(id: Int): User? {

        val (_, _, result) = Fuel
                .get("$URL/api/v1/users/$id")
                .header("Authorization" to "bearer $TOKEN")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                if (result.value.content.isBlank()) { null }
                else Gson().fromJson(result.value.obj().toString(), User::class.java)
            }
        }
    }

    suspend fun submitUser(user: User): Any {

        val user_json = Gson().toJson(user)

        val (_, _, result) = Fuel
                .post("$URL/api/v1/users/")
                .header("Content-Type" to "application/json")
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

    suspend fun getUserActivity(id: Int): List<UserActivity>? {

        val userActivities = mutableListOf<UserActivity>()

        val (_, _, result) = Fuel
                .get("$URL/api/v1/users/$id/activity")
                .header("Authorization" to "bearer $TOKEN")
                .responseJson()

        return when (result) {
            is Result.Failure -> {
                null
            }
            is Result.Success -> {
                (0 until result.value.array().length()).mapTo(userActivities) { Gson().fromJson(result.value.array()[it].toString(), UserActivity::class.java) }
            }
        }
    }

    fun getCurrentUserId(): Int {
        return decodeToken(TOKEN!!)!!["id"].toString().toInt();
    }

    private fun decodeToken(token: String): Claims? {
        var tokenClaims: Claims? = null

        try {
            // This hacky thing is to allow getting the claims without caring about the signature.
            Jwts.parser().setSigningKeyResolver(object : SigningKeyResolverAdapter() {
                override fun resolveSigningKeyBytes(header: JwsHeader<*>?, claims: Claims?): ByteArray {
                    tokenClaims = claims
                    return "0".toByteArray()
                }
            }).parseClaimsJws(token)
        } catch (e: SignatureException) {
            e.printStackTrace()
        }

        return tokenClaims
    }

    fun getAllPolls(): MutableList<Poll> {

        val (_, _, result) = Fuel
                .get("$URL/api/v1/polls")
                .header("Authorization" to "bearer $TOKEN")
                .responseJson()
        val polls = mutableListOf<Poll>()

        return when (result) {
            is Result.Failure -> {
                polls
            }
            is Result.Success -> {
                if (result.value.content.isBlank()) { polls }
                else (0 until result.value.array().length()).mapTo(polls)
                    { Gson().fromJson(result.value.array()[it].toString(), Poll::class.java) }
            }
        }

    }
}