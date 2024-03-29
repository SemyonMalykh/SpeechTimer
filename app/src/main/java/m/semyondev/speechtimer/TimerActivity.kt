package m.semyondev.speechtimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.buttons_timer.*

import kotlinx.android.synthetic.main.main_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.progress_bar_timer.*
import m.semyondev.speechtimer.util.NotificationUtil
import m.semyondev.speechtimer.util.PrefUtil
import java.util.*

class TimerActivity : AppCompatActivity() {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerSpan{
        Intro, Main, Conclusion
    }

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var timerSpan = TimerSpan.Intro

    private var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "      Timer"

        fab_play_pause.setOnClickListener{ v ->
            if (timerState == TimerState.Running){
                timer.cancel()
                timerState = TimerState.Paused
                updateButtons()
            }
            else {
                startTimer()
                timerState = TimerState.Running
                updateButtons()
            }
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }

        fab_set.setOnClickListener{ v ->
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        else if (timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
        }
        PrefUtil.setPreviousTimerLength(timerSpan, timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
        PrefUtil.setTimerSpan(timerSpan, this)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)
        timerSpan = PrefUtil.getTimerSpan(this)
        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped
        timerSpan = TimerSpan.Intro
        PrefUtil.setTimerSpan(TimerSpan.Intro, this)
        setNewTimerLength()

        PrefUtil.setPreviousTimerLength(TimerSpan.Intro, PrefUtil.getTimerLength(TimerSpan.Intro, this) * 60L, this)
        PrefUtil.setPreviousTimerLength(TimerSpan.Main, PrefUtil.getTimerLength(TimerSpan.Main, this) * 60L, this)
        PrefUtil.setPreviousTimerLength(TimerSpan.Conclusion, PrefUtil.getTimerLength(TimerSpan.Conclusion, this) * 60L, this)
        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running


        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
        vibrate()

    }

    private fun vibrate(){
        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26){
            vibratorService.vibrate(VibrationEffect.createOneShot(300,10))
        }
        else{
            vibratorService.vibrate(300)
        }
    }

    private fun onTimerContinues(){
        when(timerSpan){
            TimerSpan.Intro -> {
                vibrate()
                timerSpan = TimerSpan.Main
                PrefUtil.setTimerSpan(timerSpan, this)
                setNewTimerLength()
                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds
                startTimer()
            }
            TimerSpan.Main -> {
                vibrate()
                timerSpan = TimerSpan.Conclusion
                PrefUtil.setTimerSpan(timerSpan, this)
                setNewTimerLength()
                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds
                startTimer()
            }
        }
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = if (timerSpan == TimerSpan.Conclusion) onTimerFinished()
                                      else onTimerContinues()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        Log.v("Timer span", timerSpan.toString())
        val lengthInMinutes = PrefUtil.getTimerLength(timerSpan,this)
        Log.v("Timer length", lengthInMinutes.toString())
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLength(timerSpan,this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        progress_countdown.progress = secondsRemaining.toInt()
    }

    private fun updateButtons(){
        when (timerState) {
            TimerState.Running ->{
                fab_play_pause.isEnabled = true
                fab_play_pause.setImageResource(R.drawable.ic_pause)
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_play_pause.isEnabled = true
                fab_play_pause.setImageResource(R.drawable.ic_play)
                fab_stop.isEnabled = false
            }
            TimerState.Paused -> {
                fab_play_pause.isEnabled = true
                fab_play_pause.setImageResource(R.drawable.ic_play)
                fab_stop.isEnabled = true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
