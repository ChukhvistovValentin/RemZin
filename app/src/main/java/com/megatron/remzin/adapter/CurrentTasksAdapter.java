package com.megatron.remzin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megatron.remzin.R;
import com.megatron.remzin.Utils;
import com.megatron.remzin.fragment.CurrentTaskFragment;
import com.megatron.remzin.model.Item;
import com.megatron.remzin.model.ModelSeparator;
import com.megatron.remzin.model.ModelTask;

import java.util.Calendar;

public class CurrentTasksAdapter extends TaskAdapter {
    private final static String LOG_TAG = "myLogs";
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_TASK = 0;
    private boolean click_task = false;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_activity_main, viewGroup, false);
                return new TaskViewHolder(v,
                        (TextView) v.findViewById(R.id.text_title),
                        (TextView) v.findViewById(R.id.text_current),
                        (TextView) v.findViewById(R.id.text_date),
                        (TextView) v.findViewById(R.id.text_time),
                        (ImageView) v.findViewById(R.id.icon_active),
                        (CardView) v.findViewById(R.id.cv),
                        (RelativeLayout) v.findViewById(R.id.rcv),
                        (ImageView) v.findViewById(R.id.img_main));
            case TYPE_SEPARATOR:
                View separator = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_separator, viewGroup, false);
                return new SeparatorViewHolder(separator, (TextView) separator.findViewById(R.id.tvSeparatorName));
            default:
                return null;
        }
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Item item = (Item) this.items.get(position);
        final Resources resources = viewHolder.itemView.getResources();
        if (item.isTask()) {
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
            final View itemView = taskViewHolder.itemView;
            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.text.setText(task.getText());
            // дата и время (по отдельности)
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getShortDate(task.getDate()));
                taskViewHolder.time.setText(Utils.getTime(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }
            itemView.setVisibility(View.VISIBLE);

            Utils.refreshImageTask(getTaskFragment().getActivity(), taskViewHolder.img_task, task.getImg());

            // разукраска *************************************
            int color_title = resources.getColor(R.color.primary_text_default_material_light);
            int color_date = resources.getColor(R.color.secondary_text_default_material_light);

            Calendar curent_date = Calendar.getInstance();
            curent_date.setTimeInMillis(task.getDate());

            if (getTaskPosition(curent_date, ModelTask.TYPE_OVERDUE)) {
                //прошедшие
//                taskViewHolder.cv.setCardBackgroundColor(resources.getColor(R.color.task_overdue));
                taskViewHolder.rcv.setBackgroundResource(R.drawable.fon_blue);
                color_title = resources.getColor(R.color.title_overdue);
                color_date = resources.getColor(R.color.date_overdue);
            } else if (getTaskPosition(curent_date, ModelTask.TYPE_TODAY)) {
                // сегодня
//                taskViewHolder.cv.setCardBackgroundColor(resources.getColor(R.color.task_today));
                taskViewHolder.rcv.setBackgroundResource(R.drawable.fon_green);
//                color_title = resources.getColor(R.color.title_today);
//                color_date = resources.getColor(R.color.date_today);
                color_title = resources.getColor(R.color.title_future);
                color_date = resources.getColor(R.color.date_future);
            } else if (getTaskPosition(curent_date, ModelTask.TYPE_TOMORROW)) {
                // завтра
//                taskViewHolder.cv.setCardBackgroundColor(resources.getColor(R.color.task_tomorrow));
                taskViewHolder.rcv.setBackgroundResource(R.drawable.fon_yellow);
//                color_title = resources.getColor(R.color.title_tomorrow);
//                color_date = resources.getColor(R.color.date_tomorrow);
                color_title = resources.getColor(R.color.title_future);
                color_date = resources.getColor(R.color.date_future);
            } else if (getTaskPosition(curent_date, ModelTask.TYPE_FUTURE)) {
                // будущее
//                taskViewHolder.cv.setCardBackgroundColor(resources.getColor(R.color.task_future));
                taskViewHolder.rcv.setBackgroundResource(R.drawable.fon_pink);
                color_title = resources.getColor(R.color.title_future);
                color_date = resources.getColor(R.color.date_future);
            }//***********************************************
            taskViewHolder.title.setTextColor(color_title);
            taskViewHolder.text.setTextColor(color_title);
            taskViewHolder.date.setTextColor(color_date);
            taskViewHolder.time.setTextColor(color_date);

            Utils.setFontTextViev(taskFragment.activity, taskViewHolder.title, "font_separator1");
            Utils.setFontTextViev(taskFragment.activity, taskViewHolder.text, "font_separator2");
            Utils.setFontTextViev(taskFragment.activity, taskViewHolder.time, "font_separator17");
            //11, 14,15,16

//            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
            itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            CurrentTasksAdapter.this.getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);
                    return true;
                }
            });
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
//                    Intent intent = new Intent(getTaskFragment().getActivity(), IconDialog.class);
//                    getTaskFragment().startActivity(intent);

                    CurrentTasksAdapter.this.getTaskFragment().showTaskEditDialog(task);
                }
            });
            setClick_task(false);
            taskViewHolder.imageView.setImageResource(R.mipmap.ic_cb_green);
            taskViewHolder.imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // удалить событие по чекбоксу (сделать неактивным)
                    if (!getClick_task()) { // запрет несколько раз клацать
                        setClick_task(true);
                        ImageViewAnimatedChange(taskFragment.activity, taskViewHolder.imageView,
                                R.mipmap.ic_cb_red, viewHolder.getLayoutPosition(), task);

                        task.setStatus(task.STATUS_DONE);
                        CurrentTasksAdapter.this.getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), task.STATUS_DONE);
                        CurrentTasksAdapter.this.getTaskFragment().moveTask(task);
                    }
                }
            });

