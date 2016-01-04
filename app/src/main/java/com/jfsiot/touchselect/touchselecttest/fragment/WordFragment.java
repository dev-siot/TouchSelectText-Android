package com.jfsiot.touchselect.touchselecttest.fragment;

import android.view.View;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;

/**
 * Created by SSS on 2015-12-27.
 */
public class WordFragment extends SelectableFragment implements View.OnTouchListener {
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle(R.string.nav_drawer_word);
    }

    @Override
    protected String[] getStringArrayResourse() {
        return ((MainActivity) getActivity()).getTextSourse(0);
    }
}
