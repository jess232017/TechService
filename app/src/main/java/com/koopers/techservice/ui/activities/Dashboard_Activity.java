package com.koopers.techservice.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koopers.techservice.R;

public class Dashboard_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setUpNavigation();

        /*ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, connection -> {
            assert connection != null;
            if (connection.getIsConnected()) {
                txtNoConectionState.setVisibility(View.GONE);
                ShowNetConnected();
            } else {
                txtConectionState.setVisibility(View.GONE);
                txtNoConectionState.setVisibility(View.VISIBLE);
            }
        });*/
    }

    public void setUpNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }
}
