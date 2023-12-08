package com.bautista.stephen.block3.p1.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bautista.stephen.block3.p1.quiz.databinding.ActivityMainBinding
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restartOutput()

//        Numbers
        binding.button0.setOnClickListener { updateOutput("0") }
        binding.button1.setOnClickListener { updateOutput("1") }
        binding.button2.setOnClickListener { updateOutput("2") }
        binding.button3.setOnClickListener { updateOutput("3") }
        binding.button4.setOnClickListener { updateOutput("4") }
        binding.button5.setOnClickListener { updateOutput("5") }
        binding.button6.setOnClickListener { updateOutput("6") }
        binding.button7.setOnClickListener { updateOutput("7") }
        binding.button8.setOnClickListener { updateOutput("8") }
        binding.button9.setOnClickListener { updateOutput("9") }

//        Symbols
        binding.buttonadd.setOnClickListener { updateOutput("+") }
        binding.buttonsub.setOnClickListener { updateOutput("-") }
        binding.buttonmult.setOnClickListener { updateOutput("*") }
        binding.buttondiv.setOnClickListener { updateOutput("/") }
        binding.buttonequal.setOnClickListener {
            if(!binding.textView.text.toString().equals("")) {
                val result = evaluateEquation(binding.textView.text.toString())
                var fresult = String.format("%.2f", result)
                if(fresult.endsWith(".00")){
                    fresult = fresult.replace(".00", "")
                }
                binding.answer.text = fresult
                restartOutput()
            }
        }

        binding.buttonres.setOnClickListener { restartOutput() }


    }
    private fun updateOutput(value : String) {
        var currentText = binding.textView.text.toString()
        when(value) {
            "+", "-", "*", "/" -> {
                try {
                    if (currentText.isNotEmpty()) {
                        val lastChar = currentText[currentText.length - 1]
                        if(lastChar.isDigit()) binding.textView.text = currentText + value
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
            else -> binding.textView.text = currentText + value
        }

    }
    private fun restartOutput() {
        binding.textView.text = ""
    }
    fun evaluateEquation(equation: String): Double {
        val context = Context.enter()
        context.optimizationLevel = -1
        val scope: Scriptable = context.initStandardObjects()
        return try {
            val script = "result = $equation;"
            context.evaluateString(scope, script, "JavaScript", 1, null)
            val result = scope.get("result", scope)

            if (result is Number) {
                result.toDouble()
            } else {
                throw IllegalArgumentException("Invalid result type: ${result?.javaClass?.simpleName}")
            }
        } finally {
            Context.exit()
        }
    }
}