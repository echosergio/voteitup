package upm.dam.voteitup.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.ProgressBar
import android.content.SharedPreferences
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.Validators
import upm.dam.voteitup.entities.User

class LoginActivity : AppCompatActivity() {

    private var TOKEN: String = ""
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        TOKEN = getString(R.string.shared_preferences_token)

        ApiClient.URL = resources.getString(R.string.api_url)

        // DEV: To reset user token
        // val editor = sharedPreferences!!.edit()
        // editor.remove(TOKEN)
        // editor.apply()

        val token = sharedPreferences!!.getString(TOKEN, null)

        if (token != null) {
            ApiClient.TOKEN = token
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
        email_register_button.setOnClickListener{ goToRegistration() }

        val user_email = intent.getStringExtra(USER_EMAIL)
        val user_pass = intent.getStringExtra(PASS)

        if(user_email != null && user_pass != null){
            email.setText(user_email)
            password.setText(user_pass)
            this.attemptLogin();
        }
    }

    private fun goToRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun attemptLogin() {

        val mProgressBar = findViewById<ProgressBar>(R.id.login_progress);
        val mForgotPass = findViewById<TextView>(R.id.forgotPass);

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !Validators().isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!Validators().isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            mForgotPass.visibility = View.GONE
            mProgressBar.visibility = View.VISIBLE

            val getAuthTokenAsync = async { ApiClient.getAuthToken(emailStr, passwordStr) }

            launch(UI) {
                val token = getAuthTokenAsync.await()

                if (token == null){
                    mProgressBar.visibility = View.GONE
                    mForgotPass.visibility = View.VISIBLE
                } else {
                    mProgressBar.visibility = View.GONE

                    ApiClient.TOKEN = token

                    val editor = sharedPreferences!!.edit()
                    editor.putString(TOKEN, token)
                    editor.apply()

                    enterApp()
                }
            }
        }
    }

    private fun enterApp(): Unit {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {

        val USER_EMAIL = "user_email"
        val PASS = "pass"

        fun newIntent(context: Context, user: User): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(USER_EMAIL, user.email)
            intent.putExtra(PASS, user.password)
            return intent
        }
    }
}
