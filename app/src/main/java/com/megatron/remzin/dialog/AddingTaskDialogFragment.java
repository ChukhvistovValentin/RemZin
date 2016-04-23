package com.megatron.remzin.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.megatron.remzin.AlarmHelper;
import com.megatron.remzin.R;
import com.megatron.remzin.Utils;
import com.megatron.remzin.model.ModelTask;

import java.util.Calendar;

public class AddingTaskDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final static String LOG_TAG = "myLogs";
    private AddingTaskListener addingTaskListener;
//    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_ICON = 1;

    EditText etDate = null;
    EditText etTime = null;
    Calendar calendar;
    ImageView btn_image;
    String img;

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask modelTask);

        void onTaskAddingCancel();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.addingTaskListener = (AddingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTaskListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        final View container = getActivity().getLayoutInflater().inflate(R.layout.dialog_task, null);

        TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();
        TextInputLayout tilText = (TextInputLayout) container.findViewById(R.id.tilDialogTaskText);
        final EditText etText = tilText.getEditText();
        btn_image = (ImageView) container.findViewById(R.id.btn_image);
        img = "";
        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogTaskDate);
        /*final EditText*/
        etDate = tilDate.getEditText();
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTime);
        etTime = tilTime.getEditText();
//        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPriority);
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilText.setHint(getResources().getString(R.string.task_text));
        //***
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));
        builder.setView(container);
        final ModelTask task = new ModelTask();
//        spPriority.setAdapter(new ArrayAdapter(getActivity(), 17367049, getResources().getStringArray(R.array.priority_levels)));
//        spPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                task.setPriority(position);
//            }
//
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
        calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
//        calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY) + 1);

        Log.v(LOG_TAG, "AddingTaskDialogFragment calendar " + calendar.toString());

        etDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DatePickerDialog.newInstance(AddingTaskDialogFragment.this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

//                DatePickerFragment dialogFragment = new DatePickerFragment().newInstance(calendar);
//                dialogFragment.setTargetFragment(AddingTaskDialogFragment.this, REQUEST_DATE);
//                dialogFragment.show(AddingTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
            }
        });

        etTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText("");
                }
//                new TimePickerFragment() {
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
//                        calendar.set(calendar.MINUTE, minute);
//                        calendar.set(calendar.SECOND, 0);
//                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
//                    }
//
//                    public void onCancel(DialogInterface dialog) {
//                        etTime.setText(null);
//                    }
//                }.show(AddingTaskDialogFragment.this.getFragmentManager(), "TimePickerFragment");
                TimePickerDialog.newInstance(AddingTaskDialogFragment.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        btn_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IconDialog.class);
                startActivityForResult(intent, REQUEST_ICON);
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(etTitle.getText().toString());
                task.setText(etText.getText().toString());
                task.setStatus(ModelTask.STATUS_CURRENT);
//                if (!(etDate.length() == 0 && etTime.length() == 0)) {
                task.setDate(calendar.getTimeInMillis());
                task.setImg(img);
                task.setStatus(ModelTask.STATUS_CURRENT);

                AlarmHelper.getInstance().setAlarm(task);
                AddingTaskDialogFragment.this.addingTaskListener.onTaskAdded(task);

                Log.v(LOG_TAG, "AddingTaskDialogFragment setPositiveButton " + calendar.toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AddingTaskDialogFragment.this.addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        final TextInputLayout textInputLayout = tilText;
        alertDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                if (etText.length() == 0) {
                    positiveButton.setEnabled(false);
                    //textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                }

                etText.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if ((s.length() == 0) || (etTime.getText().length() == 0) || (etDate.getText().length() == 0)) {
                            positiveButton.setEnabled(false);
                            //textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                            return;
                        }
                        positiveButton.setEnabled(true);
                        textInputLayout.setErrorEnabled(false);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });

                etDate.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if ((s.length() == 0) || (etTime.getText().length() == 0) || (etText.getText().length() == 0)) {
                            positiveButton.setEnabled(false);
                            //textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                            return;
                        }
                        positiveButton.setEnabled(true);
                        textInputLayout.setErrorEnabled(false);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });

                etTime.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if ((s.length() == 0) || (etText.getText().length() == 0) || (etDate.getText().length() == 0)) {
                            positiveButton.setEnabled(false);
                            //textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                            return;
                        }
                        positiveButton.setEnabled(true);
                        textInputLayout.setErrorEnabled(false);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });

            }
        });
        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
//        if (requestCode == REQUEST_DATE) {// ответ от диалога даты
//            Calendar date = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
//            if (date != null) {
////                calendar.set(Calendar.YEAR, date.getYear());
////                calendar.set(Calendar.MONTH, date.getMonth());
////                calendar.set(Calendar.DATE, date.getDate());
//                calendar = date;
//                String format = android.text.format.DateFormat.format("dd.MM.yy", date).toString();
//                etDate.setText(format);
//                Log.v(LOG_TAG, "AddingTaskDialogFragment onActivityResult " + etDate.toString());
//            }
//        } else
        if (requestCode == REQUEST_ICON) {
            String ret_filename = "return_filename";
            if (data != null) {
                String url_image = img = "";
                url_image = img = data.getStringExtra(ret_filename);

                Utils.refreshImageTask(getActivity(), btn_image, url_image);
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        String format = android.text.format.DateFormat.format("dd.MM.yy", calendar).toString();
        etDate.setText(format);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
    }
}
