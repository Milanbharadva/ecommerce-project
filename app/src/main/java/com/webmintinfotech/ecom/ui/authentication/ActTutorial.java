package com.webmintinfotech.ecom.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActTutorialBinding;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;

public class ActTutorial extends BaseActivity {

    private ActTutorialBinding tutorialBinding;
    private ArrayList<Drawable> imagelist;

    @Override
    public View setLayout() {
        tutorialBinding = ActTutorialBinding.inflate(getLayoutInflater());
        return tutorialBinding.getRoot();
    }

    @Override
    public void initView() {
        imagelist = new ArrayList<>();
        imagelist.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pageone, null));
        imagelist.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pagetwo, null));
        imagelist.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pagethree, null));
        imagelist.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pagefour, null));

        tutorialBinding.viewPager.setAdapter(new StartScreenAdapter(ActTutorial.this, imagelist));
        tutorialBinding.tabLayout.setupWithViewPager(tutorialBinding.viewPager, true);

        tutorialBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == imagelist.size() - 1) {
                    tutorialBinding.tvBtnSkip.setText(getResources().getString(R.string.start_));
                } else {
                    tutorialBinding.tvBtnSkip.setText(getResources().getString(R.string.skip));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tutorialBinding.tvBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreference.setBooleanPref(ActTutorial.this, SharePreference.isTutorial, true);
                openActivity(ActMain.class);
                finish();
            }
        });
    }

    static class StartScreenAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<Drawable> mImagelist;

        StartScreenAdapter(Context context, ArrayList<Drawable> imagelist) {
            mContext = context;
            mImagelist = imagelist;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.row_tutorial, collection, false);

            ImageView iv = layout.findViewById(R.id.ivScreen);
            iv.setImageDrawable(mImagelist.get(position));

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mImagelist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}