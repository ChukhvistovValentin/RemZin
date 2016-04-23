package com.megatron.remzin.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;

import com.megatron.remzin.AlarmHelper;
import com.megatron.remzin.MainActivity;
import com.megatron.remzin.R;
import com.megatron.remzin.adapter.TaskAdapter;
import com.megatron.remzin.dialog.EditTaskDialogFragment;
import com.megatron.remzin.model.Item;
import com.megatron.remzin.model.ModelTask;


public abstract class TaskFragment extends Fragment {
    public MainActivity activity;
    protected TaskAdapter adapter;
    protected AlarmHelper alarmHelper;
    protected LayoutManager layoutManager;
    protected RecyclerView recyclerView;
    private static final int REQUEST_EDIT_TASK = 1;

    private int position = -1;
    private static ModelTask temp_task;

    public abstract void addTask(ModelTask modelTask, boolean z);

    public abstract void addTaskFromDB();

    public abstract void checkAdapter();

    public abstract void findTasks(String str);

    public abstract void moveTask(ModelTask modelTask);

    public abstract void setGreenIcon();

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            this.activity = (MainActivity) getActivity();
        }
        this.alarmHelper = AlarmHelper.getInstance();
        addTaskFromDB();
    }

    public void updateTask(ModelTask task) {
        this.adapter.updateTask(task);
    }

    public void updateTaskID(int id) {
        this.adapter.findTaskID(id);
    }

    public void removeTaskDialog(int location) {
        Builder dialogBuilder = new Builder(getActivity());
        dialogBuilder.setMessage(R.string.dialog_removing_message);
        Item item = this.adapter.getItem(location);
        if (item.isTask()) {
            final long timeStamp = ((ModelTask) item).getTimeStamp();
            final boolean[] isRemoved = new boolean[]{false};
            final int i = location;
            dialogBuilder.setPositiveButton(R.string.dialog_remove, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TaskFragment.this.adapter.removeItem(i);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(TaskFragment.this.getActivity().findViewById(R.id.coordinator), R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        public void onClick(View v) {
                            TaskFragment.this.addTask(TaskFragment.this.activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                        public void onViewAttachedToWindow(View v) {
                        }

                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]) {
                                TaskFragment.this.alarmHelper.removeAlarm(timeStamp);
                                TaskFragment.this.activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        dialogBuilder.show();
    }

    public void showTaskEditDialog(ModelTask task) {
        EditTaskDialogFragment.newInstance(task).show(getActivity().getFragmentManager(), "EditingTaskDialogFragment");
    }

    public Boolean getShowTaskEditDialog(Fragment fragment, ModelTask task, int position) {
        // редактировать перед возвратом на текущие задачи
        this.position = position;
        EditTaskDialogFragment df = EditTaskDialogFragment.newInstance(task);
        df.setTargetFragment(fragment, REQUEST_EDIT_TASK);
        df.show(fragment.getFragmentManager(), "EditingTaskDialogFragment");
        return true;
    }

    public void removeAllTasks() {
        this.adapter.removeAllItems();
    }

    private int getPosition() {
        return position;
    }

    private ModelTask getTemp_task() {
        return temp_task;
    }

    public static void setModelTasfAfterEdit(ModelTask modelTask) {
        temp_task = modelTask;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EDIT_TASK:
                    this.adapter.removeItem(getPosition());
                    getTemp_task().setStatus(ModelTask.STATUS_CURRENT);
                    this.moveTask(getTemp_task());
                    break;
            }
        } else setGreenIcon();
    }

}
