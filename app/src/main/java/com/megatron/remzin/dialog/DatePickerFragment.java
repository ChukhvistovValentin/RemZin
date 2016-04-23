package com.megatron.remzin.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.megatron.remzin.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {
    private final static String LOG_TAG = "myLogs";
    public static final String EXTRA_DATE = "com.megatron.remzin.dialog.Dialog_Date";
    //    private Date mDate;
    private Calendar mDate;

    public static DatePickerFragment newInstance(Calendar date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
        mDate = (Calendar) getArguments().getSerializable(EXTRA_DATE);

        Calendar c = Calendar.getInstance();
        if (mDate != null) {
            c = mDate;
            //c.set(mDate.getYear(), mDate.getMonth(), mDate.getDay());
//            c.setTime(mDate);
        } else mDate = Calendar.getInstance();

        final DatePickerDialog dp = new DatePickerDialog(getActivity(), this, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DATE));
        dp.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dp.cancel();
                    }
                });

        dp.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        mDate.setYear(dp.getDatePicker().getYear());
//                        mDate.setMonth(dp.getDatePicker().getMonth());
//                        mDate.setDate(dp.getDatePicker().getDayOfMonth());
                        mDate.set(dp.getDatePicker().getYear(), dp.getDatePicker().getMonth(), dp.getDatePicker().getDayOfMonth());
                        sendResult(Activity.RESULT_OK);
                    }
                });

        return dp;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        if (mDate == null)
//            mDate = new Date();
//        mDate.setYear(year);
//        mDate.setMonth(monthOfYear + 1);
//        mDate.setDate(dayOfMonth);
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        Log.v(LOG_TAG, "DatePickerFragment sendResult " + mDate.toString());

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }
}
