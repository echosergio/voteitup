package upm.dam.voteitup.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_create_poll.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import upm.dam.voteitup.Validators
import upm.dam.voteitup.entities.User

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        email_register_button.setOnClickListener { attemptCreateUser() }
        password.setOnClickListener { password.text.clear() }
    }

    private fun attemptCreateUser() {
        //areValid
        if (!validateRegistration().first) {
            validateRegistration().second!!.requestFocus()
            return
        }

        //get info
        val submit_user = User(
                email = email.text.toString(),
                password = password.text.toString(),
                username = user_name.text.toString(),
                bio = txtBio.text.toString())

        //save user.
        val mProgressBar = findViewById<ProgressBar>(R.id.registration_progress);
        mProgressBar.visibility = View.VISIBLE

        val result = async { ApiClient.submitUser(submit_user) }
        launch(UI) {
            val result = result.await()
            mProgressBar.visibility = View.GONE
            if (result == true) {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(LoginActivity.USER_EMAIL, email.text.toString())
        intent.putExtra(LoginActivity.PASS, password.text.toString())
        startActivity(intent)
        finish()
    }

    private fun validateRegistration(): Pair<Boolean, View?> {

        // Reset errors.
        user_name.error = null
        password.error = null
        email.error = null

        var cancel = false
        var focusView: View? = null


        if (TextUtils.isEmpty(user_name.text)) {
            txtBox_desc.error = getString(R.string.error_field_required)
            focusView = user_name
            cancel = true
        }
        if (TextUtils.isEmpty(password.text) || !Validators().isPasswordValid(password.text.toString())) {
            txtBox_desc.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }

        if (TextUtils.isEmpty(email.text)) {
            txtBox_desc.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!Validators().isEmailValid(email.text.toString())) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        return Pair(!cancel, focusView)
    }


}
