package com.jfsiot.touchselect.touchselecttest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;
import com.jfsiot.touchselect.touchselecttest.helper.TextOffsetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSS on 2015-12-27.
 */
public class ComplexWordParagraphFragment extends SelectableFragment implements View.OnTouchListener {
    private List<Integer> wordIndexList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.wordIndexList = new ArrayList<>();

        String text = "";
        for(String str : ((MainActivity) getActivity()).getTextSourse(0)){
            text += str;
            wordIndexList.add(text.length());
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle(R.string.nav_drawer_group_complex_word_paragraph);
    }

    @Override
    protected String[] getStringArrayResourse() {
        return ((MainActivity) getActivity()).getTextSourse(2);
    }

    @Override
    protected void touchInner(boolean isLeft, int letterOffset) {
        int offset, fixOffset;
        if(isLeft) {
            offset = TextOffsetHelper.getOffsetTextList(wordIndexList, letterOffset, true, editText.getText().toString());
            fixOffset = editText.getSelectionEnd();
            if (Math.abs(editText.getSelectionStart() - offset) > 1) editText.setSelection(offset, fixOffset);
        }else{
            offset = TextOffsetHelper.getOffsetTextList(wordIndexList, letterOffset, false, editText.getText().toString());
            fixOffset = editText.getSelectionStart();
            if(Math.abs(editText.getSelectionEnd() - offset) > 1)  editText.setSelection(fixOffset, offset);
        }
    }

    @Override
    protected void touchOuter(boolean isLeft, int letterOffset) {
        super.touchOuter(isLeft, letterOffset);
    }
}
