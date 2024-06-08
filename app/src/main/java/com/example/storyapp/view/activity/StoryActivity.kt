package com.example.storyapp.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.data.preference.UserPreference
import com.example.storyapp.view.adapter.LoadingStateAdapter
import com.example.storyapp.view.model.ViewModelFactory
import com.example.storyapp.view.adapter.StoryAdapter
import com.example.storyapp.view.model.StoryViewModel
import com.example.submissionintermediate.R
import com.example.submissionintermediate.databinding.ActivityStoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryActivity : AppCompatActivity(){
    private lateinit var binding: ActivityStoryBinding
    private lateinit var preferences: UserPreference
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var factory: ViewModelFactory
    private val viewModel: StoryViewModel by viewModels {
        factory
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_DATA = "extra_data"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        factory = ViewModelFactory.getInstance(this)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserPreference(this)

        setupView()
        setupAction()

        storyAdapter = StoryAdapter()
        binding.listStory.layoutManager = LinearLayoutManager(this)
        binding.listStory.setHasFixedSize(true)
        binding.listStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                storyAdapter.retry()
            }
        )

        if (!isUserLoggedIn()) {
            navigateToLogin()
        }

        setStory()
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
        binding.logoutButton.setOnClickListener {
            preferences.setToken(null)
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.maps -> {
                    val moveIntent = Intent(this@StoryActivity, MapsActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                R.id.upload -> {
                    val moveIntent = Intent(this@StoryActivity, UploadStoryActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setStory() {
        val token = preferences.getToken() ?: ""
        val userToken = "Bearer $token"

        GlobalScope.launch(Dispatchers.IO) {
            val storyResponse = viewModel.stories(userToken)
            withContext(Dispatchers.Main) {
                storyResponse.observe(this@StoryActivity, Observer { storyData ->
                    storyAdapter.submitData(lifecycle, storyData)
                })
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val token = preferences.getToken()
        return token != null && token.isNotEmpty()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}
