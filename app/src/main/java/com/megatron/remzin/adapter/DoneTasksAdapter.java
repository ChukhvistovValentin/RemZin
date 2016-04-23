package com.megatron.remzin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megatron.remzin.R;
import com.megatron.remzin.Utils;
import com.megatron.remzin.fragment.DoneTaskFragment;
import com.megatron.remzin.model.Item;
import com.megatron.remzin.model.ModelTask;


public class DoneTasksAdapter extends TaskAdapter {
    private static TaskViewHolder taskViewHolder_;
    public static boolean click_task = false;

    public void setTaskViewHolder_(TaskViewHolder taskViewHolder_) {
        this.taskViewHolder_ = taskViewHolder_;
    }

    public static TaskViewHolder getTaskViewHolder_() {
        return taskViewHolder_;
    }

    public DoneTasksAdapter(DoneTaskFragment taskFragment) {
        super(taskFragment);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_activity_done, viewGroup, false);
        return new TaskViewHolder(v,
                (TextView) v.findViewById(R.id.text_title_done),
                (TextView) v.findViewById(R.id.text_current_done),
                (TextView) v.findViewById(R.id.text_date_done),
                (TextView) v.findViewById(R.id.text_time_done),
                (ImageView) v.findViewById(R.id.icon_active_done),
                (CardView) v.findViewById(R.id.cv_done),
                (RelativeLayout) v.findViewById(R.id.rcv_done),
                (ImageView) v.findViewById(R.id.img_done));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Item item = (Item) this.items.get(position);
        if (item.isTask()) {
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();
            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.text.setText(task.getText());
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getShortDate(task.getDate()));
                taskViewHolder.time.setText(Utils.getTime(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }
            Utils.refreshImageTask(getTaskFragment().getActivity(), taskViewHolder.img_task, task.getImg());

            // разукраска *************************************
            int color_title = resources.getColor(R.color.title_overdue);
            int color_date = resources.getColor(R.color.date_overdue);

//            taskViewHolder.cv.setCardBackgroundColor(resources.getColor(R.color.task_overdue));
            itemView.setVisibility(View.VISIBLE);
//            taskViewHolder.priority.setEnabled(true);
            taskViewHolder.title.setTextColor(color_title);
            taskViewHolder.text.setTextColor(color_title);
            taskViewHolder.date.setTextColor(color_date);
            taskViewHolder.time.setTextColor(color_date);

//            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);
            setClick_task(false);
            taskViewHolder.imageView.setImageResource(R.mipmap.ic_cb_green);
            taskViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getClick_task()) {
                        setClick_task(true);
                        setTaskViewHolder_(taskViewHolder);
                        ImageViewAnimatedChange(taskFragment.activity, taskViewHolder.imageView,
                                R.mipmap.ic_cb_red, viewHolder.getLayoutPosition(), task);
                    }
                }
            });

            itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DoneTasksAdapter.this.getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);
                    return true;
                }
            });
//            taskViewHolder.priority.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    taskViewHolder.priority.setEnabled(false);
//                    task.setStatus(1);
//                    DoneTasksAdapter.this.getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), 1);
//                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
//                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
//                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", new float[]{180.0f, 0.0f});
//                    taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
//                    flipIn.addListener(new AnimatorListener() {
//                        public void onAnimationStart(Animator animation) {
//                        }
//
//                        public void onAnimationEnd(Animator animation) {
//                            if (task.getStatus() != 2) {
//                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{0.0f, (float) (-itemView.getWidth())});
//                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{(float) (-itemView.getWidth()), 0.0f});
//                                translationX.addListener(new AnimatorListener() {
//                                    public void onAnimationStart(Animator animation) {
//                                    }
//
//                                    public void onAnimationEnd(Animator animation) {
//                                        itemView.setVisibility(8);
//                                        DoneTasksAdapter.this.getTaskFragment().moveTask(task);
//                                        DoneTasksAdapter.this.removeItem(taskViewHolder.getLayoutPosition());
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
        }
    }

    // анимация кружочка
    public void ImageViewAnimatedChange(final Context c, final ImageView v, final int new_image, final int position, final ModelTask task) {
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
                            DoneTasksAdapter.this.getTaskFragment().getShowTaskEditDialog(DoneTasksAdapter.this.taskFragment, task, position);
                            setClick_task(false);
// removeItem(position);
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

    public static void setGreenIcon_() {
        // при отмене редактирования восстановления таска
        getTaskViewHolder_().imageView.setImageResource(R.mipmap.ic_cb_green);
        setClick_task(false);
    }

    private static void setClick_task(boolean click_task_) {
        click_task = click_task_;
    }

    private boolean getClick_task() {
        return this.click_task;
    }
}
