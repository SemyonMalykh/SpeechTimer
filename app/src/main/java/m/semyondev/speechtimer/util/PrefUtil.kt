package m.semyondev.speechtimer.util

import android.content.Context
import android.preference.PreferenceManager
import m.semyondev.speechtimer.TimerActivity

class PrefUtil{
    companion object {
        private const val TIMER_LENGTH_ID = "m.semyondev.speechtimer.timer_length"
        private const val TIMER_MAIN_LENGTH_ID = "m.semyondev.speechtimer.timer_length_main"
        private const val TIMER_CONCLUSION_LENGTH_ID = "m.semyondev.speechtimer.timer_length_conclusion"

        fun getTimerLength(span: TimerActivity.TimerSpan, context: Context) : Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            when (span){
                TimerActivity.TimerSpan.Intro ->    return preferences.getInt(TIMER_LENGTH_ID, 10)
                TimerActivity.TimerSpan.Main ->  return preferences.getInt(TIMER_MAIN_LENGTH_ID, 10)
                TimerActivity.TimerSpan.Conclusion ->  return preferences.getInt(TIMER_CONCLUSION_LENGTH_ID, 10)
            }
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "m.semyondev.speechtimer.previous_timer_length"
        private const val PREVIOUS_TIMER_MAIN_LENGTH_SECONDS_ID = "m.semyondev.speechtimer.previous_timer_length_main"
        private const val PREVIOUS_TIMER_CONCLUSION_LENGTH_SECONDS_ID = "m.semyondev.speechtimer.previous_timer_length_conclusion"

        fun getPreviousTimerLength(span: TimerActivity.TimerSpan, context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            when (span){
                TimerActivity.TimerSpan.Intro ->  return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
                TimerActivity.TimerSpan.Main ->  return preferences.getLong(PREVIOUS_TIMER_MAIN_LENGTH_SECONDS_ID, 0)
                TimerActivity.TimerSpan.Conclusion ->  return preferences.getLong(PREVIOUS_TIMER_CONCLUSION_LENGTH_SECONDS_ID, 0)
            }

        }

        fun setPreviousTimerLength(span: TimerActivity.TimerSpan, seconds : Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            when (span){
                TimerActivity.TimerSpan.Intro ->  editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
                TimerActivity.TimerSpan.Main ->  editor.putLong(PREVIOUS_TIMER_MAIN_LENGTH_SECONDS_ID, seconds)
                TimerActivity.TimerSpan.Conclusion ->  editor.putLong(PREVIOUS_TIMER_CONCLUSION_LENGTH_SECONDS_ID, seconds)
            }
            editor.apply()
        }


//        fun getPreviousTimerLength(context: Context): Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
//        }
//
//        fun setPreviousTimerLength(seconds : Long, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
//            editor.apply()
//        }


        private const val TIMER_STATE_ID = "m.semyondev.speechtimer.timer_state"

        fun getTimerState(context: Context): TimerActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val TIMER_SPAN_ID = "m.semyondev.speechtimer.timer_span"

        fun getTimerSpan(context: Context): TimerActivity.TimerSpan{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_SPAN_ID, 0)
            return TimerActivity.TimerSpan.values()[ordinal]
        }

        fun setTimerSpan(state: TimerActivity.TimerSpan, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_SPAN_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "m.semyondev.speechtimer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds : Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "m.semyondev.speechtimer.background_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time : Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()

        }
    }
}