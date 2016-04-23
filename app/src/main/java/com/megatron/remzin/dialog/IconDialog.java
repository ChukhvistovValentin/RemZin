package com.megatron.remzin.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.megatron.remzin.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IconDialog extends Activity {
    //    static int a = 11;
    static String ret_filename = "return_filename";
    public static String icons_dir = "icons";
    //    private  d;
    private boolean e = false;
    private ImageBaseAdapter imgBaseAdapter = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.title_dialog_icon));
//        this.d = new com.slayminex.shared_lib.a((Activity) this);
//        this.d.a(new String[]{"remove_ads"});
//        this.d.b();
//        this.e = d.d((Context) this).getBoolean("is_ok_billing_admob", false);
        GridView gridView = new GridView((Context) this);
        gridView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        try {
            ArrayList arrayList = new ArrayList((Collection) Arrays.asList((Object[]) this.getAssets().list(icons_dir)));
            if (!this.e) {
                Collections.sort((List) arrayList, (Comparator) new Comparator() {

                    public int a(String string, String string2) {
                        if (string.contains((CharSequence) "#")) {
                            return 1;
                        }
                        if (string2.contains((CharSequence) "#")) {
                            return -1;
                        }
                        return string.compareTo(string2);
                    }

                    public int compare(Object object, Object object2) {
                        return this.a((String) object, (String) object2);
                    }
                });
            }
//            int n = com.slayminex.shared_lib.b.c((Context) this, 5);
//            int n2 = com.slayminex.shared_lib.b.c((Context) this, 10);
            gridView.setColumnWidth(150);
            gridView.setNumColumns(GridView.AUTO_FIT);//-1
//            gridView.setGravity(Gravity.RIGHT);//17
            gridView.setPadding(5, 5, 5, 5);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH); //2
            gridView.setHorizontalSpacing(15);
            gridView.setVerticalSpacing(15);
            this.imgBaseAdapter = new ImageBaseAdapter((Context) this, (List) arrayList);
            gridView.setAdapter(imgBaseAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapterView, View view, int pos, long l2) {
                    View temp = view.findViewById(pos);
                    Object tag = temp.getTag();
                    String string = tag.toString();
//                    if (e || !string.contains((CharSequence) "#"))
                    {
//                        ((ApplicationGlobal) IconsActivity.this.getApplication()).a("\u0412\u044b\u0431\u043e\u0440 \u0438\u043a\u043e\u043d\u043a\u0438", string);
                        Intent intent = new Intent();
                        intent.putExtra(ret_filename, string);
                        IconDialog.this.setResult(Activity.RESULT_OK, intent);
                        IconDialog.this.finish();
                        return;
                    }
//                    IconDialog.this.d.a("remove_ads");
                }
            });
        } catch (IOException var4_6) {
            return;
        }
        this.setContentView((View) gridView);
    }

    //************************* CLASS ******************
    private class ImageBaseAdapter extends BaseAdapter {
        Context context;
        List listImage;

        public ImageBaseAdapter(Context context, List list) {
            this.context = context;
            this.listImage = list;
        }

        public int getCount() {
            return this.listImage.size();
        }

        public Object getItem(int n) {
            return this.listImage.get(n);
        }

        public long getItemId(int n) {
            return 0;
        }


        public View getView(int n, View view, ViewGroup viewGroup) {
            b b2;
            View view2;
            if (view == null) {
                b2 = new b();
                view2 = new FrameLayout(this.context);
                view2.setBackgroundResource((R.drawable.bg_btn_green));
//                int n2 = b2(this.context, 40);
                b2.a = new ImageView(this.context);
                b2.a.setLayoutParams(new FrameLayout.LayoutParams(100, 100, Gravity.CENTER
//                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT
                ));
                ((FrameLayout) view2).addView((View) b2.a);
//                b2.b = new ImageView(this.context);
//                ((RelativeLayout) view2).addView((View) b2.b);
                view2.setTag((Object) b2);
            } else {
                b b3 = (b) view.getTag();
                view2 = view;
                b2 = b3;
            }
//            view2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            b2.a.setId(n);
//            b2.a.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            IconDialog.ImageBaseAdapter(context, (String) this.listImage.get(n));
            //if (!IconDialog.this.e && ((String) this.listImage.get(n)).contains((CharSequence) "#")) {
//                int id = (int) b2.a.getId();
//                b2.b.setImageResource();
            try {
                InputStream ims = context.getAssets().open(icons_dir + "/" + listImage.get(n));
                Drawable d = Drawable.createFromStream(ims, null);
                // выводим картинку в ImageView
                b2.a.setImageDrawable(d);
                b2.a.setTag(this.listImage.get(n));
            } catch (IOException e) {

            }
//                b2.b.setImageDrawable();
            return view2;
            //}
//            b2.a.setImageDrawable(null);
//            return view2;
        }
    }

    static class b {
        public ImageView a;
//        public ImageView b;

        b() {
        }
    }

}
