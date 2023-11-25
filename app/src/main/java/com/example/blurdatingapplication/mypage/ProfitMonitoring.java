package com.example.blurdatingapplication.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.example.blurdatingapplication.data.Preference;
import com.example.blurdatingapplication.data.Profile;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.data.profitMonitor;
import com.example.blurdatingapplication.utils.FireBaseUtil;

public class ProfitMonitoring extends AppCompatActivity {

    UserData userData;

    profitMonitor profitMonitoring;

    public ProfitMonitoring() {
        // Empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_monitoring);
    }

    void getUserInfo() {

        FireBaseUtil.currentUserData().get().addOnCompleteListener(task -> {
            userData = task.getResult().toObject(UserData.class);
        });

        FireBaseUtil.currentUserPayment().get().addOnCompleteListener(task -> {
            profitMonitoring = task.getResult().toObject(profitMonitor.class);
        });
    }

    void calculateProfit() {
        double currentProfit = 0;
        int bankingInfo = Integer.parseInt(userData.getBankingInfo());

        // a way to calculate the profit of the app.
        int profitPerWeek = Integer.parseInt(profitMonitoring.getPaymentWeek());

        currentProfit += profitPerWeek;

        if (currentProfit < 100) {
            // Display that the application is making no money. Use Notification to output result.
        } else if (currentProfit == 100) {
            // Display that the application is breaking even. Use Notification to output result.
        } else {
            // Send money to user using bankingInfo. Double check with francisco about signing up for a direct
            // deposit.
        }
    }

}