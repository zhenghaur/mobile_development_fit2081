package com.example.movielibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody();
//            Toast.makeText(context, "Sender: " + senderNum + ", message: " + message, Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent();
            myIntent.setAction("MySMS");
            myIntent.putExtra("key1", message);
            context.sendBroadcast(myIntent);
        }
    }
}
