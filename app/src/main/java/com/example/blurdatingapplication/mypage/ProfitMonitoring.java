package com.example.blurdatingapplication.mypage;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.data.profitMonitor;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfitMonitoring extends AppCompatActivity {

    private TextView totalProfitTextView;
    UserData userData;

    profitMonitor profitMonitoring;

    public ProfitMonitoring() {
        // Empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_monitoring);

        getUserInfo();  // Fetch user info when the activity is created

        ImageView backButton = findViewById(R.id.back);
        totalProfitTextView = findViewById(R.id.totalProfitTextView);

        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click.
                onBackPressed();
            }
        });
        calculateProfit();
    }

    void getUserInfo() {
        FireBaseUtil.currentUserData().get().addOnCompleteListener(userDataTask -> {
            if (userDataTask.isSuccessful()) {
                userData = userDataTask.getResult().toObject(UserData.class);
                // Fetch profit info after retrieving user data
                fetchProfitInfo();
            }
        });
    }

    void fetchProfitInfo() {
        FireBaseUtil.currentUserPayment().get().addOnCompleteListener(paymentTask -> {
            if (paymentTask.isSuccessful()) {
                profitMonitoring = paymentTask.getResult().toObject(profitMonitor.class);
            }
        });
    }

    // A way to calculate the profit of the app.
        void calculateProfit() {
            FireBaseUtil.allUserCollectionUserData().get().addOnCompleteListener(usersTask -> {
                double currentProfit = 0;
                if (usersTask.isSuccessful()) {
                    for (UserData user : usersTask.getResult().toObjects(UserData.class)) {
                        if (user.getPlan() == 1) {
                            // Increment profit for users with plan equal to 0
                            currentProfit += 14;
                            }
                        }
                    }
                    if (totalProfitTextView != null) {
                        if (currentProfit < 100) {
                            // Display that the application is making no money.
                            totalProfitTextView.setText("The Application is making no money: $" + currentProfit);
                        } else if (currentProfit == 100) {
                            // Display that the application is breaking even.
                            totalProfitTextView.setText("The Application is breaking even: $" + currentProfit);
                        } else {
                            // Send money to the user using bankingInfo.
                            totalProfitTextView.setText("The Application is making money, this is your current profit: $" + currentProfit/4);
                        }
                    }
            });
        }
    }