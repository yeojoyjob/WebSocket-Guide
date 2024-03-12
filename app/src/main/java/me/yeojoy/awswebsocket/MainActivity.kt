package me.yeojoy.awswebsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel()
        mainViewModel?.socketStatus?.observe(this) {
            // Do something to view
        }

        mainViewModel?.messages?.observe(this) {
            // set Messages to view
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel?.release()
        mainViewModel = null
    }
}