package com.jfsiot.touchselect.touchselecttest.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jfsiot.touchselect.touchselecttest.R;
import com.jfsiot.touchselect.touchselecttest.Toolbar.OnToolbarAction;
import com.jfsiot.touchselect.touchselecttest.fragment.ComplexSentenceParagraphFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.ComplexWordParagraphFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.ComplexWordSentenceFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.DefaultFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.ParagraphFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.PasteFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.SentenceFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.WordFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private int textKindOf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new DefaultFragment();
        this.getFragmentManager().beginTransaction()
                .add(R.id.activity_container, fragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public MenuItem toolbarItemVisibility(int itemRes, boolean visibility){
        return this.toolbar.getMenu().findItem(itemRes).setVisible(visibility);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.activity_container);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete) {
            if(fragment instanceof OnToolbarAction)
                ((OnToolbarAction) fragment).OnToolbarAction(1);
            return true;
        }else if (id == R.id.action_text_swap){
            this.textKindOf ^= 1;
            toolbar.getMenu().findItem(R.id.action_text_swap).setTitle(String.format(getString(R.string.action_swap), textKindOf^1));
            getFragmentManager().beginTransaction().replace(R.id.activity_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getTextSourse(int range){
        switch (range){
            case 0 : return getResources().getStringArray(textKindOf > 0 ? R.array.word_text2 : R.array.word_text);
            case 1 : return getResources().getStringArray(textKindOf > 0 ? R.array.sentence_text2 : R.array.sentence_text);
            case 2 : return getResources().getStringArray(textKindOf > 0 ? R.array.paragraph_text2 : R.array.paragraph_text);
            default: return new String[0];
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new DefaultFragment();
        if (id == R.id.nav_default) {
            // Handle the camera action
            fragment = new DefaultFragment();
        } else if (id == R.id.nav_word) {
            fragment = new WordFragment();
        } else if (id == R.id.nav_sentence) {
            fragment = new SentenceFragment();
        } else if (id == R.id.nav_paragraph) {
            fragment = new ParagraphFragment();
        } else if (id == R.id.nav_complex_word_sentence) {
            fragment = new ComplexWordSentenceFragment();
        } else if (id == R.id.nav_complex_word_paragraph) {
            fragment = new ComplexWordParagraphFragment();
        } else if (id == R.id.nav_complex_sentence_paragraph) {
            fragment = new ComplexSentenceParagraphFragment();
        } else if (id == R.id.nav_paste) {
            fragment = new PasteFragment();
        }
        this.getFragmentManager().beginTransaction()
                .replace(R.id.activity_container, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
