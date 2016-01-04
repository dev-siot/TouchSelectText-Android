package com.jfsiot.touchselect.touchselecttest.fragment;

import android.view.View;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.activity.MainActivity;

/**
 * Created by SSS on 2015-12-27.
 */
public class ParagraphFragment extends SelectableFragment implements View.OnTouchListener {
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle(R.string.nav_drawer_paragraph);
    }

    @Override
    protected String[] getStringArrayResourse() {
        return getResources().getStringArray(R.array.paragraph_text);
    }
}
