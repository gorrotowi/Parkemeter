package com.gorrotowi.parkemeter.customelements;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gorrotowi.parkemeter.R;

/**
 * Created by gorro on 01/08/15.
 */
public class TextViewBariol extends TextView {

    Typeface tf;

    public TextViewBariol(Context context) {
        super(context);
        initStyle();
    }

    public TextViewBariol(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.custom_font,
                0, 0);
        try {
            int font = a.getInteger(R.styleable.custom_font_font_type, 0);
            initStyle(font);
        } finally {
            a.recycle();
        }

    }

    private void initStyle() {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Regular.otf");
        this.setTypeface(tf);
    }

    private void initStyle(int font) {

        switch (font) {
            case 0:
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Regular.otf");
                this.setTypeface(tf);
                break;
            case 1:
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.otf");
                this.setTypeface(tf);
                break;
            case 2:
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.otf");
                this.setTypeface(tf);
                break;
            default:
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Regular.otf");
                this.setTypeface(tf);
                break;
        }

    }

}
