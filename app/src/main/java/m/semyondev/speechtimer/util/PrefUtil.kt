package m.semyondev.speechtimer.util

import android.content.Context
import android.preference.PreferenceManager
import m.semyondev.speechtimer.TimerActivity

class PrefUtil{
    companion object {
        private const val TIME_LENGTH_ID = "m.semyondev.speechtimer.timer_length"
        fun getTimerLength(context: Context) : Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIME_LENGTH_ID, 10)
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "m.semyondev.speechtimer.previous_timer_length"

        fun getPreviousTimerLength(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLength(seconds : Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun setPreviousTimerLengthSet(seconds: Set<String>, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putStringSet(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun getPreviousTimerLengthSet(context: Context): Set<String>{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getStringSet(PREVIOUS_TIMER_LENGTH_SECONDS_ID, setOf("0"))
        }


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

        private const val SECONDS_REMAINING_ID = "m.semyondev.speechtimer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun getSecondsRemainingSet(runningTimerNum : Int, context: Context): String{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getStringSet(SECONDS_REMAINING_ID, setOf("0")).elementAt(runningTimerNum)
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