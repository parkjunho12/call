package com.junho.incallservice.Service

import android.os.Build
import android.provider.CallLog.Calls.PRESENTATION_ALLOWED
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.junho.incallservice.Utils.CallConnection

@RequiresApi(Build.VERSION_CODES.M)
class CallConnectionService : ConnectionService() {
    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.i("CallConnectionService", "onCreateOutgoingConnection")
        val conn =
            CallConnection(applicationContext)
        conn.setAddress(request!!.address, PRESENTATION_ALLOWED)
        conn.setInitializing()
        conn.setActive()
        return conn
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "create outgoing call failed")
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.i("CallConnectionService", "onCreateIncomingConnection")
        val conn =
            CallConnection(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            conn.connectionProperties = Connection.PROPERTY_SELF_MANAGED
        }
        conn.setCallerDisplayName("test call", TelecomManager.PRESENTATION_ALLOWED)
        conn.setAddress(request!!.address, PRESENTATION_ALLOWED)
        conn.setInitializing()
        conn.setActive()

        return conn
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "create outgoing call failed ")
    }
}