package com.example.sss.touchselecttest.customview;

import android.content.Context;
import android.util.AttributeSet;
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
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
    }
}
