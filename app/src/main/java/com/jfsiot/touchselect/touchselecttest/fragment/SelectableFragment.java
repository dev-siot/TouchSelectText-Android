package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.Toolbar.OnToolbarAction;
import com.jfsiot.touchselect.touchselecttest.customview.SelectableEditText;
import com.jfsiot.touchselect.touchselecttest.helper.TextOffsetHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by SSS on 2015-12-27.
 */
public abstract class SelectableFragment extends Fragment implements View.OnTouchListener, OnToolbarAction {
    @Bind(R.id.main_edit) protected SelectableEditText editText;
    @Bind(R.id.main_container) protected RelativeLayout container;

    protected List<Integer> mainIndexList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    abstract protected String[] getStringArrayResourse();

    @Override
    public void onResume() {
        super.onResume();
        init();
        this.editText.setOnTouchListener(this);
    }

    @Override
    public void OnToolbarAction(int action) {
        if(action == 2){
            init();
        }
    }

    protected void init(){
        String text = "";
        if(!mainIndexList.isEmpty())
            mainIndexList.clear();
        for(String str : this.getStringArrayResourse()){
            text += str;
            mainIndexList.add(text.length());
        }
        this.editText.setText(text);
        free();
    }

    protected void free(){
        posDownX = -1;
        posDownY = -1;
        this.editText.setTextIsSelectable(false);
        this.editText.clearFocus();
    }

    protected int posDownX, posDownY;
    protected float initScrollY;
    protected long downTime, tabTime = 0;
    protected int MAX_SCROLLABLE_Y_POSITION = -1;
    protected float posDownRawY, posDownRawX;
    protected int defaultTop = -1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int location[] = new int[2];
        if(!editText.isTextSelectable() && editText.hasSelection())
            free();
        if(defaultTop < 0){
            defaultTop = ((int) editText.getY());
        }
        Timber.d("touch : %s %s %s %s", editText.isTextSelectable(), editText.hasSelection(), editText.getSelectionStart(), editText.getSelectionEnd());
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.downTime = event.getEventTime();
            this.posDownX = ((int) event.getX());
            this.posDownY = ((int) event.getY()+editText.getScrollY());
            this.posDownRawX = event.getRawX();
            this.posDownRawY = event.getRawY();
            this.initScrollY = editText.getScrollY();
            MAX_SCROLLABLE_Y_POSITION =  TextOffsetHelper.getViewTotalHeight(this.editText) - editText.getHeight();
        }
        float diff = Math.abs(posDownX - event.getX() + posDownY - event.getY() - editText.getScrollY());
        float diffRaw = (float) Math.sqrt( Math.abs( Math.pow(posDownRawX - event.getRawX(), 2) + Math.pow(posDownRawY - event.getRawY(), 2) ) );
        float diffRawY = posDownRawY - event.getRawY();
        if (event.getAction() == MotionEvent.ACTION_MOVE/* && Math.abs(diffRawY) > 10*/) {
            if(((int) (diffRawY + initScrollY)) > MAX_SCROLLABLE_Y_POSITION) {
                this.editText.scrollTo(0, Math.max(MAX_SCROLLABLE_Y_POSITION, 0));
            }else if(((int) (diffRawY + initScrollY)) < 0){
                this.editText.scrollTo(0, 0);
            }else{
                this.editText.scrollTo(0, ((int) (diffRawY + initScrollY)));
            }
        }/*else if(event.getAction() == MotionEvent.ACTION_UP && Math.abs(diffRawY) > 10){
            TextOffsetHelper.getPositionLineOffset(location, this.editText, ((int) event.getRawX()), ((int) (posDownRawY + initScrollY)));
            editText.setSelection(location[1], location[1]);
        }*/


        if (editText.hasSelection()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }else if (event.getAction() == MotionEvent.ACTION_UP && diffRaw < 20) {
                if(event.getEventTime() - downTime < 150){
                    TextOffsetHelper.getPositionLineOffset(location, this.editText, posDownX, ((int) (posDownY + initScrollY)));
                    if(tabTime != 0 && event.getEventTime() - tabTime < 200){
                        tabTime = 0;
                        this.free();
                    }else {
                        tabTime = event.getEventTime();

                        /**
                         * Change selection range
                         */
                        float centerOfSelection =  editText.getSelectionStart() + (editText.getSelectionEnd() - editText.getSelectionStart()) / 2;
                        if(location[1] < editText.getSelectionStart()) {
                            this.touchOuter(true , location[1]);
                        } else if (location[1] > editText.getSelectionEnd()) {
                            this.touchOuter(false, location[1]);
                        } else if (location[1] <= centerOfSelection) {
                            this.touchInner(true, location[1]);
                        } else if (location[1] > centerOfSelection) {
                            this.touchInner(false, location[1]);
                        }
                    }
                    return true;
                }
            }
            return true;
        }else{
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                editText.setFocusable(true);
            }
            if (event.getEventTime() - downTime > 150 || diffRaw > 20) {
                free();
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE) {
                if(diffRaw< 20) {
                    editText.setTextIsSelectable(true);
                }else{
                    free();
                }
            }else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(diffRaw < 20) {
                    TextOffsetHelper.getPositionLineOffset(location, this.editText, ((int) event.getX()), ((int) event.getY() + editText.getScrollY()));
                    int start = TextOffsetHelper.getOffsetTextList(mainIndexList, location[1], true, editText.getText().toString() )
                            , end = TextOffsetHelper.getOffsetTextList(mainIndexList, location[1], false, editText.getText().toString());
                    editText.setSelection(start, end);
                    return true;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    protected void touchInner(boolean isLeft, int letterOffset){
        int offset, fixOffset;
        if(isLeft) {
            offset = TextOffsetHelper.getOffsetTextList(mainIndexList, letterOffset, true, editText.getText().toString());
            fixOffset = editText.getSelectionEnd();
            if (Math.abs(editText.getSelectionStart() - offset) > 1)
                editText.setSelection(offset, fixOffset);
        }else{
            offset = TextOffsetHelper.getOffsetTextList(mainIndexList, letterOffset, false, editText.getText().toString());
            fixOffset = editText.getSelectionStart();
            if(Math.abs(editText.getSelectionEnd() - offset) > 1)  editText.setSelection(fixOffset, offset);
        }
    }
    protected void touchOuter(boolean isLeft, int letterOffset){
        int offset, fixOffset;
        if(isLeft) {
            offset = TextOffsetHelper.getOffsetTextList(mainIndexList, letterOffset, true, editText.getText().toString());
            fixOffset = editText.getSelectionEnd();
            if (Math.abs(editText.getSelectionStart() - offset) > 1)
                editText.setSelection(offset, fixOffset);
        }else{
            offset = TextOffsetHelper.getOffsetTextList(mainIndexList, letterOffset, false, editText.getText().toString());
            fixOffset = editText.getSelectionStart();
            if(Math.abs(editText.getSelectionEnd() - offset) > 1)  editText.setSelection(fixOffset, offset);
        }
    }
}
