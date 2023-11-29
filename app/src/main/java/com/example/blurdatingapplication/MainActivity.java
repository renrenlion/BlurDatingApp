package com.example.blurdatingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.blurdatingapplication.automaticmatching.AutomaticMatchingFragment;
import com.example.blurdatingapplication.chat.ChatFragment;
import com.example.blurdatingapplication.manualmatching.ManualMatchingFragment;
import com.example.blurdatingapplication.mypage.MyPageFragment;
import com.example.blurdatingapplication.notification.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ManualMatchingFragment manualMatchingFragment;
    AutomaticMatchingFragment automaticMatchingFragment;
    ChatFragment chatFragment;
    MyPageFragment myPageFragment;

    NotificationFragment notificationFragment;

    ImageView imageViewSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        imageViewSetting = findViewById(R.id.setting);

        manualMatchingFragment = new ManualMatchingFragment();
        automaticMatchingFragment = new AutomaticMatchingFragment();
        notificationFragment = new NotificationFragment();
        chatFragment = new ChatFragment();
        myPageFragment = new MyPageFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.frame, manualMatchingFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_manual){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, manualMatchingFragment).commit();
                    imageViewSetting.setVisibility(View.VISIBLE);
                }
                if(item.getItemId() == R.id.nav_automatic){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, automaticMatchingFragment).commit();
                    imageViewSetting.setVisibility(View.VISIBLE);
                }
                if(item.getItemId() == R.id.nav_notification){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, notificationFragment).commit();
                    imageViewSetting.setVisibility(View.GONE);
                }
                if(item.getItemId() == R.id.nav_chat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, chatFragment).commit();
                    imageViewSetting.setVisibility(View.VISIBLE);
                }
                if(item.getItemId() == R.id.nav_mypage){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,myPageFragment).commit();
                    imageViewSetting.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        imageViewSetting.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Setting.class));
        });
    }
}