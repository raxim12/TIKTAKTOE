package com.example.tiktaktoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktaktoe.databinding.ActivityGameGridBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class GameGridActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameGridBinding
    private val board = Array(3) { Array(3) { "" } }
    private val boxIds = listOf(
        R.id.box1, R.id.box2, R.id.box3,
        R.id.box4, R.id.box5, R.id.box6,
        R.id.box7, R.id.box8, R.id.box9
    )
    private val boxMap = mutableMapOf<Int, Pair<Int, Int>>()
    private var isPlayerXTurn = true // Kim boshlashini aniqlash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameGridBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeBoxMap()
        setupBoxClickListeners()

        // MainActivity'dan kelgan parametrni olish
        val isPlayerStarting = intent.getBooleanExtra("isPlayerStarting", true)
        if (!isPlayerStarting) {
            isPlayerXTurn = false
            computerMove() // Kompyuter boshlaydi
        }
    }

    private fun initializeBoxMap() {
        boxMap[R.id.box1] = 0 to 0
        boxMap[R.id.box2] = 0 to 1
        boxMap[R.id.box3] = 0 to 2
        boxMap[R.id.box4] = 1 to 0
        boxMap[R.id.box5] = 1 to 1
        boxMap[R.id.box6] = 1 to 2
        boxMap[R.id.box7] = 2 to 0
        boxMap[R.id.box8] = 2 to 1
        boxMap[R.id.box9] = 2 to 2
    }

    private fun setupBoxClickListeners() {
        for (boxId in boxIds) {
            val box = findViewById<LinearLayout>(boxId)
            box.setOnClickListener { onBoxClicked(boxId) }
        }
    }

    private fun onBoxClicked(boxId: Int) {
        if (!isPlayerXTurn) return // Agar foydalanuvchi yurishi navbati bo'lmasa

        val (row, col) = boxMap[boxId] ?: return
        if (board[row][col].isNotEmpty()) return

        board[row][col] = "Player"
        val box = findViewById<LinearLayout>(boxId)
        box.setBackgroundResource(R.drawable.x_bg)
        box.findViewById<ImageView>(getXImageViewId(boxId)).visibility = View.VISIBLE

        if (checkWinner("Player")) {
            showWinnerBottomSheet("Player")
            return
        }

        isPlayerXTurn = false
        computerMove()
    }

    private fun computerMove() {
        val emptyBoxes = boxIds.filter { id ->
            val (row, col) = boxMap[id] ?: return@filter false
            board[row][col].isEmpty()
        }

        if (emptyBoxes.isEmpty()) {
            showDrawBottomSheet()
            return
        }

        val randomBoxId = emptyBoxes.random()
        val (row, col) = boxMap[randomBoxId] ?: return

        board[row][col] = "Computer"
        val box = findViewById<LinearLayout>(randomBoxId)
        box.setBackgroundResource(R.drawable.o_bg)
        box.findViewById<ImageView>(getOImageViewId(randomBoxId)).visibility = View.VISIBLE

        if (checkWinner("Computer")) {
            showWinnerBottomSheet("Computer")
            return
        }

        isPlayerXTurn = true
    }

    private fun getXImageViewId(boxId: Int): Int {
        return resources.getIdentifier(
            "box${boxIds.indexOf(boxId) + 1}_x_imageView",
            "id",
            packageName
        )
    }

    private fun getOImageViewId(boxId: Int): Int {
        return resources.getIdentifier(
            "box${boxIds.indexOf(boxId) + 1}_o_imageView",
            "id",
            packageName
        )
    }

    private fun checkWinner(player: String): Boolean {
        for (i in 0 until 3) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty()) return true
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty()) return true
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty()) return true
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty()) return true

        return false
    }

    private fun showWinnerBottomSheet(winner: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_winner, null)
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.winnerTextView).text = "$winner Won!"
        view.findViewById<Button>(R.id.restartButton).setOnClickListener {
            dialog.dismiss()
            resetGame()
        }

        dialog.show()
    }

    private fun showDrawBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_draw, null)
        dialog.setContentView(view)

        view.findViewById<Button>(R.id.restartButton).setOnClickListener {
            dialog.dismiss()
            resetGame()
        }

        dialog.show()
    }

    private fun resetGame() {
        board.forEach { row -> row.fill("") }
        boxIds.forEach { id ->
            val box = findViewById<LinearLayout>(id)
            box.setBackgroundResource(R.drawable.box_bg)
            box.findViewById<ImageView>(getXImageViewId(id)).visibility = View.GONE
            box.findViewById<ImageView>(getOImageViewId(id)).visibility = View.GONE
        }
        isPlayerXTurn = true
    }
}
