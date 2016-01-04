package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.Toolbar.OnToolbarAction;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;
import com.jfsiot.touchselect.touchselecttest.customview.SelectableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SSS on 2015-12-27.
 */
public class PasteFragment extends Fragment implements OnToolbarAction{
    @Bind(R.id.paste_edit) protected EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_memo, container, false);
        ButterKnife.bind(this, view);

        editText.setTextIsSelectable(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle(R.string.nav_drawer_paste);
        ((MainActivity) getActivity()).toolbarItemVisibility(R.id.action_complete, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).toolbarItemVisibility(R.id.action_complete, false);
    }

    @Override
    public void OnToolbarAction(int action) {
        if(action == 1){
            editText.setCursorVisible(false);
            editText.clearFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
