package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.Toolbar.OnToolbarAction;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;
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
        View view = inflater.inflate(R.layout.fragment_editable, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    abstract protected String[] getStringArrayResourse();

    @Override
    public void onResume() {
        super.onResume();
        init();
        this.editText.setOnTouchListener(this);
        this.editText.setTextIsSelectable(true);
    }

    @Override
    public void OnToolbarAction(int action) {
        if(action == 2){
            init();
        }
    }
    private boolean adjustSpannalbe = false;
    public void setAdjustSpannable(boolean setting){
        this.adjustSpannalbe = setting;
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
        if(adjustSpannalbe){
            if(((MainActivity) getActivity()).getCurrentTextStatue() == 1) {
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                builder.setSpan(new UnderlineSpan(), 97, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 188, 196, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 324, 409, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 467, 531, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 606, 835, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 839, 918, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                this.editText.setText(builder);
            }else if(((MainActivity) getActivity()).getCurrentTextStatue() == 0){
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                builder.setSpan(new UnderlineSpan(), 92, 116, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD | Typeface.ITALIC), 259, 284, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new UnderlineSpan(), 207, 328, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                this.editText.setText(builder);
            }
        }
    }

    protected void free(){
        posDownX = -1;
        posDownY = -1;
        this.editText.setSelection(editText.getSelectionStart());
        this.editText.clearFocus();
//        editText.setFocusable(false);
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
        Timber.d("touch : %s %s %s %s %s %s", editText.isTextSelectable(), editText.hasSelection(), editText.isFocused(), editText.isFocusable(), editText.getSelectionStart(), editText.getSelectionEnd());
        if(!editText.isTextSelectable() && editText.hasSelection()) {
            free();
            return true;
        }
        if(defaultTop < 0){
            defaultTop = ((int) editText.getY());
        }
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

        TextOffsetHelper.getPositionLineOffset(location, this.editText, ((int) event.getX()), ((int) event.getY() + editText.getScrollY()));
        Timber.d("offset : %s", location[1]);

        if (editText.hasSelection()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }else if (event.getAction() == MotionEvent.ACTION_UP && diffRaw < 20) {
                if(event.getEventTime() - downTime < 150){
                    TextOffsetHelper.getPositionLineOffset(location, this.editText, posDownX, ((int) (posDownY /*+ initScrollY*/)));
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
                            Timber.d("touch o l");
                        } else if (location[1] > editText.getSelectionEnd()) {
                            this.touchOuter(false, location[1]);
                            Timber.d("touch o r");
                        } else if (location[1] <= centerOfSelection) {
                            this.touchInner(true, location[1]);
                            Timber.d("touch i l");
                        } else if (location[1] > centerOfSelection) {
                            this.touchInner(false, location[1]);
                            Timber.d("touch o r");
                        }
                    }
                    return true;
                }
            }
            return true;
        }else{
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                editText.setFocusable(true);
//            }
//            if (event.getEventTime() - downTime > 150 || diffRaw > 20) {
//                free();
//            }
            if(event.getAction() == MotionEvent.ACTION_MOVE) {
                if(diffRaw< 20) {
                    Timber.d("touch focusable");
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                }else{
                    Timber.d("touch focusable false");
//                    free();
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
            if (Math.abs(editText.getSelectionStart() - offset) > 1) {
                editText.setSelection(offset, fixOffset);
            }
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
