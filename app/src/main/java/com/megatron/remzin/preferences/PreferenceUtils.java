package com.megatron.remzin.preferences;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.megatron.remzin.Utils;

public class PreferenceUtils {
    private static MediaPlayer player;

    public static void setVibrateUtils(Context c, int id) {
        //проверить вибрацию в настройках
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Utils.getVibrateIndex(id), -1);
    }

    public static void setRingtonUtils(Context c, int id) {
        // проигрывание музыки
        if (player != null)
            player.stop();
        player = MediaPlayer.create(c, Utils.getRington(c, id));

        player.start();
    }

    public static void stopRington(){
        //остановить звук при выходе из настроек
        if (player != null) player.stop();
    }

}
