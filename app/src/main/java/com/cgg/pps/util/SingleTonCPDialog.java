package com.cgg.pps.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cgg.pps.R;

public class SingleTonCPDialog extends Dialog {
    
    private SingleTonCPDialog(Context context){
        super(context); 
    }
    
//    public SingleTonCPDialog(Context context) {
//        super(context);
//        try {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_layout, null);
//            ImageView imageprogress = (ImageView)view.findViewById(R.id.imageprogress);
//            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageprogress);
//            Glide.with(context).load(R.drawable.loader).into(imageViewTarget);
//            setContentView(view);
//            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            setCancelable(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
