package com.tychestudios.android.buthatkesms.SmsMethods;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.tychestudios.android.buthatkesms.R;
import com.tychestudios.android.buthatkesms.activities.MainActivity;

/**
 * Created by Harish Vishwakarma on 4/26/2016.
 */
public class SmsReceiver extends BroadcastReceiver {
        private String TAG = SmsReceiver.class.getSimpleName();
        Context context;

        public SmsReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            this.context = context;

            SmsMessage[] msgs = null;

            String str = "";

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i=0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                    str += msgs[i].getMessageBody();
                    str += "\n";
                }

            }
            sendNotification(str,"New SMS received.");
        }
    private void sendNotification(String message, String title) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_launcher)
                .setContentTitle("BuyHatkeSMS")
                .setContentText(message)
                .setSubText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    }
