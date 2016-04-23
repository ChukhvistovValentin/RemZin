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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.megatron.remzin.R;
import com.megatron.remzin.Utils;
import com.megatron.remzin.fragment.TaskFragment;
import com.megatron.remzin.model.ModelTask;

import java.util.Calendar;

public class EditTaskDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final static String LOG_TAG = "myLogs";
    public static final String EXTRA_DATE = "com.megatron.remzin.dialog.EditTaskDialogFragment";
    private EditingTaskListener editingTaskListener;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_ICON = 1;
    EditText etDate;
    EditText etTime;
    private Calendar calendar;
    private Calendar calendar2;
    ImageView btn_image;
    String img;

    public interface EditingTaskListener {
        void onTaskEdited(ModelTask modelTask);
    }

    public static EditTaskDialogFragment newInstance(ModelTask task) {
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", task.getTitle());
        args.putString("text", task.getText());
        args.putLong("date", task.getDate());
        args.putInt("priority", task.getPriority());
        args.putLong("timeStamp", task.getTimeStamp());
        args.putString("img", task.getImg());
        editTaskDialogFragment.setArguments(args);
        return editTaskDialogFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.editingTaskListener = (EditingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTaskListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ModelTask task = new ModelTask(args.getString("title"), args.getString("text"),
                args.getLong("date", 0), args.getInt("priority", 0), 0, args.getLong("timeStamp", 0),
                args.getString("img"));
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dialog_editing_title);
        View container = getActivity().getLayoutInflater().inflate(R.layout.dialog_task, null);
        TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();
        TextInputLayout tilText = (TextInputLayout) container.findViewById(R.id.tilDialogTaskText);
        final EditText etText = tilText.getEditText();
        btn_image = (ImageView) container.findViewById(R.id.btn_image);
        img = task.getImg();
        Utils.refreshImageTask(getActivity(), btn_image, img);

        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogTaskDate);
        etDate = tilDate.getEditText();
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTime);
//        final EditText
        etTime = tilTime.getEditText();
//        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPriority);
        etTitle.setText(task.getTitle());
        etTitle.setSelection(etTitle.length());
        etText.setText(task.getText());

        if (task.getDate() != 0) {
            etDate.setText(Utils.getDate(task.getDate()));
            etTime.setText(Utils.getTime(task.getDate()));
        }
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilText.setHint(getResources().getString(R.string.task_text));
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));
        builder.setView(container);
//        spPriority.setAdapter(new ArrayAdapter(getActivity(), 17367049, getResources().getStringArray(R.array.priority_levels)));
//        spPriority.setSelection(task.getPriority());
//        spPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                task.setPriority(position);
//            }
//
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
        calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY) + 1);
        if (!(etDate.length() == 0 && etTime.length() == 0)) {
            calendar.setTimeInMillis(task.getDate());
        }
        calendar2 = calendar;
        etDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (etDate.length() == 0) {
//                    etDate.setText(" ");
//                }
//                new DatePickerFragment() {
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        calendar2.set(calendar2.YEAR, year);
//                        calendar2.set(calendar2.MONTH, monthOfYear);
//                        calendar2.set(calendar2.DATE, dayOfMonth);
//                        etDate.setText(Utils.getDate(calendar2.getTimeInMillis()));
//                    }
//
//                    public void onCancel(DialogInterface dialog) {
//                        etDate.setText(null);
//                    }
//                }.show(EditTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
//                final String temp_date = etDate.getText().toString();
//                Date tdate = task.getDateDate();//Utils.getDateFromStr(temp_date);
                //***********************************
//                DatePickerFragment dialogFragment = new DatePickerFragment().newInstance(calendar);
//                dialogFragment.setTargetFragment(EditTaskDialogFragment.this, REQUEST_DATE);
//                dialogFragment.show(EditTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
                DatePickerDialog.newInstance(EditTaskDialogFragment.this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        etTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText("");
                }
//                new TimePickerFragment() {
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        calendar2.set(calendar2.HOUR_OF_DAY, hourOfDay);
//                        calendar2.set(calendar2.MINUTE, minute);
//                        calendar2.set(calendar2.SECOND, 0);
//                        etTime.setText(Utils.getTime(calendar2.getTimeInMillis()));
//                    }
//
//                    public void onCancel(DialogInterface dialog) {/*etTime.setText(null);*/ }
//                }.show(EditTaskDialogFragment.this.getFragmentManager(), "TimePickerFragment");
                TimePickerDialog.newInstance(EditTaskDialogFragment.this,
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

        final ModelTask modelTask = task;
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                modelTask.setTitle(etTitle.getText().toString());
                modelTask.setText(etText.getText().toString());
                modelTask.setStatus(ModelTask.STATUS_CURRENT);
                modelTask.setImg(img);

                if (etDate.length() == 0 && etTime.length() == 0) {
                    modelTask.setDate(0);
                } else {
                    modelTask.setDate(calendar2.getTimeInMillis());
//                    modelTask.setTimeStamp(calendar2.getTimeInMillis());
                }

                EditTaskDialogFragment.this.editingTaskListener.onTaskEdited(modelTask);
//                AlarmHelper.getInstance().setAlarm(modelTask);
                dialog.dismiss();
                //*******************
                sendResult(Activity.RESULT_OK, modelTask);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                sendResult(Activity.RESULT_CANCELED, modelTask);
            }
        });
        AlertDialog alertDialog = builder.create();
        final TextInputLayout textInputLayout = tilTitle;
        alertDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                if (etText.length() == 0) {
                    positiveButton.setEnabled(false);
                    //textInputLayout.setError(EditTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                }
                etText.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            //textInputLayout.setError(EditTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
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
////                calendar2.set(date.getYear(), date.getMonth(), date.getDate());
//                calendar2 = date;
//                String format = android.text.format.DateFormat.format("dd.MM.yy", date).toString();
//                etDate.setText(format);
//                Log.v(LOG_TAG, "EditTaskDialogFragment onActivityResult format = " + date.toString());
//            }
//            Log.v(LOG_TAG, "EditTaskDialogFragment onActivityResult ***---***");
//        } else
        if (requestCode == REQUEST_ICON) {
            String ret_filename = "return_filename";
            if (data != null) {
                String url_image = img = "";
                url_image = img = data.getStringExtra(ret_filename);
                Utils.refreshImageTask(getActivity(), btn_image, url_image);
//                try {
//                    InputStream ims = getActivity().getAssets().open("icons" + "/" + url_image);
//                    Drawable d = Drawable.createFromStream(ims, null);
//                    // выводим картинку в ImageView
//                    btn_image.setImageDrawable(d);
//                    btn_image.setTag(url_image);
//                } catch (IOException e) {
//
//                }
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar2.set(year, monthOfYear, dayOfMonth);
        String format = android.text.format.DateFormat.format("dd.MM.yy", calendar2).toString();
        etDate.setText(format);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar2.set(Calendar.MINUTE, minute);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
    }

    private void sendResult(int resultCode, ModelTask modelTask) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, resultCode);

        TaskFragment.setModelTasfAfterEdit(modelTask);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }

}
