package com.tuhoc.dreamtunes.ui.login

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tuhoc.dreamtunes.MainActivity
import com.tuhoc.dreamtunes.bases.BaseActivity
import com.tuhoc.dreamtunes.databinding.ActivityLoginBinding
import com.tuhoc.dreamtunes.manager.LoginManager.checkUserLoggedIn
import com.tuhoc.dreamtunes.manager.LoginManager.setLoggedIn
import com.tuhoc.dreamtunes.ui.signup.SignupActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var loginViewModel: LoginViewModel

    override fun observerData() {
        super.observerData()
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun initView() {
        super.initView()
        loginViewModel.fetchUsers()
    }

    override fun handleEvent() {
        super.handleEvent()

        onClick()
    }

    override fun onStart() {
        super.onStart()
        val isLoggedIn = checkUserLoggedIn(this)
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onClick() {
        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.tvSignup.setOnClickListener {
            signUp()
        }

        binding.tvForgot.setOnClickListener {
            forgot()
        }
    }

    private fun logIn() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        val regEmail = ".*@gmail\\.com$".toRegex()
        val regPassword = ".{6,}$".toRegex()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            loginViewModel.logIn(email, password) { success, message, user ->
                binding.progressBar.visibility = View.GONE
                if (regEmail.matches(email) && regPassword.matches(password)) {
                    if (success) {
                        // set trạng thái đăng nhập
                        user?.let { setLoggedIn(this, true, it) }

                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Please enter the correct format", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        finish()
    }

    private fun forgot() {
        val email = binding.edtEmail.text.toString()
        val regEmail = ".*@gmail\\.com$".toRegex()

        if (email.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            loginViewModel.forgetPassword(email) { success, message ->
                binding.progressBar.visibility = View.GONE
                if (regEmail.matches(email)) {
                    if (success) {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Please enter the correct format", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
        }
    }
}