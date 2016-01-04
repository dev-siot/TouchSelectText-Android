package com.jfsiot.touchselect.touchselecttest.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jfsiot.touchselect.touchselecttest.R;

import timber.log.Timber;

/**
 * Created by SSS on 2015-12-27.
 */
public class SelectableEditText extends EditText{

    public SelectableEditText(Context context) {
        super(context);
    }

    public SelectableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK && this.hasSelection()){
            this.clearFocus();
            this.setTextIsSelectable(false);
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
