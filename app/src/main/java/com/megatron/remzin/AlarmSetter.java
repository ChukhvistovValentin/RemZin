package com.megatron.remzin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.megatron.remzin.database.DBHelper;
import com.megatron.remzin.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

public class AlarmSetter extends BroadcastReceiver {
    final static String LOG_TAG = "myLogs";

    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "AlarmSetter onReceive 1");
        DBHelper dbHelper = new DBHelper(context);
        Log.v(LOG_TAG, "AlarmSetter onReceive 2");
        AlarmHelper.getInstance().init(context);
        Log.v(LOG_TAG, "AlarmSetter onReceive 3");
        AlarmHelper alarmHelper = AlarmHelper.getInstance();
        Log.v(LOG_TAG, "AlarmSetter onReceive 4");
        List<ModelTask> tasks = new ArrayList();
        Log.v(LOG_TAG, "AlarmSetter onReceive 5");
        tasks.addAll(dbHelper.query().getTasks("task_status = ? OR task_status = ?",
                new String[]{Integer.toString(1), Integer.toString(0)}, DBHelper.TASK_DATE_COLUMN));
        for (ModelTask task : tasks) {
            if (task.getDate() != 0) {
                Log.v(LOG_TAG, "AlarmSetter setAlarm start");
                alarmHelper.setAlarm(task);
                Log.v(LOG_TAG, "AlarmSetter setAlarm end");
            }
        }
        Log.v(LOG_TAG, "AlarmHelper end");
    }
}
