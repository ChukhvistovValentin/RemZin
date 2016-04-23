package com.megatron.remzin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.megatron.remzin.model.ModelTask;

public class AlarmService extends Service {
    final String LOG_TAG = "myLogs";
    Context context;
    private AlarmManager alarmManager;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AlarmService getService() {
            Log.v(LOG_TAG, "AlarmService LocalBinder getService");
            // Return this instance of LocalService so clients can call public methods
            return AlarmService.this;
        }
    }

    public void AlarmService(ModelTask modelTask) {
        Log.v(LOG_TAG, "AlarmService AlarmService");
        this.alarmManager = (AlarmManager) //context.getApplicationContext().
                context.getSystemService(Context.ALARM_SERVICE);
        StartServiceAlarm(modelTask);
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.v(LOG_TAG, "AlarmService onStart");
        super.onStart(intent, startId);
    }

    //        @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "AlarmService onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.v(LOG_TAG, "AlarmService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(LOG_TAG, "AlarmService onStartCommand");
        if ((flags & START_FLAG_RETRY) == 0) {
            // TODO Если это повторный запуск, выполнить какие-то действия.
            Log.v(LOG_TAG, "AlarmService (flags & START_FLAG_RETRY) == 0");
//            Toast.makeText(context, "", Toast.LENGTH_LONG).show();
        } else {
            // TODO Альтернативные действия в фоновом режиме.
            Log.v(LOG_TAG, "AlarmService (flags & START_FLAG_RETRY) <> 0");
            Toast.makeText(context, "AlarmService (flags & START_FLAG_RETRY) <> 0", Toast.LENGTH_LONG).show();
        }
        //            return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    private void StartServiceAlarm(ModelTask serviceTask) {
        Log.v(LOG_TAG, "AlarmService StartServiceAlarm");
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra("title", serviceTask.getTitle());
        intent.putExtra("text", serviceTask.getText());
        intent.putExtra("time_stamp", serviceTask.getTimeStamp());
        intent.putExtra("color", serviceTask.getPriorityColor());
        intent.putExtra("vibrate_id", serviceTask.getVibrate_id());
        intent.putExtra("ringtone_id", serviceTask.getRington_id());
        intent.putExtra("notifi_length", serviceTask.isNotifi_length());

        alarmManager.set(AlarmManager.RTC_WAKEUP, serviceTask.getDate(),
                PendingIntent.getBroadcast(context, (int) serviceTask.getTimeStamp(),
                        intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.v(LOG_TAG, "AlarmService StartServiceAlarm modeltask = " + serviceTask.getTitle());
    }


}
