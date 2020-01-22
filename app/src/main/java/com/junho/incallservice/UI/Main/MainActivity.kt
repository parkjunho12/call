package com.junho.incallservice.UI.Main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.junho.incallservice.R
import com.junho.incallservice.UI.Base.BaseActivity

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
            deleteCallLog(reList[0].idx)
        } else {
            callLog()
            deleteCallLog(reList[0].idx)
        }


    }
}
