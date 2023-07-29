package com.example.dindon.helpers.DiscoverHelpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.dindon.dtofront.ImageMetadata;
import com.example.dindon.helpers.CONSTANTS;


import java.util.List;

public class ImagePageAdapter extends PagerAdapter {
    private Context context;
    private List<ImageMetadata> images;

    public ImagePageAdapter(Context context, List<ImageMetadata> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        String urlNueva = CONSTANTS.URL_CONEXION + "/pisos/" + images.get(position).getRuta();
        Glide.with(context)
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
