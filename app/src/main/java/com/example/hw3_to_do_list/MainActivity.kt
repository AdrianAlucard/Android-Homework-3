package com.example.hw3_to_do_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addToDoClick(view: View) {
        val secondActivity = Intent(this, SecondActivity::class.java)
        startActivity(secondActivity)
    }
}
