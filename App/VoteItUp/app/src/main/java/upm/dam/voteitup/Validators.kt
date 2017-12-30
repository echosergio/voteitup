package upm.dam.voteitup

/**
 * Created by goncer on 29/12/17.
 */
class Validators {

    fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}