package com.example.tiktaktoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktaktoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Foydalanuvchi X bilan boshlashni tanlasa
        binding.exBn.setOnClickListener {
            startGame(isPlayerStarting = true) // Foydalanuvchi boshlaydi
        }

        // Kompyuter O bilan boshlashni tanlasa
        binding.zeroBn.setOnClickListener {
            startGame(isPlayerStarting = false) // Kompyuter boshlaydi
        }
    }

    /**
     * O'yinni boshlash
     * @param isPlayerStarting: agar true bo'lsa, foydalanuvchi boshlaydi. Aks holda kompyuter.
     */
    private fun startGame(isPlayerStarting: Boolean) {
        val intent = Intent(this, GameGridActivity::class.java)
        intent.putExtra("isPlayerStarting", isPlayerStarting)
        startActivity(intent)
    }
}
