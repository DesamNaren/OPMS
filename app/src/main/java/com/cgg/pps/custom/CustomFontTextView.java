package com.cgg.pps.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cgg.pps.R;

@SuppressLint("AppCompatCustomView")
public class CustomFontTextView extends TextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    // Style
    public static final int REGULAR = 0;
    public static final int SEMIBOLD = 1;
    public static final int MEDIUM = 2;
    public static final int LIGHT = 3;
    public static final int BOLD = 4;

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context, attrs);
    }

    @SuppressLint("NewApi")
    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context) {
        super(context);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_textFont);
        if (TextUtils.isEmpty(fontName)) {
            fontName = context.getString(R.string.font_name_source_opensans);
        }
        int textStyle = attributeArray.getInteger(R.styleable.CustomFontTextView_textStyle, REGULAR);
        //int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, fontName, textStyle);
        setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {
        /*if (fontName.contentEquals(context.getString(R.string.font_name_fontawesome))) {
            return FontCache.getTypeface("fontawesome.ttf", context);
        } else*/
        if (fontName.contentEquals(context.getString(R.string.font_name_source_opensans))) {
          /*
          information about the TextView textStyle:
          http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
          */
            switch (textStyle) {
                case LIGHT: // Light
                    return FontCache.getTypeface("OpenSans-Light.ttf", context);

                case MEDIUM: // Medium
                    return FontCache.getTypeface("OpenSans-Medium.ttf", context);

                case SEMIBOLD: // Semi Bold
                    return FontCache.getTypeface("OpenSans-Semibold.ttf", context);

                case BOLD: // Bold
                    return FontCache.getTypeface("OpenSans-Bold.ttf", context);

                case REGULAR: // regular
                    return FontCache.getTypeface("OpenSans-Regular.ttf", context);

                default:
                    return FontCache.getTypeface("OpenSans-Regular.ttf", context);
            }
        } else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }
    }
}