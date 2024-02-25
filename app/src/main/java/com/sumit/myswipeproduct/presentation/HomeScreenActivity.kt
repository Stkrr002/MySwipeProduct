package com.sumit.myswipeproduct.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sumit.myswipeproduct.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}