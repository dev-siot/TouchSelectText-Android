package com.jfsiot.touchselect.touchselecttest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.Toolbar.OnToolbarAction;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;
import com.jfsiot.touchselect.touchselecttest.customview.SelectableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SSS on 2015-12-27.
 */
public class DefaultFragment extends Fragment implements OnToolbarAction {
    @Bind(R.id.main_edit) protected SelectableEditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle(R.string.nav_drawer_default);
    }

    @Override
    public void OnToolbarAction(int action) {
        if (action == 2) init();

    }
    public void init(){
        String text = "";
        for(String str : ((MainActivity) getActivity()).getTextSourse(0)){
            text += str;
        }
        this.editText.setText(text);
        editText.setTextIsSelectable(true);
    }
}
