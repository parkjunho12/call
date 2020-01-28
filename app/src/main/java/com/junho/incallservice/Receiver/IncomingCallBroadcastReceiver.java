package com.junho.incallservice.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.junho.incallservice.UI.Main.MainActivity;

import gun0912.com.incomingcallmarketbroadcastreceiver.CallingService;

/**
 * Created by TedPark on 15. 12. 10..
 */
public class IncomingCallBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "PHONE STATE";
    private static String mLastState;

    private final Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG,"onReceive()");


        /**
         * http://mmarvick.github.io/blog/blog/lollipop-multiple-broadcastreceiver-call-state/
         * 2번 호출되는 문제 해결
         */
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(mLastState)) {
            return;

        } else {
            mLastState = state;

        }

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
           String savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        }
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);

            Intent serviceIntent = new Intent(context, MainActivity.class);
            serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
            context.startService(serviceIntent);

        }



    }


}