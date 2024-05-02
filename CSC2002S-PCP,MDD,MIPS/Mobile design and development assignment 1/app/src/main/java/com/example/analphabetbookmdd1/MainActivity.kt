package com.example.analphabetbookmdd1

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val butA = findViewById<Button>(R.id.button)
        val butB = findViewById<Button>(R.id.button2)
        val butC = findViewById<Button>(R.id.button3)
        val butD = findViewById<Button>(R.id.button4)
        val butE = findViewById<Button>(R.id.button5)
        val butF = findViewById<Button>(R.id.button6)
        val butG = findViewById<Button>(R.id.button7)
        val butH = findViewById<Button>(R.id.button8)
        val butI = findViewById<Button>(R.id.button9)
        val butJ = findViewById<Button>(R.id.button10)
        val butK = findViewById<Button>(R.id.button11)
        val butL = findViewById<Button>(R.id.button12)
        val butM = findViewById<Button>(R.id.button13)
        val butN = findViewById<Button>(R.id.button14)
        val butO = findViewById<Button>(R.id.button15)
        val butP = findViewById<Button>(R.id.button16)
        val butQ = findViewById<Button>(R.id.button17)
        val butR = findViewById<Button>(R.id.button18)
        val butS = findViewById<Button>(R.id.button19)
        val butT = findViewById<Button>(R.id.button20)
        val butU = findViewById<Button>(R.id.button21)
        val butV = findViewById<Button>(R.id.button22)
        val butW = findViewById<Button>(R.id.button23)
        val butX = findViewById<Button>(R.id.button24)
        val butY = findViewById<Button>(R.id.button25)
        val butZ = findViewById<Button>(R.id.button26)

        butA.setOnClickListener {
            OpenLetterPage();
        }
        butB.setOnClickListener {
            OpenLetterPage();
        }
        butC.setOnClickListener {
            OpenLetterPage();
        }
        butD.setOnClickListener {
            OpenLetterPage();
        }
        butE.setOnClickListener {
            OpenLetterPage();
        }
        butF.setOnClickListener {
            OpenLetterPage();
        }
        butG.setOnClickListener {
            OpenLetterPage();
        }
        butH.setOnClickListener {
            OpenLetterPage();
        }
        butI.setOnClickListener {
            OpenLetterPage();
        }
        butJ.setOnClickListener {
            OpenLetterPage();
        }
        butK.setOnClickListener {
            OpenLetterPage();
        }
        butL.setOnClickListener {
            OpenLetterPage();
        }
        butM.setOnClickListener {
            OpenLetterPage();
        }
        butN.setOnClickListener {
            OpenLetterPage();
        }
        butO.setOnClickListener {
            OpenLetterPage();
        }
        butP.setOnClickListener {
            OpenLetterPage();
        }
        butQ.setOnClickListener {
            OpenLetterPage();
        }
        butR.setOnClickListener {
            OpenLetterPage();
        }
        butS.setOnClickListener {
            OpenLetterPage();
        }
        butT.setOnClickListener {
            OpenLetterPage();
        }
        butU.setOnClickListener {
            OpenLetterPage();
        }
        butV.setOnClickListener {
            OpenLetterPage();
        }
        butW.setOnClickListener {
            OpenLetterPage();
        }
        butX.setOnClickListener {
            OpenLetterPage();
        }
        butY.setOnClickListener {
            OpenLetterPage();
        }
        butZ.setOnClickListener {
            OpenLetterPage();
        }





    }

    private fun OpenLetterPage() {

        val intent = Intent(this, LetterPage::class.java)
        startActivity(intent)
    }



    }


