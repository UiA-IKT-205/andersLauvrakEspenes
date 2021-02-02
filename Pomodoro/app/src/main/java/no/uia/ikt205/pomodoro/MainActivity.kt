package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import no.uia.ikt205.pomodoro.util.descriptiveTimeToMilliseconds
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minuteToMilliseconds
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var timer:CountDownTimer
    lateinit var startButton:Button

    private lateinit var workSeekBar:SeekBar
    lateinit var workTimerText:TextView

    private lateinit var pauseSeekbar:SeekBar
    lateinit var pauseTimerText:TextView

    private lateinit var repetitionCounter:EditText

    var timeToCountDownInMs = 0L // Contains value to countdown from
    val timeTicks = 1000L           // View update frequency


    val minWorkTime = 1 // minimum value of the workSeekBar in minutes
    val minPauseTime = 1 // minimum value of the pauseSeekBar in minutes

    var timeToCountDownInMinWorkTime = minWorkTime
    var timeToCountDownInMinPauseTime = minPauseTime

    var work = true


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

        repetitionCounter = findViewById(R.id.repetitionCounter)

        workSeekBar = findViewById(R.id.workSeekBar)
        workTimerText = findViewById(R.id.workTimer)
        workSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timeToCountDownInMinWorkTime = progress + minWorkTime // Calculate the work time from seekbar
                updateSeekBar(timeToCountDownInMinWorkTime, workTimerText)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }
        })
        updateSeekBar(timeToCountDownInMinWorkTime, workTimerText)

        pauseSeekbar = findViewById(R.id.pauseSeekBar)
        pauseTimerText = findViewById(R.id.pauseTimer)
        pauseSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timeToCountDownInMinPauseTime = progress + minPauseTime // Calculate the pause time from seekbar
               updateSeekBar(timeToCountDownInMinPauseTime, pauseTimerText)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }
        })
        updateSeekBar(timeToCountDownInMinPauseTime, pauseTimerText)
    }


    // Starts the countdown
    private fun startCountDown() {
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                startButton.text = getString(R.string.startButtonText)
                cancel() // Stop timer
                work = !work // Flip timer status between work and pause
                if (getRepetitionCounter() > 1) {
                    decrementRepetitionCounter()
                    updateSeekBar(timeToCountDownInMinWorkTime, workTimerText)
                    updateSeekBar(timeToCountDownInMinPauseTime, pauseTimerText)
                    startCountDown()
                }
                else {
                    stopCountDown()
                    work = true
                    Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (work)
                    updateCountDownDisplay(millisUntilFinished, workTimerText)
                else
                    updateCountDownDisplay(millisUntilFinished, pauseTimerText)
            }
        }

        Log.d("Timer", "Timer was started")
        startButton.text = getString(R.string.stopButtonText) // Update button text
        workSeekBar.isEnabled = false
        pauseSeekbar.isEnabled = false
        repetitionCounter.isEnabled = false
        timer.start() // Start the timer

    }

    private fun stopCountDown() {
        Log.d("Timer", "Timer was canceled")
        timer.cancel()
        startButton.text = getString(R.string.startButtonText)
        updateCountDownDisplay(descriptiveTimeToMilliseconds(workTimerText.text as String) + 100, workTimerText)
        updateCountDownDisplay(descriptiveTimeToMilliseconds(pauseTimerText.text as String) + 100, pauseTimerText)
        workSeekBar.isEnabled = true
        pauseSeekbar.isEnabled = true
        repetitionCounter.isEnabled = true
        timeToCountDownInMs = if(work)
            descriptiveTimeToMilliseconds(workTimerText.text as String) + 100
        else
            descriptiveTimeToMilliseconds(pauseTimerText.text as String) + 100

    }

    // Updates the countdown text
    fun updateCountDownDisplay(timeInMs:Long, timer: TextView){
        timer.text = millisecondsToDescriptiveTime(timeInMs)
    }

    // Called when seekbar progress changes, updates related objects
    fun updateSeekBar(timeInMin:Int, timer: TextView){
        updateTimeToCountDownInMs(minuteToMilliseconds(timeInMin), timer)
    }

    // Update the value to count down from
    private fun updateTimeToCountDownInMs(ms:Long, timer: TextView) {
        if(startButton.text == getString(R.string.startButtonText) || timeToCountDownInMs == 0L) {
            updateCountDownDisplay(ms, timer)
            timeToCountDownInMs = if(work)
                descriptiveTimeToMilliseconds(workTimerText.text.toString())
            else
                descriptiveTimeToMilliseconds(pauseTimerText.text.toString())
        }
    }

    private fun getRepetitionCounter():Int {
        var counter = 1
        try {
            counter = repetitionCounter.text.toString().toInt()
        }
        catch (e: NumberFormatException) {
            Toast.makeText(this@MainActivity, "test", Toast.LENGTH_SHORT).show()
        }
       return counter
    }

    private fun decrementRepetitionCounter() {
        val counter = getRepetitionCounter() - 1
        repetitionCounter.setText(counter.toString())
    }

}