package com.rudyrachman16.skripsi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rudyrachman16.skripsi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }
}