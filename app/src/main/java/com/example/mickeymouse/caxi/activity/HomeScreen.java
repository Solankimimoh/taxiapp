package com.example.mickeymouse.caxi.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.fragment.HomeMap;
import com.example.mickeymouse.caxi.helper.SessionManager;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeScreen.class.getSimpleName();

    FloatingActionButton bookridebtn;
    FloatingActionButton shareride;

    HomeMap homeMap;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.homescreen);
        setSupportActionBar(toolbar);
        SessionManager sessionManager = new SessionManager(HomeScreen.this);


        bookridebtn = (FloatingActionButton) findViewById(R.id.bookride);
        shareride = (FloatingActionButton) findViewById(R.id.shareride);

        bookridebtn.setVisibility(View.INVISIBLE);
        shareride.setVisibility(View.INVISIBLE);


        bookridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, BookRide.class);
                startActivity(intent);
            }
        });

        shareride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, ShareRide.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        txtProfileName.setText(sessionManager.userNameSession());


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        homeMap = new HomeMap();

        fragmentTransaction.replace(R.id.home_container, homeMap);

        fragmentTransaction.commit();


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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_bookride) {
            Intent intent = new Intent(HomeScreen.this, BookRide.class);
            startActivity(intent);
        } else if (id == R.id.view_ride) {
            final Intent intent = new Intent(HomeScreen.this, RIdeHistory.class);
            startActivity(intent);
        } else if (id == R.id.nav_help_feedback) {
            Intent intent = new Intent(HomeScreen.this, Help.class);
            startActivity(intent);
        } else if (id == R.id.nav_rateus) {
            Intent intent = new Intent(HomeScreen.this, Rateus.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(HomeScreen.this, Aboutus.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
