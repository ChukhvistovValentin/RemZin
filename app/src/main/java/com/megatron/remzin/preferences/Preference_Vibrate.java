package com.megatron.remzin.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.megatron.remzin.R;

public class Preference_Vibrate extends Preference implements View.OnClickListener {
    static Context context;

    public Preference_Vibrate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Preference_Vibrate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Preference_Vibrate(Context context) {
        super(context);
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        View v = super.getView(convertView, parent);
        context = v.getContext();
        RadioButton btn1 = (RadioButton) v.findViewById(R.id.rbtn1);
        RadioButton btn2 = (RadioButton) v.findViewById(R.id.rbtn2);
        RadioButton btn3 = (RadioButton) v.findViewById(R.id.rbtn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        RestoreVibratePreference(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        int id;
        switch (v.getId()) {
            case R.id.rbtn1:
                id = 0;
                break;
            case R.id.rbtn2:
                id = 1;
                break;
            case R.id.rbtn3:
                id = 2;
                break;
            default:
                id = -1;
                break;
        }
        if (id > -1) {
            PreferenceUtils.setVibrateUtils(context, id);
            SaveVibratePreference(id);
        }
    }

    private void SaveVibratePreference(int pos) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt("vibrate", pos);
        editor.apply();
    }

    private void RestoreVibratePreference(View v) {
        int id;
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        id = mySharedPreferences.getInt("vibrate", 0);
        switch (id) {
            case 1:
                ((RadioButton) v.findViewById(R.id.rbtn2)).setChecked(true);
                break;
            case 2:
                ((RadioButton) v.findViewById(R.id.rbtn3)).setChecked(true);
                break;
            default:
                ((RadioButton) v.findViewById(R.id.rbtn1)).setChecked(true);
        }
    }

}
