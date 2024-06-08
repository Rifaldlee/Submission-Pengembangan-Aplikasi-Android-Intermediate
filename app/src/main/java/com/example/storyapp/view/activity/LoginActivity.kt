package com.example.storyapp.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.data.Result
import com.example.storyapp.view.model.ViewModelFactory
import com.example.storyapp.view.model.LoginViewModel
import com.example.submissionintermediate.databinding.ActivityLoginBinding
import com.example.submissionintermediate.R

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: UserPreference
    private lateinit var factory: ViewModelFactory
    private var animationPlayed = false
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserPreference(this)

        setupView()
        setupAction()
        playAnimation()

        if (!animationPlayed) {
            playAnimation()
            animationPlayed = true
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isBlank()) {
                binding.emailEditText.error = getString(R.string.error_empty_email)
            }
            else if (password.isBlank()) {
                binding.passwordEditText.error = getString(R.string.error_empty_password)
            }
            else if (password.length < 8){
                binding.passwordEditText.error = getString(R.string.error_password_more_7)
            }
            else{
                viewModel.login(email, password).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                val data = result.data
                                preferences.setToken(data.loginResult.token)
                                val intent = Intent(this@LoginActivity, StoryActivity::class.java)
                                intent.putExtra(StoryActivity.EXTRA_DATA, data.loginResult.token)
                                showLoading(false)
                                startActivity(intent)
                                finish()}
                            is Result.Error -> {
                                Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                                showLoading(false)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 0f, 1f).apply {
            duration = 3000
        }.start()
        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login =
            ObjectAnimator.ofFloat(binding.signinButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}