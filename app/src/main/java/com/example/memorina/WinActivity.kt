package com.example.memorina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class WinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)
    }

    fun onRestartGame(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}