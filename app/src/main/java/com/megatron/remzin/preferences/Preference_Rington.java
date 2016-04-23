package com.megatron.remzin.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.megatron.remzin.R;

public class Preference_Rington extends Preference {
    Context context;
    Boolean spinner_select = false;

    public Preference_Rington(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Preference_Rington(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Preference_Rington(Context context) {
        super(context);
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        View v = super.getView(convertView, parent);
        context = v.getContext();

//        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_ringtone);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (spinner_select) {
//                    PreferenceUtils.setRingtonUtils(context, position);
//                    SaveRingtonPreference(position);
//                }
//                spinner_select = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
////                PreferenceUtils.setRingtonUtils(context, parent.getSelectedItemPosition());
//            }
//        });

        RestoreRingtonPreference(v);
        parent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    PreferenceUtils.stopRington();
                    spinner_select = false;
                }
                return false;
            }
        });

        SeekBar seekBar_rington = (SeekBar) v.findViewById(R.id.seekBar_rington);
        seekBar_rington.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                SaveRingtoneVolume(arg1);
            }
        });

        return v;
    }

//    @Override
//    public void onClick(View v) {
//        int id = 0;
////        switch (v.getId()) {
////            case R.id.btn_rington_1:
////                id = 0;
////                break;
////            case R.id.btn_rington_2:
////                id = 1;
////                break;
////            case R.id.btn_rington_3:
////                id = 2;
////                break;
////            case R.id.btn_rington_4:
////                id = 3;
////                break;
////            case R.id.btn_rington_5:
////                id = 4;
////                break;
////            default:
////                id = -1;
////                break;
////        }
//        PreferenceUtils.setRingtonUtils(context, id);
//        SaveRingtonPreference(id);
//    }

    private void SaveRingtonPreference(int pos) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt("ringtone", pos);
        editor.commit();
    }

    private void SaveRingtoneVolume(int pos) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt("ringtone_volume", pos);
        editor.commit();
    }

    private void RestoreRingtonPreference(View v) {
        int id;
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        id = mySharedPreferences.getInt("ringtone", 0);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_ringtone);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемента spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.ringtone_array, R.layout.simple_spinner_item);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_select) {
                    PreferenceUtils.setRingtonUtils(context, position);
                    SaveRingtonPreference(position);
                }
                spinner_select = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                PreferenceUtils.setRingtonUtils(context, parent.getSelectedItemPosition());
            }
        });
        spinner.setSelection(id);

//      ********************************
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int a = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        SeekBar s = (SeekBar) v.findViewById(R.id.seekBar_rington);
        s.setMax(a);
        s.setProgress(mySharedPreferences.getInt("ringtone_volume", a / 2));

    }

}
