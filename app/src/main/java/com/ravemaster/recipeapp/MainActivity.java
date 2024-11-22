package com.ravemaster.recipeapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ravemaster.recipeapp.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViews();

        viewPager2.setAdapter(new ViewPagerAdapter(this));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(bottomListener);

    }

    private final NavigationBarView.OnItemSelectedListener bottomListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.idHome){
                viewPager2.setCurrentItem(0);
                return true;
            } else if (item.getItemId() == R.id.idSearch) {
                viewPager2.setCurrentItem(1);
                return true;
            }  else if (item.getItemId() == R.id.idSettings) {
                viewPager2.setCurrentItem(2);
                return true;
            }
            return false;
        }
    };

    private void initViews(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager2 = findViewById(R.id.myViewPager);
    }
}