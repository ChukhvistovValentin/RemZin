package com.megatron.remzin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.megatron.remzin.sound_notification_play.AlarmController;

public class MegatronService extends Service {
    static final public String RECEIVER_MESSAGE = "com.megatron.remzin.RECEIVER_MESSAGE";
    static final public String RECEIVER_RESULT = "com.megatron.remzin.RECEIVER_RESULT";

    //    private final static String LOG_TAG = "myLogs";
    private static Context context;
    private static SharedPreferences mySharedPreferences;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        Log.v(LOG_TAG, "MegatronService onStart");
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.v(LOG_TAG, "MegatronService onBind");
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.v(LOG_TAG, "MegatronService onCreate");
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        mySharedPreferences = null;
        //Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
//        Log.v(LOG_TAG, "MegatronService onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        int timeStamp = intent.getIntExtra("ID", -1);
        int ringtone_id = getRington();
        int vibrate_id = getVibrate();
        int ringtoneVolume;

        if (ringtone_id > -1) {
            ringtoneVolume = getRingtoneVolume();
            AlarmController sound_player = new AlarmController(context.getApplicationContext(), ringtoneVolume);
            sound_player.playSound(Utils.getRington(context.getApplicationContext(), ringtone_id));
        }

        if (vibrate_id > -1) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(Utils.getVibrateIndex(vibrate_id), -1);
        }

//        Log.v(LOG_TAG, "MegatronService onStartCommand");
        if (!getActivityVisible()) {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String img = intent.getStringExtra("img");

            Intent i = new Intent(context, ActivityNotification.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("title", title);
            i.putExtra("text", text);
            i.putExtra("ID", timeStamp);
            i.putExtra("img", img);
//            Log.v(LOG_TAG, "MegatronService putExtra ID = " + timeStamp);
            context.startActivity(i);
        } else {
//            Log.v(LOG_TAG, "MegatronService LocalBroadcastManager MainActivity start " + timeStamp);
            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
            Intent i = new Intent(RECEIVER_RESULT);
            i.putExtra(RECEIVER_MESSAGE, (int) timeStamp);
            broadcaster.sendBroadcast(i);
//            Log.v(LOG_TAG, "MegatronService LocalBroadcastManager MainActivity sending");
//            Log.v(LOG_TAG, "MegatronService LocalBroadcastManager MainActivity end");
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean getActivityVisible() {
        if (mySharedPreferences == null) {
            mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        Boolean res = mySharedPreferences.getBoolean("visible", false);
//        Log.v(LOG_TAG, "MegatronService getActivityVisible *** " + res);
        return res;
    }

    private int getRington() {
        if (mySharedPreferences == null) {
            mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        boolean use = mySharedPreferences.getBoolean("key_rington", true);
        int id = mySharedPreferences.getInt("ringtone", 0);
        return (use ? id : -1);
    }

    private int getVibrate() {
        if (mySharedPreferences == null) {
            mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        boolean use = mySharedPreferences.getBoolean("key_vibrate", true);
        int id = mySharedPreferences.getInt("vibrate", 0);
        return (use ? id : -1);
    }

    private int getRingtoneVolume() {
        if (mySharedPreferences == null) {
            mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mySharedPreferences.getInt("ringtone_volume", 0);
    }
}
