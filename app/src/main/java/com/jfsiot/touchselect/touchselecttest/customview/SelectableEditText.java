package com.jfsiot.touchselect.touchselecttest.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;
import android.widget.EditText;

import com.jfsiot.touchselect.touchselecttest.R;

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

//    private ActionMode mActionMode;
//    private ActionMode.Callback mActionModeCallback;
//
//    @Override
//    public ActionMode startActionMode(ActionMode.Callback callback) {
//        ViewParent parent = getParent();
//        if (parent == null) {
//            return null;
//        }
//        mActionModeCallback = new JustCopyActionCallBack();
//        return parent.startActionModeForChild(this, mActionModeCallback);
//    }

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

    private class JustCopyActionCallBack implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.cab_copy :

                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            clearFocus();
//            mActionMode = null;
        }
    }
}
