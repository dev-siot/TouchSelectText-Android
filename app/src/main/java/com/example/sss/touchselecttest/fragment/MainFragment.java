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
                editText.setSelection(5, 10);
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
            }
        });
        String[] strList = {"hello", " ", "world", ".", "  ", "good", " ", "moarning", "?"};

//        for(String str : strList){
//            addNewTextView(str);
//        }
//        linearContainer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    for(TextView tv : dataTextViewList) {
//                        int location[] = new int[2];
//                        float x = event.getX();
//                        float y = event.getY();
//                        tv.getLocationInWindow(location);
//                        if((x > location[0] && x < location[0]+tv.getWidth())
//                                && (y > location[1] && y < location[1]+tv.getHeight())){
//                            tv.setSelected(true);
//                            Timber.d("selected %s %s", x, y);
//                            Timber.d("selected view %s %s %s %s", location[0], location[1], location[0]+tv.getWidth(), location[1]+ tv.getHeight());
//                        }
//                    }
//                    Timber.d("move!!");
//                    return false;
//                }
//                return true;
//            }
//        });

        return view;
    }
    private void addNewTextView(String text){
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_textview));
        textView.setTextIsSelectable(true);
//        textView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    Timber.d("v : %s", ((TextView) v).getText());
//                    v.setSelected(!v.isSelected());
//                    Timber.d("contains : %s", dataTextViewList.contains(v));
//                    return false;
//                }
//                return false;
//            }
//        });
        dataTextViewList.add(textView);
//        linearContainer.addView(textView);
    }
}
