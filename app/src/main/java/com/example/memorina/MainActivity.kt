package com.example.memorina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var firstCard: View
    var openCardsCount = 0
    var winCheck = 0
    val cardsNum = 8
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.toFloat() // единичный веc

        var tags = arrayOf<String>()
        var tagIndex = 0
        for (i in 0 until cardsNum*2) {
            if (i % 2 == 0) {
                tagIndex += 1
            }
            tags += "kuromi_$tagIndex"
        }
        tags.shuffle()

        val rows = Array(cardsNum/2) { LinearLayout(applicationContext) }

        tagIndex = 0

        for (row in rows.indices) {
            for (i in 1..cardsNum/2) {
                rows[row].addView(
                    ImageView(applicationContext).apply {
                        setImageResource(R.drawable.back20)
                        layoutParams = params
                        tag = tags[tagIndex]
                        setOnClickListener(colorListener)
                    })
                tagIndex += 1

            }
            layout.addView(rows[row])
        }
        setContentView(layout)
    }

    val colorListener = View.OnClickListener() {
        val tag = it.tag.toString()
        val resID = resources.getIdentifier(tag, "drawable", packageName)
        when (openCardsCount) {
            0 -> {
                firstCard = it; openCardsCount++; it.isClickable = false
                GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(it, resID, 0) }
            }
            1 -> {
                GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(it, resID, 0) }
                if (it.tag == firstCard.tag) {
                    GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(it, resID, 2) }
                    GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(firstCard, resID, 2) }
                    winCheck++
                } else {
                    GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(it, resID, 1) }
                    GlobalScope.launch(Dispatchers.Main) { turnCardWithDelay(firstCard, resID, 1) }
                    it.visibility = View.VISIBLE; it.isClickable = true
                    firstCard.visibility = View.VISIBLE; firstCard.isClickable = true
                }
                openCardsCount = 0
                if (winCheck == cardsNum) {
                    val intent = Intent(this, WinActivity::class.java)
                    startActivity(intent)
                }
            }
            else -> Log.d("mytag", "two cards are open already")
        }
    }

    suspend fun turnCardWithDelay(v: View, resID: Int, command: Int) {
        val imageView = v as ImageView
        when (command) {
            0 -> {
                imageView.setImageResource(resID)
            }
            1 -> {
                delay(1000)
                imageView.setImageResource(R.drawable.back20)
            }
            2 -> {
                delay(1000)
                imageView.visibility = View.INVISIBLE; imageView.isClickable = false
            }
            else -> {
                Log.d("mytag", "Unknown command")
            }
        }
    }
}