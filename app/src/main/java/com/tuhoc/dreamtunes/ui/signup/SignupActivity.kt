package com.tuhoc.dreamtunes.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tuhoc.dreamtunes.bases.BaseActivity
import com.tuhoc.dreamtunes.databinding.ActivityLoginBinding
import com.tuhoc.dreamtunes.databinding.ActivitySignupBinding
import com.tuhoc.dreamtunes.ui.login.LoginActivity
import com.tuhoc.dreamtunes.ui.login.LoginViewModel

class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {
    private lateinit var signupViewModel: SignupViewModel

    override fun observerData() {
        super.observerData()
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]
    }

    override fun initView() {
        super.initView()

    }

    override fun handleEvent() {
        super.handleEvent()

        onClick()
    }

    private fun onClick() {
        binding.btnSignup.setOnClickListener {
            signUp()
        }

        binding.tvLogin.setOnClickListener {
            logIn()
        }
    }

    private fun signUp() {
        val userName = binding.edtFullName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirm = binding.edtConfirm.text.toString()

        val regEmail = ".*@gmail\\.com$".toRegex()
        val regPassword = ".{6,}$".toRegex()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            if (password == confirm) {
                signupViewModel.checkEmailExistence(email) { emailExists, message ->
                    if (emailExists) {
                        signupViewModel.signUp(userName, email, password) { success, message ->
                            binding.progressBar.visibility = View.GONE
                            if (regEmail.matches(email) && regPassword.matches(password)) {
                                if (success) {
                                    Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@SignupActivity, "Please enter the correct format", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@SignupActivity, "Reconfirm", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@SignupActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
        }
    }


    private fun logIn() {
        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}