package com.megatron.remzin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.megatron.remzin.model.ModelTask;

public class AlarmHelper {
    final static String LOG_TAG = "myLogs";
    private static AlarmHelper instance;
    private AlarmManager alarmManager;
    private static Context context;
    private static AlarmService mService;
//    private static Intent serviceIntent;

    public static AlarmHelper getInstance() {
        Log.v(LOG_TAG, "AlarmHelper getInstance");
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    public void init(Context context) {
        Log.v(LOG_TAG, "AlarmHelper init");
        if (context == null) Log.v(LOG_TAG, "AlarmHelper init context == null");
        this.context = context;
        this.alarmManager = (AlarmManager) //context.getApplicationContext().
                context.getSystemService(Context.ALARM_SERVICE);

        /*if (serviceIntent == null) {
            Log.v(LOG_TAG, "AlarmHelper serviceIntent == null");
            try {
                serviceIntent = new Intent(this.context, AlarmService.class);
                Log.v(LOG_TAG, "AlarmHelper serviceIntent == 1");
                Log.v(LOG_TAG, "AlarmHelper serviceIntent == 2");
                context.startService(serviceIntent);
                Log.v(LOG_TAG, "AlarmHelper serviceIntent == 3");
                Thread.sleep(1000);
                this.context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                Log.v(LOG_TAG, "AlarmHelper serviceIntent == 4");
            } catch (Exception e) {
                Log.v(LOG_TAG, "AlarmHelper init e = " + e.toString());
            }
        }*/
    }

    public void setAlarm(ModelTask task) {
        Log.v(LOG_TAG, "AlarmHelper setAlarm");
        try {
            task.setVibrate_id(Utils.getVibratePreference(context));
            task.setRington_id(Utils.getRington(context));
            task.setNotifi_length(Utils.getNotificationLength(context));
            Log.v(LOG_TAG, "AlarmHelper setNotifi_length = " + task.isNotifi_length());
        } catch (Exception e) {
            Log.v(LOG_TAG, "AlarmHelper setAlarm e = " + e.toString());
        }
//        mService.AlarmService(task);

//        ******************************************************************************************
        Intent intent = new Intent(this.context, Alarm.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("text", task.getText());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());
        intent.putExtra("vibrate_id", task.getVibrate_id());
        //intent.putExtra("ringtone_id", task.getRington_id());
        intent.putExtra("notifi_length", task.isNotifi_length());
        intent.putExtra("img", task.getImg());

        this.alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(),
                PendingIntent.getBroadcast(this.context.getApplicationContext(),
                        (int) task.getTimeStamp(), intent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    public void removeAlarm(long taskTimeStamp) {
        Log.v(LOG_TAG, "AlarmHelper removeAlarm " + (int) taskTimeStamp);
        this.alarmManager.cancel(PendingIntent.getBroadcast(this.context, (int) taskTimeStamp,
                new Intent(this.context, Alarm.class), PendingIntent.FLAG_CANCEL_CURRENT));
    }

    private static ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.v(LOG_TAG, "onServiceConnected 1");
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mService = binder.getService();
            Log.v(LOG_TAG, "onServiceConnected 2");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(LOG_TAG, "onServiceDisconnected");
        }
    };

}
