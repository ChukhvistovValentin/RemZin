package com.megatron.remzin;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class ActivityNotification extends AppCompatActivity {
    private final static String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView title = (TextView) findViewById(R.id.notifi_title);
        TextView text = (TextView) findViewById(R.id.notifi_text);
        ImageView imageView = (ImageView) findViewById(R.id.img_notification);

        Intent intentRes = getIntent();
        title.setText(intentRes.getStringExtra("title"));
        text.setText(intentRes.getStringExtra("text"));
        String img_name = intentRes.getStringExtra("img");

        loadImgNotification(imageView, img_name);

        final int notifiID = intentRes.getIntExtra("ID", -1);

        Log.v(LOG_TAG, "ActivityNotification ID = " + notifiID);

        Button btnClose = (Button) findViewById(R.id.btn_close);
        Button btnView = (Button) findViewById(R.id.btn_view);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notifiID);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadImgNotification(ImageView img, String img_name) {
        if (!img_name.isEmpty()) {
            try {
                InputStream ims = getAssets().open("icons" + "/" + img_name);
                Drawable d = Drawable.createFromStream(ims, null);
                // выводим картинку в ImageView
                img.setImageDrawable(d);
            } catch (IOException e) {
//                img.setImageResource(R.drawable.ic_image_btn);
            }
        }
    }
}
