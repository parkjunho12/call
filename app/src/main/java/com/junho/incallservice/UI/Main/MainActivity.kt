package com.junho.incallservice.UI.Main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.junho.incallservice.R
import com.junho.incallservice.UI.Base.BaseActivity
import com.junho.incallservice.Utils.CallManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermission(this,
                permissionList)
            callLog()
            //deleteCallLog(reList[0].idx)
        } else {
            callLog()
            //deleteCallLog(reList[0].idx)
        }
        val callManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CallManager(context = this)
        } else {
            TODO("VERSION.SDK_INT < M")
        }

        call_button.setOnClickListener {
            callManager.startOutgoingCall()
        }
        btn_close.setOnClickListener {
            callManager.startIncomingCall()
        }

    }
}
