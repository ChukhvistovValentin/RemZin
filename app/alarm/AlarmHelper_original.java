package com.megatron.remzin.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.megatron.remzin.Alarm;
import com.megatron.remzin.AlarmHelper;
import com.megatron.remzin.model.ModelTask;

public class AlarmHelper_original {
    final String LOG_TAG = "myLogs";
    private static AlarmHelper instance;
    public AlarmManager alarmManager;
    private Context context;

    public ModelTask serviceTask;

    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) //context.getApplicationContext().
                context.getSystemService(Context.ALARM_SERVICE);
        Log.v(LOG_TAG, "init");
    }

    public void setAlarm(ModelTask task) {
        serviceTask = task;
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
        Log.v(LOG_TAG, "setAlarm");

//        ******************************************************************************************
//        Intent intent = new Intent(this.context, Alarm.class);
//        intent.putExtra("title", task.getTitle());
//        intent.putExtra("text", task.getText());
//        intent.putExtra("time_stamp", task.getTimeStamp());
//        intent.putExtra("color", task.getPriorityColor());
//        this.alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(),
//                PendingIntent.getBroadcast(this.context.getApplicationContext(),
//                        (int) task.getTimeStamp(), intent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    public void removeAlarm(long taskTimeStamp) {
        this.alarmManager.cancel(PendingIntent.getBroadcast(this.context, (int) taskTimeStamp,
                new Intent(this.context, Alarm.class), PendingIntent.FLAG_CANCEL_CURRENT));
    }

    // *******************
    public class AlarmService extends Service {
        Context context;

        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);
            Log.v(LOG_TAG, "onStart");
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            Log.v(LOG_TAG, "onBind");
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Log.v(LOG_TAG, "onCreate");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.v(LOG_TAG, "onStartCommand");
            context = this;
            if ((flags & START_FLAG_RETRY) == 0) {
                // TODO Если это повторный запуск, выполнить какие-то действия.
                Log.v(LOG_TAG, "(flags & START_FLAG_RETRY) == 0");
            } else {
                // TODO Альтернативные действия в фоновом режиме.
                Log.v(LOG_TAG, "(flags & START_FLAG_RETRY) <> 0");
                StartServiceAlarm();
            }
            //            return super.onStartCommand(intent, flags, startId);
            return Service.START_STICKY;
        }

        private void StartServiceAlarm() {
            Log.v(LOG_TAG, "StartServiceAlarm");
            Intent intent = new Intent(this.context, Alarm.class);
            intent.putExtra("title", serviceTask.getTitle());
            intent.putExtra("text", serviceTask.getText());
            intent.putExtra("time_stamp", serviceTask.getTimeStamp());
            intent.putExtra("color", serviceTask.getPriorityColor());
            alarmManager.set(AlarmManager.RTC_WAKEUP, serviceTask.getDate(),
                    PendingIntent.getBroadcast(this.context.getApplicationContext(),
                            (int) serviceTask.getTimeStamp(), intent, PendingIntent.FLAG_CANCEL_CURRENT));
        }

    }

}
