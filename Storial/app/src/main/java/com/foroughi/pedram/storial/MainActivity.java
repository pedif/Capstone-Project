package com.foroughi.pedram.storial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.foroughi.pedram.storial.Common.Constants;
import com.foroughi.pedram.storial.fragment.BaseListFragment;
import com.foroughi.pedram.storial.fragment.HomeFragment;
import com.foroughi.pedram.storial.fragment.PopularFragment;
import com.foroughi.pedram.storial.fragment.StoryFragment;
import com.foroughi.pedram.storial.fragment.WritingFragment;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StoryRecyclerAdapter.OnStoryClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    boolean twoPane;
    private final static String TAG_FRAGMENT = "tag_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initDrawer();
        twoPane = findViewById(R.id.container_frag_2) != null;

        if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, HomeFragment.newInstance(this), TAG_FRAGMENT)
                    .commit();
        }else{
            Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            if(f instanceof BaseListFragment)
                ((BaseListFragment) f).setListener(this);
        }

        navigationView.setCheckedItem(R.id.nav_home);


    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        TextView tv_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_tv_name);
        TextView tv_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_tv_email);
        ImageView img = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_profile);

        Uri photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photo != null)
            Picasso.with(getApplicationContext()).load(photo).into(img);

        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (name != null)
            tv_name.setText(name);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null)
            tv_email.setText(email);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_writing) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, WritingFragment.newInstance(this), TAG_FRAGMENT)
                    .commit();
        } else if (id == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, HomeFragment.newInstance(this), TAG_FRAGMENT)
                    .commit();

        } else if (id == R.id.nav_popular) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, PopularFragment.newInstance(this), TAG_FRAGMENT)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStorySelected(String id) {

        if (twoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_frag_2, StoryFragment.newInstance(id))
                    .commit();
        } else {
            StoryRecyclerAdapter.currentSelectedId = null;
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra(Constants.EXTRA_STORY_ID, id);
            startActivity(intent);
        }
    }
}
