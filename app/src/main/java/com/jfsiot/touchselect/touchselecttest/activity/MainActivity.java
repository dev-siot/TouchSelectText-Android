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
import com.jfsiot.touchselect.touchselecttest.fragment.DefaultFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.ParagraphFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.PasteFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.SentenceFragment;
import com.jfsiot.touchselect.touchselecttest.fragment.WordFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;

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

    public Toolbar getToolbar(){
        return toolbar;
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
        Fragment fragment;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete) {
            fragment = getFragmentManager().findFragmentById(R.id.activity_container);
            if(fragment instanceof OnToolbarAction)
                ((OnToolbarAction) fragment).OnToolbarAction(1);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        } else if (id == R.id.nav_complex_word_paragraph) {

        } else if (id == R.id.nav_complex_sentence_paragraph) {

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
