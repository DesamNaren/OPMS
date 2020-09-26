package com.cgg.pps.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cgg.pps.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_layout, null);
            ImageView imageprogress = (ImageView) view.findViewById(R.id.imageprogress);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageprogress);
            Glide.with(context).load(R.drawable.loader).into(imageViewTarget);
            setContentView(view);
            if (getWindow() != null)
                this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private Dialog dialog;
//    private static final CustomProgressDialog ourInstance = new CustomProgressDialog();
//
//    public static CustomProgressDialog getInstance() {
//        return ourInstance;
//    }
//
//    private CustomProgressDialog() {
//    }
//
//    public void show(Context context) {
//        if (dialog != null && dialog.isShowing()) {
//            return;
//        }
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        dialog.setContentView(R.layout.custom_progress_layout);
//        ImageView imageprogress = dialog.findViewById(R.id.imageprogress);
//        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageprogress);
//        Glide.with(context).load(R.drawable.loader).into(imageViewTarget);
//        dialog.setCancelable(true);
//        dialog.show();
//    }
//
//    public void dismiss() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
}
