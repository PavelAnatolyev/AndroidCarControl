package com.example.joysticktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class MainActivity : AppCompatActivity(), JoystickView.JoystickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.d("Main method", "X percent: $xPercent, Y percent: $yPercent")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bluetooth -> {
                showDevices()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDevices() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
