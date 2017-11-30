package upm.dam.voteitup

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.ProgressBar
import android.content.SharedPreferences
import com.github.kittinunf.fuel.android.extension.responseJson

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private var TOKEN: String = ""
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        TOKEN = getString(R.string.shared_preferences_token)

        // DEV: To reset user token
        // val editor = sharedPreferences!!.edit()
        // editor.remove(TOKEN)
        // editor.apply()

        if (sharedPreferences!!.getString(TOKEN, null) != null) {
            enterApp()
        }

        setContentView(R.layout.activity_login)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {

        var mProgressBar = findViewById<ProgressBar>(R.id.login_progress);
        var mForgotPass = findViewById<TextView>(R.id.forgotPass);

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            mForgotPass.visibility = View.GONE
            mProgressBar.visibility = View.VISIBLE

            Fuel
                    .post(getString(R.string.api_url) + "/api/v1/auth/token")
                    .header("Content-Type" to "application/json")
                    .body("{ \"email\" : \"$emailStr\", \"password\" : \"$passwordStr\"}")
                    .responseJson { _, _, result ->
                        when (result) {
                            is Result.Failure -> {
                                mProgressBar.visibility = View.GONE
                                mForgotPass.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                mProgressBar.visibility = View.GONE

                                val editor = sharedPreferences!!.edit()
                                editor.putString(TOKEN, result.get().obj()["token"].toString())
                                editor.apply()

                                enterApp()
                            }
                        }
                    }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }

    private fun enterApp(): Unit {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
