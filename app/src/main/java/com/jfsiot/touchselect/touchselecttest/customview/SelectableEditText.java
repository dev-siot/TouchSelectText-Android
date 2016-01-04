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
    private ExpandButton startCursor, endCursor;

    public SelectableEditText(Context context) {
        super(context);
        init();
    }

    public SelectableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        startCursor = new ExpandButton(getContext());
        endCursor = new ExpandButton(getContext());
        startCursor.setOrientation(ExpandButton.LEFT);
        endCursor.setOrientation(ExpandButton.RIGHT);
    }

    public void showCursor(int x, int y){
        startCursor.show(x, y);
        endCursor.show(x,y);
    }
    int oldStart =0, oldEnd =0;
    public void move(int start, int end){
        if(start != oldStart)
            moveStart(start);
        if(end != oldEnd)
            moveEnd(end);
        oldStart = start;
        oldEnd = end;
    }

    public void setLeftClick(OnClickListener listener){
        this.startCursor.setOnClickListener(listener);
    }
    public void setRightClick(OnClickListener listener){
        this.endCursor.setOnClickListener(listener);
    }

    private void moveStart(int start){
        int []location = new int[2];
        getLocationOnScreen(location);
        int line = getLayout().getLineForOffset(start);
        int top = getLayout().getLineBottom(line);
        int bottom = getLayout().getLineTop(line);
        int x = ((int) getLayout().getPrimaryHorizontal(start)) - 10;
        int y = (bottom - top)/2 + top + location[1];
        startCursor.setPosition(x, y);
    }

    private void moveEnd(int end){
        int []location = new int[2];
        getLocationOnScreen(location);
        int line = getLayout().getLineForOffset(end);
        int top = getLayout().getLineBottom(line);
        int bottom = getLayout().getLineTop(line);
        int x = ((int) getLayout().getPrimaryHorizontal(end)) + 60;
        int y = (bottom - top)/2 + top + location[1];
        endCursor.setPosition(x, y);
    }

    public void hide(){
        startCursor.hide();
        endCursor.hide();
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

    class ExpandButton extends View{
        private int posX, posY, mWidth, mHeight;
        private int mHotspotX, mHotspotY;
        private int orientation;
        public static final int LEFT = 0, RIGHT = 1;
        private Drawable mDrawable;
        private PopupWindow window;
        private boolean show;

        public ExpandButton(Context context) {
            super(context);
            init();
        }

        public ExpandButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ExpandButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            this.mDrawable = getResources().getDrawable(R.drawable.ic_navigation_white_24dp);
            mWidth = mDrawable.getIntrinsicWidth();
            mHeight = mDrawable.getIntrinsicHeight();
            this.orientation = LEFT;
            this.posY = -1;
            this.posX = -1;
            this.window = new PopupWindow(this);
            this.window.setClippingEnabled(false);
            this.window.setWidth(mWidth);
            this.window.setHeight(mHeight);
            mHotspotX = mWidth / 2;
            mHotspotY = 0;
        }
        private void setOrientation(int orientation){
            if(orientation <= 1 && orientation >= 0){
                this.orientation = orientation;
                if(orientation == LEFT) {
                    this.mDrawable = getResources().getDrawable(R.drawable.ic_arrow_left_white_24dp);
                } else {
                    this.mDrawable = getResources().getDrawable(R.drawable.ic_arrow_right_white_24dp);
                }
                this.mDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }else{
                this.orientation = LEFT;
            }
        }
        private void setPosition(int x, int y){
            this.posX = x;
            this.posY = y;
            Timber.d("hot %s %s", mHotspotX, mHotspotY);
            window.update(x, y, mWidth, mHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mDrawable.setBounds(0, 0, mWidth, mHeight);
            mDrawable.draw(canvas);
        }
        public void show(int x, int y){
            final int[] coords = new int[2];
            SelectableEditText.this.getLocationInWindow(coords);
            show = true;
            coords[0] += x - mHotspotX;
            coords[1] += y - mHotspotY;
            window.showAtLocation(SelectableEditText.this, Gravity.NO_GRAVITY, coords[0], coords[1]);
        }
        public void hide(){
            show = false;
            window.dismiss();
        }

    }

}
