package com.megatron.remzin.model;

import com.megatron.remzin.R;

import java.util.Date;

public class ModelTask implements Item {
    public static final int PRIORITY_HIGH = 2;
    public static final String[] PRIORITY_LEVELS = new String[]{"Low Priority", "Normal Priority", "High Priority"};
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;
    public static final int STATUS_OVERDUE = 0;

    public static final int TYPE_OVERDUE = 0;
    public static final int TYPE_TODAY = 1;
    public static final int TYPE_TOMORROW = 2;
    public static final int TYPE_FUTURE = 3;

    private long date;
    private int dateStatus;
    private int priority;
    private int status;
    private long timeStamp;
    private String title;
    private String text;
    private int vibrate_id;
    private int rington_id;
    private boolean notifi_length;
    private String img;

    public ModelTask() {
        this.status = -1;
        this.timeStamp = new Date().getTime();
    }

    public ModelTask(String title, String text, long date, int priority, int status, long timeStamp, String img) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
        this.img = img;
    }

    public int getPriorityColor() {
        switch (getPriority()) {
            case PRIORITY_LOW /*0*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_low;
                }
                return R.color.priority_low_selected;
            case STATUS_CURRENT /*1*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_normal;
                }
                return R.color.priority_normal_selected;
            case STATUS_DONE /*2*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_high;
                }
                return R.color.priority_high_selected;
            default:
                return PRIORITY_LOW;
        }
    }

    public boolean isTask() {
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDateStatus() {
        return this.dateStatus;
    }

    public void setDateStatus(int dateStatus) {
        this.dateStatus = dateStatus;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public Date getDateDate() {
        Date temp = new Date(this.date);
//        temp.setTime(this.date);
        return temp;
    }

    public void setRington_id(int rington_id) {
        this.rington_id = rington_id;
    }

    public int getRington_id() {
        return rington_id;
    }

    public void setVibrate_id(int vibrate_id) {
        this.vibrate_id = vibrate_id;
    }

    public int getVibrate_id() {
        return vibrate_id;
    }

    public void setNotifi_length(boolean notifi_length) {
        this.notifi_length = notifi_length;
    }

    public boolean isNotifi_length() {
        return notifi_length;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }
}
