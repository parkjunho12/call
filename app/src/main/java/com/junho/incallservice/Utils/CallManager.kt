package com.junho.incallservice.Utils

import android.Manifest.permission.MANAGE_OWN_CALLS
import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import android.util.Log
import androidx.annotation.RequiresApi
import com.junho.incallservice.Service.CallConnectionService

@RequiresApi(Build.VERSION_CODES.M)
class CallManager (context: Context){
    val telecomManager: TelecomManager
    var phoneAccountHandle:PhoneAccountHandle
    var context:Context
    val number = "01083696569"
    init {
        telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        this.context = context
        val componentName =  ComponentName(this.context, CallConnectionService::class.java)
        phoneAccountHandle = PhoneAccountHandle(componentName, "Admin")
        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "Admin").setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED).build()


        telecomManager.registerPhoneAccount(phoneAccount)
        val intent = Intent()
        intent.component = ComponentName("com.android.server.telecom", "com.android.server.telecom.settings.EnableAccountPreferenceActivity")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    }

        @TargetApi(Build.VERSION_CODES.M)
        fun startOutgoingCall() {
            val extras = Bundle()
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)

            val manager = context.getSystemService(TELECOM_SERVICE) as TelecomManager
            val phoneAccountHandle = PhoneAccountHandle(ComponentName(context.packageName, CallConnectionService::class.java!!.getName()), "estosConnectionServiceId")
            val test = Bundle()
            test.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
            test.putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL)
            test.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, extras)
            try {
                manager.placeCall(Uri.parse("tel:$number"), test)
            } catch (e:SecurityException){
                e.printStackTrace()
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        fun  startIncomingCall(){
            if (this.context.checkSelfPermission(MANAGE_OWN_CALLS) == PackageManager.PERMISSION_GRANTED) {
                val extras = Bundle()
                val uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null)
                extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri)
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
                extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
                val isCallPermitted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telecomManager.isIncomingCallPermitted(phoneAccountHandle)
                } else {
                    true
                }
                Log.i("CallManager", "is incoming call permited = $isCallPermitted")
                telecomManager.addNewIncomingCall(phoneAccountHandle, extras)
            }

    }

}