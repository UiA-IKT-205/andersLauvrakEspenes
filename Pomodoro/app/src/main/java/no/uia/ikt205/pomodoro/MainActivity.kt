package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import no.uia.ikt205.pomodoro.util.descriptiveTimeToMilliseconds
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    private lateinit var coutdownDisplay:TextView


    var timeToCountDownInMs = 0L // Contains value to countdown from
    val timeTicks = 1000L           // View update frequency


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById(R.id.startCountdownButton)
       startButton.setOnClickListener {
           if(startButton.text == getString(R.string.startButtonText))
               startCountDown()
           else
               stopCountDown()
       }

        // Find countdown buttons and set onClick listeners
        val buttonList = arrayOf(btn30min, btn60min, btn90min, btn120min)
        buttonList.forEach { it.setOnClickListener(this) }

       coutdownDisplay = findViewById(R.id.countDownView)

    }

    // Starts the countdown
    private fun startCountDown() {

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                startButton.text = getString(R.string.startButtonText)
                cancel() // Stop timer
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }

        Log.d("Timer", "Timer was started")
        startButton.text = getString(R.string.stopButtonText) // Update button text
        timer.start() // Start the timer

    }

    private fun stopCountDown() {
        Log.d("Timer", "Timer was canceled")
        timer.cancel()
        startButton.text = getString(R.string.startButtonText)
        updateTimeToCountDownInMs(descriptiveTimeToMilliseconds(coutdownDisplay.text as String) + 100)


    }

    // Updates the countdown text
    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    // Handles onclick events by id from add time buttons
    override fun onClick(v:View?) {
        when(v?.id) {
            R.id.btn30min -> updateTimeToCountDownInMs(1800000L)
            R.id.btn60min -> updateTimeToCountDownInMs(3600000L)
            R.id.btn90min -> updateTimeToCountDownInMs(5400000L)
            R.id.btn120min -> updateTimeToCountDownInMs(7200000L)
        }
    }

    // Update the value to count down from
    private fun updateTimeToCountDownInMs(ms:Long) {
        if(startButton.text == getString(R.string.startButtonText)) {
            timeToCountDownInMs = ms
            updateCountDownDisplay(timeToCountDownInMs)
        }
    }


}