package com.example.storyapp.view.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import coil.load
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.Story
import com.example.submissionintermediate.R
import com.example.submissionintermediate.databinding.ActivityStoryDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    companion object {
        const val EXTRA_STORY_ID = "story_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detailStory = intent.getParcelableExtra<Story>(EXTRA_STORY_ID) as Story

        setupView()
        setupAction()
        detailData(detailStory)
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
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.story -> {
                    val moveIntent = Intent(this@StoryDetailActivity, StoryActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                R.id.maps -> {
                    val moveIntent = Intent(this@StoryDetailActivity, MapsActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                R.id.upload -> {
                    val moveIntent = Intent(this@StoryDetailActivity, UploadStoryActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                else -> false
            }
        }
    }

    private fun detailData(detailStory: Story) {
        Glide.with(this@StoryDetailActivity)
            .load(detailStory.photoUrl)
            .fitCenter()
            .into(binding.detailImage)

        detailStory.apply {
            binding.nameTextView.text = name
            binding.captionTextView.text = description
        }
    }
}
