package com.ruitu.entrance_guard.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by wubin on 2017/5/10.
 */
//轮播广告adapter
public class AdAdapter extends PagerAdapter {
    private Activity activity;
    private int[] imgResIds;

    public AdAdapter(Activity activity, int[] imgResIds) {
        this.activity = activity;
        this.imgResIds = imgResIds;
    }

    @Override
    public int getCount() {
        return imgResIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(activity.getApplicationContext());
        iv.setImageResource(imgResIds[position]);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}