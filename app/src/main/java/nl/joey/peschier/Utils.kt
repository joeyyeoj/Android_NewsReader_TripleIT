package nl.joey.peschier

import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import com.google.android.material.navigation.NavigationView


class Utils {
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

    fun updateDrawerLayoutAfterLogin(activity: Activity) {
        AppPreferences.init(activity)
        val navigationView = activity.findViewById<NavigationView>(R.id.nav_view)
        val menu = navigationView.menu
        menu.findItem(R.id.login).setVisible(false)
        menu.findItem(R.id.register).setVisible(false)
        menu.findItem(R.id.logout).setVisible(true)
        menu.findItem(R.id.nav_liked).setVisible(true)
    }

    fun updateDrawerLayoutAfterLogout (activity: Activity) {
        AppPreferences.init(activity)
        val navigationView = activity.findViewById<NavigationView>(R.id.nav_view)
        val menu = navigationView.menu
        menu.findItem(R.id.login).setVisible(true)
        menu.findItem(R.id.register).setVisible(true)
        menu.findItem(R.id.logout).setVisible(false)
        menu.findItem(R.id.nav_liked).setVisible(false)
    }
}