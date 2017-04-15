package com.foroughi.pedram.storial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.foroughi.pedram.storial.fragment.HomeFragment;
import com.foroughi.pedram.storial.fragment.WritingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initDrawer();


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_writing) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, WritingFragment.newInstance())
                    .commit();
        } else if (id == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_1, HomeFragment.newInstance())
            .commit();

        } else if (id == R.id.nav_popular) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
