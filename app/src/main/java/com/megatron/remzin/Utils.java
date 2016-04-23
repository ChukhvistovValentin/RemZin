package com.megatron.remzin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getDate(long date) {
        return new SimpleDateFormat("dd.MM.yy").format(Long.valueOf(date));
    }

    public static String getShortDate(long date) {
        return new SimpleDateFormat("dd MMMM yyyy").format(Long.valueOf(date));
    }

    public static String getTime(long time) {
        return new SimpleDateFormat("HH:mm").format(Long.valueOf(time));
    }

    public static void setFontTextViev(Activity v, TextView textView, String fontName) {
        if (textView != null) {
            Typeface typeFace = Typeface.createFromAsset(v.getAssets(), "fonts/" + fontName + ".ttf");
            textView.setTypeface(typeFace);
        }
    }

    public static String getFullDate(long date) {
        return new SimpleDateFormat("dd.MM.yy HH:mm").format(Long.valueOf(date));
    }

    public static Date getDateFromStr(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        try {
            Date dateF = format.parse(date);
            return dateF;
//                        System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static long[] getVibrateIndex(int index) {
        switch (index) {
            case 0:
                return new long[]{0, 500, 300, 500, 300, 500};
            case 1:
                return new long[]{0, 1000, 300, 1000, 300, 1000};
            case 2:
                return new long[]{0, 1500, 500, 1500, 500, 1500};
            default:
                return new long[]{0, 0};
        }
    }

    public static int getVibratePreference(Context context) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean use = mySharedPreferences.getBoolean("key_vibrate", true);
        int id = mySharedPreferences.getInt("vibrate", 0);
        return (use ? id : -1);
    }

    public static Uri getRington(Context c, int index) {
        int rington[] = {R.raw.sound_1, R.raw.sound_2, R.raw.sound_3, R.raw.sound_4,
                R.raw.sound_5, R.raw.sound_6, R.raw.sound_7, R.raw.sound_8,
                R.raw.sound_9, R.raw.sound_10, R.raw.sound_11, R.raw.sound_12,
                R.raw.sound_13, R.raw.sound_14, R.raw.sound_15, R.raw.sound_16};
//        Toast.makeText(c, "index = " + index, Toast.LENGTH_LONG).show();
        String str = "android.resource://" + c.getApplicationContext().getPackageName() + "/";
        if ((index >= 0) && (index <= 16)) {
            return Uri.parse(str + rington[index]);
        } else return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    public static int getRington(Context c) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
        boolean use = mySharedPreferences.getBoolean("key_rington", true);
        int id = mySharedPreferences.getInt("ringtone", 0);
        return (use ? id : -1);
    }

    public static Boolean getNotificationLength(Context c) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
        boolean use = mySharedPreferences.getBoolean("notifi_length", true);
        return use;
    }

    public static void refreshImageTask(Context c, ImageView img, String url_image) {
        try {
            InputStream ims = c.getAssets().open("icons" + "/" + url_image);
            Drawable d = Drawable.createFromStream(ims, null);
            // выводим картинку в ImageView
            img.setImageDrawable(d);
            img.setTag(url_image);
        } catch (IOException e) {
            img.setImageResource(R.drawable.ic_image_btn);
        }
    }

}