//            taskViewHolder.priority.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    taskViewHolder.priority.setEnabled(false);
//                    task.setStatus(2);
//                    CurrentTasksAdapter.this.getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), 2);
//                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
//                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
//                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", new float[]{-180.0f, 0.0f});
//                    flipIn.addListener(new AnimatorListener() {
//                        public void onAnimationStart(Animator animation) {
//                        }
//
//                        public void onAnimationEnd(Animator animation) {
//                            if (task.getStatus() == 2) {
//                                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);
//                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{0.0f, (float) itemView.getWidth()});
//                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{(float) itemView.getWidth(), 0.0f});
//                                translationX.addListener(new AnimatorListener() {
//                                    public void onAnimationStart(Animator animation) {
//                                    }
//
//                                    public void onAnimationEnd(Animator animation) {
//                                        itemView.setVisibility(8);
//                                        CurrentTasksAdapter.this.getTaskFragment().moveTask(task);
//                                        CurrentTasksAdapter.this.removeItem(taskViewHolder.getLayoutPosition());
//                                    }
//
//                                    public void onAnimationCancel(Animator animation) {
//                                    }
//
//                                    public void onAnimationRepeat(Animator animation) {
//                                    }
//                                });
//                                AnimatorSet translationSet = new AnimatorSet();
//                                translationSet.play(translationX).before(translationXBack);
//                                translationSet.start();
//                            }
//                        }
//
//                        public void onAnimationCancel(Animator animation) {
//                        }
//
//                        public void onAnimationRepeat(Animator animation) {
//                        }
//                    });
//                    flipIn.start();
//                }
//            });
            return;
        }
        // сепаратор ...
        ((SeparatorViewHolder) viewHolder).type.setText(resources.getString(((ModelSeparator) item).getType()));
        Utils.setFontTextViev(taskFragment.activity, ((SeparatorViewHolder) viewHolder).type, "font_separator4");
    }

    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return TYPE_TASK;
        }
        return TYPE_SEPARATOR;
    }

    // анимация кружочка
    private void ImageViewAnimatedChange(final Context c, final ImageView v, final int new_image, final int position, final ModelTask task) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.scale_in);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.scale_out);

        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                Log.v("myLogs", "onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                Log.v("myLogs", "onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
//                        Log.v("myLogs", "onAnimationStart2");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
//                        Log.v("myLogs", "onAnimationRepeat2");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        try {
                            removeItem(position);
                            setClick_task(false);
//                            CurrentTasksAdapter.this.removeItem(taskViewHolder.getLayoutPosition());
                        } catch (Exception e) {
//                            Log.d("myLogs", e.toString());
                        }
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    private void setClick_task(boolean click_task) {
        this.click_task = click_task;
    }

    private boolean getClick_task() {
        return this.click_task;
    }

    private boolean getTaskPosition(Calendar task_calendar, int pos) {
        Calendar calendar_data = Calendar.getInstance();

        int task_Year = task_calendar.get(task_calendar.YEAR);
        int task_Day = task_calendar.get(task_calendar.DAY_OF_MONTH);
        int task_Mon = task_calendar.get(task_calendar.MONTH);

        int calr_Year = calendar_data.get(calendar_data.YEAR);
        int calr_Day = calendar_data.get(calendar_data.DAY_OF_MONTH);
        int calr_Mon = calendar_data.get(calendar_data.MONTH);
//        Log.v(LOG_TAG, "***" + task.getTitle() + "***");
//        Log.v(LOG_TAG, "curent_date:" + task_Day + "." + (task_Mon + 1) + "." + task_Year +
//                " hh:mm:ss = " + task_H + ":" + task_M + ":" + task_S);
//        Log.v(LOG_TAG, "calendar_data:" + calr_Day + "." + (calr_Mon + 1) + "." + calr_Year +
//                " hh:mm:ss = " + calr_H + ":" + calr_M + ":" + calr_S);
//        Log.v(LOG_TAG, "***---***");

        switch (pos) {
            case ModelTask.TYPE_OVERDUE://прошедшие
                return task_calendar.before(calendar_data);

            case ModelTask.TYPE_TODAY: //сегодня
                return (task_Day == calr_Day) && (task_Mon == calr_Mon) && (task_Year == calr_Year);

            case ModelTask.TYPE_TOMORROW: //завтра
                calendar_data.add(Calendar.DAY_OF_MONTH, 1);
                Log.v(LOG_TAG, Utils.getShortDate(task_calendar.getTimeInMillis()) +
                        "  ||  " + Utils.getShortDate(calendar_data.getTimeInMillis()));

                return (task_Day == calendar_data.get(calendar_data.DAY_OF_MONTH)) &&
                        (task_Mon == calendar_data.get(calendar_data.MONTH)) && (task_Year == calr_Year);

            case ModelTask.TYPE_FUTURE://будущие
                calendar_data.add(Calendar.DAY_OF_MONTH, 1);
                return task_calendar.after(calendar_data);

            default:
                return false;
        }
    }

}
