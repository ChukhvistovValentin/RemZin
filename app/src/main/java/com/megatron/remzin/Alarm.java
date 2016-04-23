package com.megatron.remzin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import com.megatron.remzin.sound_notification_play.AlarmController;

public class Alarm extends BroadcastReceiver {
    //    private final static String BROADCAST_ACTION = "com.megatron.remzin.alarm.AlarmReceiver";
    final String LOG_TAG = "myLogs";

    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Alarm onReceive");
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        int color = intent.getIntExtra("color", 0);
//        int vibrate_id = intent.getIntExtra("vibrate_id", -1);
//        int r_id = Utils.getRington(context.getApplicationContext()); //********
//        int ringtone_id = r_id;//intent.getIntExtra("ringtone_id", -1); // -1
        Boolean notifi = intent.getBooleanExtra("notifi_length", true);
        String img_resources = intent.getStringExtra("img");

        Log.v(LOG_TAG, "Alarm notifi = " + notifi);

        Intent resultIntent = new Intent(context, MainActivity.class);
//        if (MegatronApplication.isActivityVisible()) {
//            resultIntent = intent;
//        }
        resultIntent.putExtra("hello", "wtf");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);//FLAG_CANCEL_CURRENT

        Builder builder = new Builder(context);
        builder.setTicker(context.getResources().getText(R.string.app_name) + " " + title); // строка которая отобр. при появлении уведомления
        builder.setContentTitle(/*context.getResources().getText(R.string.app_name) + "\n" +*/ title); // когда открыли шторку - заголовок
        builder.setContentText(text); // сам текст
        builder.setColor(context.getResources().getColor(color));
        builder.setSmallIcon(R.mipmap.ic_launcher); // иконка
        builder.setLights(Color.GREEN, 500, 2000);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(Notification.PRIORITY_HIGH);

        Notification notification;
        notification = notifi ? (new NotificationCompat.BigTextStyle(builder).bigText(text).build()) : builder.build();
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//        notification.defaults = Notification.DEFAULT_VIBRATE;// | Notification.DEFAULT_SOUND;

        //int numMessages = 0;
        //builder.setNumber(++numMessages);

//        int vibrate_id = Utils.getVibratePreference(context);
//        Toast.makeText(context, "index = " + vibrate_id, Toast.LENGTH_LONG).show();
//        if (vibrate_id > -1)
//            notification.vibrate = Utils.getVibrateIndex(vibrate_id);
//        if (ringtone_id > -1)
//            notification.sound = Utils.getRington(context, ringtone_id);

//        Toast.makeText(context, "index = " + ringtone_id, Toast.LENGTH_LONG).show();
//        if (ringtone_id > -1) {
//            AlarmController sound_player = new AlarmController(context.getApplicationContext());
//            sound_player.playSound(Utils.getRington(context.getApplicationContext(), ringtone_id));
//        }
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify((int) timeStamp, notification);

        Intent i = new Intent(context, MegatronService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("title", title);
        i.putExtra("text", text);
        i.putExtra("ID", (int) timeStamp);
        i.putExtra("img", img_resources);

        context.startService(i);
    }

}
