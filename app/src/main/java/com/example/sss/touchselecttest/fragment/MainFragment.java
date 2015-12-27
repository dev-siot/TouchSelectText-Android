package com.example.sss.touchselecttest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sss.touchselecttest.R;
import com.example.sss.touchselecttest.customview.SelectableEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by SSS on 2015-12-27.
 */
public class MainFragment extends Fragment {
    @Bind(R.id.main_edit) protected SelectableEditText editText;
    private List<TextView> dataTextViewList;

    private int savedStart = 0, savedEnd = 0;
    private String[] strList = {"hello", " ", "world", ".", "  ", "good", " ", "moar.ning", "?"};
    private List<Integer> indexList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, view);
        dataTextViewList = new ArrayList<>();
        editText.setText("hello world. good, mor.ning?");
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Timber.d("create");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Timber.d("prepare");
                Timber.d("selection : %s", editText.getSelectionStart());
                int top = editText.getLayout().getLineBottom(editText.getLayout().getLineForOffset(0));
                int bottom = editText.getLayout().getLineTop(editText.getLayout().getLineForOffset(0));
                int x = ((int) editText.getLayout().getPrimaryHorizontal(editText.getSelectionStart()));
                int y = (bottom - top) / 2 + top;
                editText.showCursor(x, y);
                int start = editText.getSelectionStart();
                int end = editText.getSelectionEnd() ;
                editText.move(start, end);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Timber.d("action");
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Timber.d("destroy");
                editText.hide();
            }
        });
        editText.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 0;
                for(int i : indexList){
                    if(i >= editText.getSelectionStart()){
                        break;
                    }else{
                        value = i;
                    }
                }
                Timber.d("left : %s %s", editText.getSelectionStart(), value);
                int start = value;
                int end = editText.getSelectionEnd();
                editText.move(start, end);
                editText.setSelection(start, end);
            }
        });
        editText.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 0;
                for(int i : indexList){
                    if(i > editText.getSelectionEnd()){
                        value = i;
                        break;
                    }
                }
                Timber.d("right : %s %s", editText.getSelectionEnd(), value);
                int start = editText.getSelectionStart();
                int end = value;
                editText.move(start, end);
                editText.setSelection(start, end);
            }
        });
        editText.setOnSelectionChanged(new SelectableEditText.OnSelectionChanged() {
            @Override
            public void onSelectionChanged(int start, int end) {

                editText.move(start, end);
//                Timber.d("index : %s %s %s %s %s", x, y, bottom, top, location[1]);
//                Timber.d("change!! %s %s %s", start, end, line);
//                if (end > savedEnd){
//                    int before = 0;
//                    for(int i : indexList){
//                        if(i > end){
//                            editText.setSelection(start, before);
//                            break;
//                        }else{
//                            before = i;
//                        }
//                    }
//                }
//                savedStart = start;
//                savedEnd = end;
            }
        });
        String text = "";
        for(String str : strList){
            text += str;
            indexList.add(text.length() - 1);
        }
        Timber.d("index : %s", indexList);
        this.editText.setText(text);

        return view;
    }
}
