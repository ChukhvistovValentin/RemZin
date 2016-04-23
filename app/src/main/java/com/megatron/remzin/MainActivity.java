package com.megatron.remzin;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.megatron.remzin.adapter.TabAdapter;
import com.megatron.remzin.database.DBHelper;
import com.megatron.remzin.dialog.AddingTaskDialogFragment;
import com.megatron.remzin.dialog.AddingTaskDialogFragment.AddingTaskListener;
import com.megatron.remzin.dialog.EditTaskDialogFragment.EditingTaskListener;
import com.megatron.remzin.fragment.CurrentTaskFragment;
import com.megatron.remzin.fragment.CurrentTaskFragment.OnTaskDoneListener;
import com.megatron.remzin.fragment.DoneTaskFragment;
import com.megatron.remzin.fragment.DoneTaskFragment.OnTaskRestoreListener;
import com.megatron.remzin.fragment.TaskFragment;
import com.megatron.remzin.model.ModelTask;

public class MainActivity extends AppCompatActivity implements AddingTaskListener, EditingTaskListener, OnTaskDoneListener, OnTaskRestoreListener {
    static final public String RECEIVER_MESSAGE = "com.megatron.remzin.RECEIVER_MESSAGE";
    static final public String RECEIVER_RESULT = "com.megatron.remzin.RECEIVER_RESULT";

    FragmentManager fragmentManager;
    public DBHelper dbHelper;
    AlarmHelper alarmHelper;
    TaskFragment currentTaskFragment;
    TabAdapter tabAdapter;
    //    SearchView searchView;
    TaskFragment doneTaskFragment;
    BroadcastReceiver updateReceiver;
    private SharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Intent serviceIntent = new Intent(getApplicationContext(), AlarmService.class);
//        getApplicationContext().startService(serviceIntent);

        AlarmHelper.getInstance().init(getApplicationContext());
        this.alarmHelper = AlarmHelper.getInstance();
        this.dbHelper = new DBHelper(getApplicationContext());
        this.fragmentManager = getFragmentManager();
        setUI();
        onCreateUpdateReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intentSetting;

        if (id == R.id.action_settings) {
            intentSetting = new Intent(this, SettingsActivity.class);
            startActivity(intentSetting);
            return true;
        } else if (id == R.id.action_exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            setSupportActionBar(toolbar);
        }
        // добавляем вкладки...
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal), getResources().getColor(R.color.tab_select)); //цвет активной и неактивной вкладки
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        this.tabAdapter = new TabAdapter(this.fragmentManager, 2);
        viewPager.setAdapter(this.tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        this.currentTaskFragment = (CurrentTaskFragment) this.tabAdapter.getItem(tabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        this.doneTaskFragment = (DoneTaskFragment) this.tabAdapter.getItem(tabAdapter.DONE_TASK_FRAGMENT_POSITION);
//        this.searchView = (SearchView) findViewById(R.id.searchView);
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabReselected(Tab tab) {
            }
        });
//        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            public boolean onQueryTextChange(String newText) {
//                MainActivity.this.currentTaskFragment.findTasks(newText);
//                MainActivity.this.doneTaskFragment.findTasks(newText);
//                return false;
//            }
//        });
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AddingTaskDialogFragment().show(MainActivity.this.fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    public void onTaskAdded(ModelTask newTask) {
        this.currentTaskFragment.addTask(newTask, true);
    }

    public void onTaskAddingCancel() {
    }

    public void onTaskDone(ModelTask task) {
        this.doneTaskFragment.addTask(task, false);
    }

    public void onTaskRestore(ModelTask task) {
        this.currentTaskFragment.addTask(task, false);
    }

    public void onBackPressed() {
//        if (this.searchView.isIconified()) {
        super.onBackPressed();
//        } else {
//            this.searchView.setIconified(true);
//        }
    }

    public void onTaskEdited(ModelTask newTask) {
        this.currentTaskFragment.updateTask(newTask);
        this.dbHelper.update().task(newTask);
        AlarmHelper.getInstance().setAlarm(newTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MegatronApplication.setPref(1);
    }

    @Override
    protected void onStop() {
        setPref(false);
        // закрываем ресивер
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MegatronApplication.setPref(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPref(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // запускаем ресивер
        LocalBroadcastManager.getInstance(this).registerReceiver((updateReceiver),
                new IntentFilter(RECEIVER_RESULT));
    }

    private void onCreateUpdateReceiver() {
        // обновление таска когда активная прога и сработал ресивер
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int id = intent.getIntExtra(RECEIVER_MESSAGE, -1);
                currentTaskFragment.updateTaskID(id);
            }
        };
    }

    private void setPref(boolean index) {
        if (mySharedPreferences == null)
            mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("visible", index);
        editor.apply();
    }
}
