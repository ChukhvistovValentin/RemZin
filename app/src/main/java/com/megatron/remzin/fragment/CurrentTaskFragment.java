package com.megatron.remzin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megatron.remzin.R;
import com.megatron.remzin.Utils;
import com.megatron.remzin.adapter.CurrentTasksAdapter;
import com.megatron.remzin.database.DBHelper;
import com.megatron.remzin.model.ModelSeparator;
import com.megatron.remzin.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CurrentTaskFragment extends TaskFragment {
    private final static String LOG_TAG = "myLogs";
    OnTaskDoneListener onTaskDoneListener;
//    MegatronService mService;
//    boolean mBound = false;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask modelTask);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onTaskDoneListener = (OnTaskDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTaskDoneListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new CurrentTasksAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }

    public void findTasks(String title) {
//        checkAdapter();
//        this.adapter.removeAllItems();
//        List<ModelTask> tasks = new ArrayList();
//        tasks.addAll(this.activity.dbHelper.query().getTasks(
//                this.activity.dbHelper.SELECTION_LIKE_TITLE + " AND " +
//                        this.activity.dbHelper.SELECTION_STATUS + " OR " +
//                        this.activity.dbHelper.SELECTION_STATUS,
//                new String[]{"%" + title + "%", Integer.toString(1), Integer.toString(0)}, DBHelper.TASK_DATE_COLUMN));
//        for (int i = 0; i < tasks.size(); i++) {
//            addTask((ModelTask) tasks.get(i), false);
//        }
    }

    public void addTaskFromDB() {
        checkAdapter();
        this.adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList();
        tasks.addAll(this.activity.dbHelper.query().getTasks(
                this.activity.dbHelper.SELECTION_STATUS,//+ " OR " + this.activity.dbHelper.SELECTION_STATUS,
                new String[]{Integer.toString(1)/*, Integer.toString(0)*/}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask((ModelTask) tasks.get(i), false);
        }
    }

    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;
        ModelSeparator separator = null;
        checkAdapter();
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            if (this.adapter.getItem(i).isTask()) {
                if (newTask.getDate() < ((ModelTask) this.adapter.getItem(i)).getDate()) {
                    position = i;
                    break;
                }
            }
        }
        if (newTask.getDate() != 0) {
            Calendar task_calendar = Calendar.getInstance();
            task_calendar.setTimeInMillis(newTask.getDate());

            if (setTaskSeparator(task_calendar, ModelSeparator.TYPE_OVERDUE)) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if (!this.adapter.containsSeparatorOverdue) {
                    this.adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (setTaskSeparator(task_calendar, ModelSeparator.TYPE_TODAY)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
                if (!this.adapter.containsSeparatorToday) {
                    this.adapter.containsSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (setTaskSeparator(task_calendar, ModelSeparator.TYPE_TOMORROW)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
                if (!this.adapter.containsSeparatorTomorrow) {
                    this.adapter.containsSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (setTaskSeparator(task_calendar, ModelSeparator.TYPE_FUTURE)) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if (!this.adapter.containsSeparatorFuture) {
                    this.adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
//            if (task_calendar.get(task_calendar.DAY_OF_YEAR) < Calendar.getInstance().get(task_calendar.DAY_OF_YEAR) &&
//                    task_calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() ||
//                    // если сегодня и прошло времья
//                    (task_calendar.get(task_calendar.DAY_OF_YEAR) == Calendar.getInstance().get(task_calendar.DAY_OF_YEAR) &&
//                            task_calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())) {
//                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
//                if (!this.adapter.containsSeparatorOverdue) {
//                    this.adapter.containsSeparatorOverdue = true;
//                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
//                }
//                // сегодня
//            } else if (task_calendar.get(task_calendar.DAY_OF_YEAR) == Calendar.getInstance().get(task_calendar.DAY_OF_YEAR) && task_calendar.get(task_calendar.YEAR) == Calendar.getInstance().get(task_calendar.YEAR)) {
//                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
//                if (!this.adapter.containsSeparatorToday) {
//                    this.adapter.containsSeparatorToday = true;
//                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
//                }
//                // завтра
//            } else if (task_calendar.get(task_calendar.DAY_OF_YEAR) == Calendar.getInstance().get(task_calendar.DAY_OF_YEAR) + 1 && task_calendar.get(task_calendar.YEAR) == Calendar.getInstance().get(task_calendar.YEAR)) {
//                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
//                if (!this.adapter.containsSeparatorTomorrow) {
//                    this.adapter.containsSeparatorTomorrow = true;
//                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
//                }
//                //будущие
//            } else if (task_calendar.get(task_calendar.DAY_OF_YEAR) > Calendar.getInstance().get(task_calendar.DAY_OF_YEAR) + 1 || task_calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
//                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
//                if (!this.adapter.containsSeparatorFuture) {
//                    this.adapter.containsSeparatorFuture = true;
//                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
//                }
//            }
        }
        if (position != -1) {
            if (!this.adapter.getItem(position - 1).isTask()) {
                if (position - 2 < 0 || !this.adapter.getItem(position - 2).isTask()) {
                    if (position - 2 < 0 && newTask.getDate() == 0) {
                        position--;
                    }
                } else if (((ModelTask) this.adapter.getItem(position - 2)).getDateStatus() == newTask.getDateStatus()) {
                    position--;
                }
            }
            if (separator != null) {
                this.adapter.addItem(position - 1, separator);
            }
            this.adapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                this.adapter.addItem(separator);
            }
            this.adapter.addItem(newTask);
        }
        if (saveToDB) {
            this.activity.dbHelper.saveTask(newTask);
        }
    }

    public void moveTask(ModelTask task) {
        this.alarmHelper.removeAlarm(task.getTimeStamp());
        this.onTaskDoneListener.onTaskDone(task);
    }

    public void checkAdapter() {
        if (this.adapter == null) {
            this.adapter = new CurrentTasksAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void setGreenIcon() {

    }

    private boolean setTaskSeparator(Calendar calendar_task, int type) {
        Calendar currentDate = Calendar.getInstance();
        Boolean res = false;
//        int day = currentDate.get(Calendar.DAY_OF_MONTH);
//        currentDate.add(Calendar.DAY_OF_MONTH, 1);
//
//        Log.v(LOG_TAG, Utils.getShortDate(calendar_task.getTimeInMillis()) +
//                "  ||  " + Utils.getShortDate(currentDate.getTimeInMillis()));
        switch (type) {
            case ModelSeparator.TYPE_OVERDUE:
                res = calendar_task.before(currentDate);
                return res;

            case ModelSeparator.TYPE_TODAY:
                res = ((calendar_task.get(calendar_task.DAY_OF_MONTH) == currentDate.get(currentDate.DAY_OF_MONTH)) &&
                        (calendar_task.get(calendar_task.MONTH) == currentDate.get(currentDate.MONTH)) &&
                        (calendar_task.get(calendar_task.YEAR) == currentDate.get(currentDate.YEAR))
                );
                return res;

            case ModelSeparator.TYPE_TOMORROW:
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                res = ((calendar_task.get(calendar_task.DAY_OF_MONTH) == currentDate.get(currentDate.DAY_OF_MONTH)) &&
                        (calendar_task.get(calendar_task.MONTH) == currentDate.get(currentDate.MONTH)) &&
                        (calendar_task.get(calendar_task.YEAR) == currentDate.get(currentDate.YEAR))
                );
                return res;

            case ModelSeparator.TYPE_FUTURE:
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                res = calendar_task.after(currentDate);
                return res;

            default:
                return res;
        }

    }

}
