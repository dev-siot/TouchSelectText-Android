package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jfsiot.touchselect.touchselecttest.R;
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
public class WordFragment extends Fragment implements View.OnTouchListener {
    @Bind(R.id.main_edit) protected SelectableEditText editText;
    @Bind(R.id.select_button) protected TextView selectButton;
    @Bind(R.id.main_container) protected RelativeLayout container;
    private List<TextView> dataTextViewList;

    private int savedStart = -1, savedEnd = -1;
    private String[] strList = {"hello world.", "  ", "good mor.ning?", "world .", "  ", "gooooooood", " ", "morning ", " afternoon ", " evening? ", "world", ".", "  ", "gooddd mor.ning?"};
    private List<Integer> indexList = new ArrayList<>();

    private int currentMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, view);

        dataTextViewList = new ArrayList<>();
        editText.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = TextOffsetHelper.jumpLeftSide(indexList, editText.getSelectionStart());
                int start = value;
                int end = editText.getSelectionEnd();
                editText.setSelection(start, end);
            }
        });
        editText.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = TextOffsetHelper.jumpLeftSide(indexList, editText.getSelectionEnd());
                int start = editText.getSelectionStart();
                int end = value;
                editText.setSelection(start, end);
            }
        });
        editText.setOnSelectionChanged(new SelectableEditText.OnSelectionChanged() {
            @Override
            public void onSelectionChanged(int start, int end) {
                if (currentMode == 1) {
                    if (savedStart < 0 && savedEnd < 0) {

                    } else if (start < savedStart)
                        editText.setSelection(TextOffsetHelper.jumpLeftSide(indexList, editText.getSelectionStart()), end);
                    else if (end > savedEnd)
                        editText.setSelection(start, TextOffsetHelper.jumpLeftSide(indexList, editText.getSelectionEnd()));
                    savedStart = start;
                    savedEnd = end;
                }
            }
        });
        String text = "";
        for(String str : getResources().getStringArray(R.array.word_text)){
            text += str;
            indexList.add(text.length());
        }
        Timber.d("index : %s", indexList);
        this.editText.setText(text);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.editText.setOnTouchListener(this);
        free();
        currentMode = 0;
        selectButton.setText("normal handle");
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMode > 2) currentMode = 0;
                else currentMode++;

                switch (currentMode) {
                    case 0:
                        selectButton.setText("normal handle");
                        break;
                    case 1:
                        selectButton.setText("jump handle");
                        break;
                }

            }
        });
    }

    public void free(){
        posDownX = -1;
        posDownY = -1;
        offsetDownStart = -1;
        offstDownEnd = -1;
        this.editText.setTextIsSelectable(false);
        this.editText.clearFocus();
    }


    private int posDownX, posDownY;
    private int offsetDownStart, offstDownEnd;
    private boolean keepTouch;
    private boolean moving;
    private long downTime, tabTime = 0;
    Boolean isStart;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int location[] = new int[2];
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.downTime = event.getEventTime();
            this.posDownX = ((int) event.getX());
            this.posDownY = ((int) event.getY());
        }
        if(event.getEventTime() - downTime > 500)
            free();
        float diff = posDownX - event.getX() + posDownY - event.getY();
        if (editText.hasSelection()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                offsetDownStart = editText.getSelectionStart();
                offstDownEnd = editText.getSelectionEnd();
                keepTouch = false;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP && diff < 20) {
                if(event.getEventTime() - downTime < 150){
                    TextOffsetHelper.getPositionLineOffset(location, this.editText, ((int) event.getX()), ((int) event.getY()));
                    int offset, fixOffset;
                    if(tabTime != 0 && event.getEventTime() - tabTime < 200){
                        tabTime = 0;
                        this.free();
                    }else {
                        tabTime = event.getEventTime();

                        /**
                         * Change selection range
                         */
                        float centerOfSelection =  editText.getSelectionStart() + (editText.getSelectionEnd() - editText.getSelectionStart()) / 2;
                        Timber.d("center : %s %s %s", editText.getSelectionStart(), centerOfSelection, editText.getSelectionEnd());
                        if (location[1] <= centerOfSelection) {
                            offset = TextOffsetHelper.getOffsetTextList(indexList, location[1], true, editText.getText().length() - 1);
                            fixOffset = editText.getSelectionEnd();
                            editText.setSelection(offset, fixOffset);
                        } else if (location[1] > centerOfSelection) {
                            offset = TextOffsetHelper.getOffsetTextList(indexList, location[1], false, editText.getText().length() - 1);
                            fixOffset = editText.getSelectionStart();
                            editText.setSelection(fixOffset, offset);
                        }
                    }
                }
            }
            return true;
        }else{
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                moving = false;
                editText.setTextIsSelectable(true);
            }
            if (event.getEventTime() - downTime > 150) {
                editText.setTextIsSelectable(false);
                free();
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE) {
                moving = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP) {

                if(Math.abs(diff) < 20) {
                    TextOffsetHelper.getPositionLineOffset(location, this.editText, ((int) event.getX()), ((int) event.getY()));
                    int start = TextOffsetHelper.getOffsetTextList(indexList, location[1], true, editText.getText().length() - 1)
                            , end = TextOffsetHelper.getOffsetTextList(indexList, location[1], false, editText.getText().length() - 1);
                    editText.setSelection(start, end);
                    return true;
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
