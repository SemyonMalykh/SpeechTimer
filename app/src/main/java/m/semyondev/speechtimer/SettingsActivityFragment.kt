package m.semyondev.speechtimer

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsActivityFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}
