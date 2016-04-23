package com.megatron.remzin.model;

import com.megatron.remzin.R;

public class ModelSeparator implements Item {
    public static final int TYPE_FUTURE = R.string.separator_future; //2131165243;
    public static final int TYPE_OVERDUE = R.string.separator_overdue;//2131165244;
    public static final int TYPE_TODAY = R.string.separator_today;//2131165245;
    public static final int TYPE_TOMORROW = R.string.separator_tomorrow;//2131165246;
    private int type;

    public ModelSeparator(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isTask() {
        return false;
    }
}
