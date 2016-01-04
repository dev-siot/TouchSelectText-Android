package com.jfsiot.touchselect.touchselecttest.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

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
            this.setSelection(this.getSelectionStart());
            this.clearFocus();
            this.setFocusable(false);
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
