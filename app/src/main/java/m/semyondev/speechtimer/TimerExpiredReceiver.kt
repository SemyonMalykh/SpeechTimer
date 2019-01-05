package m.semyondev.speechtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import m.semyondev.speechtimer.util.NotificationUtil
import m.semyondev.speechtimer.util.PrefUtil
import java.util.*

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
//        PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
//        PrefUtil.setAlarmSetTime(0, context)
        Log.v("Timer expired", " ")

        val span = PrefUtil.getTimerSpan(context)
        when (span){
            TimerActivity.TimerSpan.Intro ->{
                PrefUtil.setTimerSpan(TimerActivity.TimerSpan.Main, context)
                val timerLength = PrefUtil.getPreviousTimerLength(TimerActivity.TimerSpan.Main, context)
                PrefUtil.setSecondsRemaining(timerLength, context)
                val nowSeconds = Calendar.getInstance().timeInMillis / 1000
                TimerActivity.setAlarm(context, nowSeconds, timerLength)
            }
            TimerActivity.TimerSpan.Main ->{
                PrefUtil.setTimerSpan(TimerActivity.TimerSpan.Conclusion, context)
                val timerLength = PrefUtil.getPreviousTimerLength(TimerActivity.TimerSpan.Conclusion, context)
                PrefUtil.setSecondsRemaining(timerLength, context)
                val nowSeconds = Calendar.getInstance().timeInMillis / 1000
                TimerActivity.setAlarm(context, nowSeconds, timerLength)
            }
            TimerActivity.TimerSpan.Conclusion ->{
                NotificationUtil.showTimerExpired(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
                PrefUtil.setAlarmSetTime(0, context)
            }
        }
    }
}
