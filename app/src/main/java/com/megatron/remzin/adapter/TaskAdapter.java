package com.megatron.remzin.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megatron.remzin.fragment.TaskFragment;
import com.megatron.remzin.model.Item;
import com.megatron.remzin.model.ModelSeparator;
import com.megatron.remzin.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskAdapter extends Adapter<ViewHolder> {
    public boolean containsSeparatorFuture;
    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    List<Item> items = new ArrayList();
    TaskFragment taskFragment;

    protected class SeparatorViewHolder extends ViewHolder {
        protected TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    protected class TaskViewHolder extends ViewHolder {
        protected TextView date;
        protected TextView time;
        //        protected CircleImageView priority;
        protected TextView title;
        protected TextView text;
        protected ImageView imageView;
        protected CardView cv;
        protected RelativeLayout rcv;
        protected ImageView img_task;

        public TaskViewHolder(View itemView, TextView title, TextView text, TextView date,
                              TextView time, ImageView imageView, CardView cv, RelativeLayout rcv,
                              ImageView img_task) {
            super(itemView);
            this.title = title;
            this.text = text;
            this.date = date;
            this.time = time;
            this.imageView = imageView;
//            this.priority = priority;
            this.cv = cv;
            this.rcv = rcv;
            this.img_task = img_task;
        }
    }

    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
    }

    public Item getItem(int position) {
        return (Item) this.items.get(position);
    }

    public void updateTask(ModelTask newTask) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isTask()) {
                if (newTask.getTimeStamp() == ((ModelTask) getItem(i)).getTimeStamp()) {
                    removeItem(i);
                    getTaskFragment().addTask(newTask, false);
                }
            }
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item) {
        this.items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int location) { // удалить по долгому нажатию
        if (location >= 0 && location <= getItemCount() - 1) {
            this.items.remove(location);
            notifyItemRemoved(location);
            if (location - 1 < 0 || location > getItemCount() - 1) {
                if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {
                    switch (((ModelSeparator) getItem(getItemCount() - 1)).getType()) {
                        case ModelSeparator.TYPE_FUTURE /*2131165243*/:
                            this.containsSeparatorFuture = false;
                            break;
                        case ModelSeparator.TYPE_OVERDUE /*2131165244*/:
                            this.containsSeparatorOverdue = false;
                            break;
                        case ModelSeparator.TYPE_TODAY /*2131165245*/:
                            this.containsSeparatorToday = false;
                            break;
                        case ModelSeparator.TYPE_TOMORROW /*2131165246*/:
                            this.containsSeparatorTomorrow = false;
                            break;
                    }
                    int loc = getItemCount() - 1;
                    this.items.remove(loc);
                    notifyItemRemoved(loc);
                }
            } else if (!getItem(location).isTask() && !getItem(location - 1).isTask()) {
                switch (((ModelSeparator) getItem(location - 1)).getType()) {
                    case ModelSeparator.TYPE_FUTURE /*2131165243*/:
                        this.containsSeparatorFuture = false;
                        break;
                    case ModelSeparator.TYPE_OVERDUE /*2131165244*/:
                        this.containsSeparatorOverdue = false;
                        break;
                    case ModelSeparator.TYPE_TODAY /*2131165245*/:
                        this.containsSeparatorToday = false;
                        break;
                    case ModelSeparator.TYPE_TOMORROW /*2131165246*/:
                        this.containsSeparatorTomorrow = false;
                        break;
                }
                this.items.remove(location - 1);
                notifyItemRemoved(location - 1);
            }
        }
    }

    public void removeAllItems() {
        if (getItemCount() != 0) {
            this.items = new ArrayList();
            notifyDataSetChanged();
            this.containsSeparatorOverdue = false;
            this.containsSeparatorToday = false;
            this.containsSeparatorTomorrow = false;
            this.containsSeparatorFuture = false;
        }
    }

    public int getItemCount() {
        return this.items.size();
    }

    public TaskFragment getTaskFragment() {
        return this.taskFragment;
    }

    public void findTaskID(int id) {// поиск и обновление даных когда прилож. запущено и сработал ресивер
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isTask()) {
                if (((int) ((ModelTask) getItem(i)).getTimeStamp()) == id) {
                    updateTask((ModelTask) getItem(i));
                    break;
                }
            }
        }
    }
}
