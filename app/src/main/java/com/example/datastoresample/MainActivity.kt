package com.example.datastoresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.datastoresample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appPreferences: AppPreferences
    private lateinit var bookmarkDataStore: BookmarkDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferencesDataStoreUsage()
        //protoDataStoreUsage()
    }

    private fun preferencesDataStoreUsage() {
        appPreferences = AppPreferences(this)

        binding.btnBookmark.setOnClickListener {
            val bookmark = binding.etBookmark.text.toString().trim()
            lifecycleScope.launch {
                appPreferences.saveBookmark(bookmark)
            }
        }

        appPreferences.bookmark.asLiveData().observe(this, {
            binding.tvCurrentBookmarkValue.text = it
        })
    }

    private fun protoDataStoreUsage() {
        bookmarkDataStore = BookmarkDataStore(this)

        binding.btnBookmark.setOnClickListener {
            val bookmark = binding.etBookmark.text.toString().trim()
            lifecycleScope.launch {
                bookmarkDataStore.saveBookmark(bookmark)
            }
        }

        bookmarkDataStore.bookmark.asLiveData()
            .observe(this, { binding.tvCurrentBookmarkValue.text = it })
    }
}