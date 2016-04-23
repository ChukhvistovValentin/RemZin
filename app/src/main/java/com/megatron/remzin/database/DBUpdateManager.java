package com.megatron.remzin.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.megatron.remzin.model.ModelTask;


public class DBUpdateManager {
    SQLiteDatabase database;

    DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    public void title(long timeStamp, String title) {
        update(DBHelper.TASK_TITLE_COLUMN, timeStamp, title);
    }

    public void text(long timeStamp, String text) {
        update(DBHelper.TASK_TEXT_COLUMN, timeStamp, text);
    }

    public void date(long timeStamp, long date) {
        update(DBHelper.TASK_DATE_COLUMN, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DBHelper.TASK_PRIORITY_COLUMN, timeStamp, (long) priority);
    }

    public void status(long timeStamp, int status) {
        update(DBHelper.TASK_STATUS_COLUMN, timeStamp, (long) status);
    }

    public void image(long timeStamp, String img) {
        update(DBHelper.TASK_IMG_COLUMN, timeStamp, img);
    }

    public void task(ModelTask task) {
        title(task.getTimeStamp(), task.getTitle());
        text(task.getTimeStamp(), task.getText());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
        image(task.getTimeStamp(), task.getImg());
    }

    private void update(String column, long key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        this.database.update(DBHelper.TASKS_TABLE, cv, "task_time_stamp = " + key, null);
    }

    private void update(String column, long key, long value) {
        ContentValues cv = new ContentValues();
        cv.put(column, Long.valueOf(value));
        this.database.update(DBHelper.TASKS_TABLE, cv, "task_time_stamp = " + key, null);
    }
}
