package com.cgg.pps.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


@SuppressLint("AppCompatCustomView")
public class CustomFontEditText extends EditText {

    public CustomFontEditText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomFontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("OpenSans_Regular.ttf", context);
        setTypeface(customFont);
    }
}