package com.megatron.remzin.sound_notification_play;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;

public class AlarmController {
    Context context;
    MediaPlayer mp;
    AudioManager mAudioManager;
    //    int userVolume;
    int userRingtonVolume;
    Boolean isMuted;

    public AlarmController(Context c, int userRingtonVolume) { // constructor for my alarm controller class
        this.context = c;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //remeber what the user's volume was set to before we change it.
//        userVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if (mp == null)
            mp = new MediaPlayer();
        this.userRingtonVolume = userRingtonVolume;
    }

    public void playSound(Uri soundURI) {
        Uri alarmSound = null;
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try {
            alarmSound = soundURI;//Uri.parse(soundURI);
        } catch (Exception e) {
            alarmSound = ringtoneUri;
        } finally {
            if (alarmSound == null) {
                alarmSound = ringtoneUri;
            }
        }
//        Toast.makeText(context, alarmSound + "", Toast.LENGTH_LONG).show();
        try {
            if (!mp.isPlaying()) {
                mp.setDataSource(context, alarmSound);
                mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                mp.setLooping(false);//true
                mp.prepare();
                mp.start();
            }
        } catch (IOException e) {
            Toast.makeText(context, "Your alarm sound was unavailable.", Toast.LENGTH_LONG).show();
        }
        // set the volume to what we want it to be.  In this case it's max volume for the alarm stream.
//        mAudioManager.setStreamVolume(
//                AudioManager.STREAM_ALARM,
//                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_PLAY_SOUND);
        toggleMute();
    }

    public void stopSound() {
        // reset the volume to what it was before we changed it.
//        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, userVolume, AudioManager.FLAG_PLAY_SOUND);
        mp.stop();
        mp.reset();
    }

    public void releasePlayer() {
        mp.release();
    }

    //********************************************************
    public boolean toggleMute() {
        isMuted = isVolumeMuted();
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, userRingtonVolume, 0);
//        mAudioManager.adjustStreamVolume(AudioManager.STREAM_RING,
//                AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        if (isMuted) {
//            //If muted, sets the volume at max/2 to avoid making headphone users go deaf.
//            mAudioManager.setStreamVolume(
//                    AudioManager.STREAM_MUSIC,
//                    mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10, 0);
//        } else {
//            //If not muted, mutes the volume.
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//        }

//        isMuted = !isMuted;
//        setVolumeToggleImage(isMuted);
        return isMuted;
    }

    //Sets the appropriate image graphic based on whether the volume is muted or not.
    private void setVolumeToggleImage(boolean isMute) {
//        toggleImage.setImageResource(isMute ? R.drawable.ic_audio_vol_mute : R.drawable.ic_audio_vol);
    }

    private boolean isVolumeMuted() {
        if (mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM) == 0) {
            return true;
        }
        return false;
    }

}
