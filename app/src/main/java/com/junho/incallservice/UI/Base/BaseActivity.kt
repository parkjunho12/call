package com.junho.incallservice.UI.Base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.junho.incallservice.Data.RecentCallLog
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {

    val reList = arrayListOf<RecentCallLog>()

    private val CALL_PROJECTION = arrayOf(
        CallLog.Calls.TYPE,
        CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls._ID,
        CallLog.Calls.DATE, CallLog.Calls.DURATION
    )


    val permissionList = arrayOf<String>(
        "android.permission.RECORD_AUDIO",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.CALL_PHONE",
        "android.permission.READ_PHONE_STATE",
        "android.permission.READ_CONTACTS",
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG"
    )

    fun callLog(){
        val cursor: Cursor? = getCallHistory(this)
        var callCount = 0
        if (cursor != null) {
            if( cursor.moveToFirst() && cursor.count  > 0){
                while (!cursor.isAfterLast){
                    val callIdx:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID))
                    val num = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    val date = timeToString(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)))
                    var callType:String = ""
                    if(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)).equals(CallLog.Calls.INCOMING_TYPE)){
                        callType = "수신"
                    }
                    else if(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)).equals(CallLog.Calls.OUTGOING_TYPE))
                    {
                        callType = "발신"
                    }
                    val callDuration:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))
                    reList.add(RecentCallLog(callIdx, num, date, callType, callDuration))
                    cursor.moveToNext()
                    callCount++

                }
            }

        }
    }



    fun timeToString(time:Long): String {
        val simpleDataformat = SimpleDateFormat("HH:mm")
        val date = simpleDataformat.format(Date(time))
        return date
    }



    fun getCallHistory(context: Context):Cursor? {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
        {
            val cursor: Cursor? = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            return cursor
        }else{
            requestPermission(this,
                permissionList)

            val cursor: Cursor? = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            return cursor
        }
    }


    @SuppressLint("MissingPermission")
    fun deleteCallLog(del_id:String){
        val queryString = CallLog.Calls._ID + "=" + "'" + del_id + "'"
        this.applicationContext.contentResolver.delete(CallLog.Calls.CONTENT_URI, queryString, null)
    }

    fun requestPermission(
        paramActivity: Activity?,
        paramArrayOfString: Array<String>?
    ) {
        ActivityCompat.requestPermissions(paramActivity!!, paramArrayOfString!!, 10001)
    }

}
