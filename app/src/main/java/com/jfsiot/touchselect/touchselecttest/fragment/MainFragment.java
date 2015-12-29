package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.customview.SelectableEditText;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by SSS on 2015-12-27.
 */
public class MainFragment extends Fragment implements View.OnTouchListener{
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
//        try {
//            Field editorField = TextView.class.getDeclaredField("mEditor");
//            editorField.setAccessible(true);
//            Object editorObject = editorField.get(this);
//
//            Class editorClass = Class.forName("android.widget.Editor");
//            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
//            mInsertionControllerEnabledField.setAccessible(true);
//            mInsertionControllerEnabledField.set(editorObject, false);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });

        editText.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = jumpLeftSide();
                Timber.d("left : %s %s", editText.getSelectionStart(), value);
                int start = value;
                int end = editText.getSelectionEnd();
                editText.setSelection(start, end);
            }
        });
        editText.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = jumpRightSide();
                Timber.d("right : %s %s", editText.getSelectionEnd(), value);
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
                        editText.setSelection(jumpLeftSide(), end);
                    else if (end > savedEnd)
                        editText.setSelection(start, jumpRightSide());
                    savedStart = start;
                    savedEnd = end;
                }
            }
        });
        this.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.clearFocus();
                free();
            }
        });
        String text = "";
        for(String str : strList){
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

    public int jumpLeftSide(){
        int value = 0;
        for(int i : indexList){
            if(i >= editText.getSelectionStart()){
                break;
            }else{
                value = i;
            }
        }
        return value;
    }
    public int jumpRightSide(){
        int value = 0;
        for(int i : indexList){
            if(i > editText.getSelectionEnd()){
                value = i;
                break;
            }
        }
        return value;
    }
    public int getOffsetTextList(int offset, boolean isLeft){
//        int value = 0;
//        if(offset < indexList.get(0))
//            return 0;
//        for(int i : indexList){
//            if(i > offset){
//                value = i;
//                break;
//            }
//            value = i;
//        }
        for(int i = 0; i < indexList.size(); i++){
            if(indexList.get(i) > offset){
                if(indexList.get(i) > editText.getText().length() -1)
                    return editText.getText().length() -1;
                else if(isLeft && i > 0)
                    return indexList.get(i-1);
                else if(i == 0)
                    return 0;
                else return indexList.get(i);
            }
        }
//        if(value > editText.getText().length() - 1)
//            value = editText.getText().length() - 1;
        return 0;
    }

    private void getPositionXY(int location[], int offset){
//        int[] screenLocation = new int[2];
//        editText.getLocationOnScreen(screenLocation);
        int line = editText.getLayout().getLineForOffset(offset);
        int top = editText.getLayout().getLineBottom(line);
        int bottom = editText.getLayout().getLineTop(line);
        int x = ((int) editText.getLayout().getPrimaryHorizontal(offset));
        int y = (bottom - top)/2 + top;
        location[0] = x;
        location[1] = y;
    }

    private void getPositionLineOffset(int location[], int x, int y){
        int line = editText.getLayout().getLineForVertical(y);
        int offset = editText.getLayout().getOffsetForHorizontal(line, x);
        Timber.d("** line off : %s %s", line, offset);
        location[0] = line;
        location[1] = offset;
    }



    private int posDownX, posDownY;
    private int offsetDownStart, offstDownEnd;
    private boolean keepTouch;
    Boolean isStart;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int location[] = new int[2];
        if (editText.hasSelection()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                posDownX = ((int) event.getX());
                posDownY = ((int) event.getY());
                offsetDownStart = editText.getSelectionStart();
                offstDownEnd = editText.getSelectionEnd();
                keepTouch = false;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (posDownX < 0 || posDownY < 0) return true;

                boolean swipeLeft;
                getPositionLineOffset(location, posDownX, posDownY);
                int diff = location[1];
                getPositionLineOffset(location, ((int) event.getX()), ((int) event.getY()));
                diff -= location[1];

                if (diff > 0) swipeLeft = true;
                else if (diff == 0) return true;
                else swipeLeft = false;

                getPositionLineOffset(location, ((int) event.getX()), ((int) event.getY()));
                if (!keepTouch){
                    if (location[1] < offsetDownStart && swipeLeft) isStart = true;
                    else if (offsetDownStart < location[1] && location[1] < offstDownEnd && swipeLeft)
                        isStart = false;
                    else if (offsetDownStart < location[1] && location[1] < offstDownEnd && !swipeLeft)
                        isStart = true;
                    else if (location[1] > offstDownEnd && !swipeLeft) isStart = false;
                    else return true;
                    keepTouch = true;
                }
                Timber.d("orientation %s , %s", swipeLeft ? "left" : "right", isStart == null ? "null" : isStart ? "start" : "end");

                getPositionLineOffset(location, ((int) event.getX()), ((int) event.getY()));
                int offset = getOffsetTextList(location[1], swipeLeft);
                Timber.d("orien %s %s", offset, editText.getText().toString().charAt(offset));

                int fixOffset;
                if(isStart){
                    fixOffset = editText.getSelectionEnd();
                    if(offset == fixOffset) offset --;
                }else{
                    fixOffset = editText.getSelectionStart();
                    if(offset == fixOffset) offset ++;
                }
                editText.setSelection(Math.min(offset, fixOffset), Math.max(offset, fixOffset));

                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                free();
            }
            return true;
        }else{
            if(event.getAction() == MotionEvent.ACTION_UP) {
                getPositionLineOffset(location, ((int) event.getX()), ((int) event.getY()));
                int start = getOffsetTextList(location[1], true), end = getOffsetTextList(location[1], false);
                editText.setSelection(start, end);
                return true;
            }
        }
        return false;
    }

    public void free(){
        posDownX = -1;
        posDownY = -1;
        offsetDownStart = -1;
        offstDownEnd = -1;
    }
}
