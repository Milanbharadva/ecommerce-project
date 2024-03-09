package com.webmintinfotech.ecom.ui.activity;

import android.view.View;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActImageSliderBinding;
import com.webmintinfotech.ecom.model.ProductimagesItem;

import java.util.ArrayList;

public class ActImageSlider extends BaseActivity {
    private ActImageSliderBinding imageSliderBinding;
    private ArrayList<ProductimagesItem> imgList = null;

    @Override
    public View setLayout() {
        return imageSliderBinding.getRoot();
    }

    @Override
    public void initView() {
        imageSliderBinding = ActImageSliderBinding.inflate(getLayoutInflater());
        imgList = getIntent().getParcelableArrayListExtra("imageList");
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
//            SlideModel slideModel = new SlideModel(imgList.get(i).getImageUrl(), ScaleTypes.CENTER_CROP);
//            imageList.add(slideModel);
        }
        imageSliderBinding.imageSlider.setImageList(imageList);
        imageSliderBinding.ivCancle.setOnClickListener(v -> finish());
    }
}
