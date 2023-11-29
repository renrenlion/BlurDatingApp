package com.example.blurdatingapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PremiumActivity extends AppCompatActivity {

    EditText bankingInfoEditText, billingInfoEditText, creditCardInfoEditText, expirationDateEditText, csvEditText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        bankingInfoEditText = findViewById(R.id.editTextBankingInfo);
        billingInfoEditText = findViewById(R.id.editTextBillingInfo);
        creditCardInfoEditText = findViewById(R.id.editTextCreditCardInfo);
        expirationDateEditText = findViewById(R.id.editTextExpirationDate);
        csvEditText = findViewById(R.id.editTextCsv);
        saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePremiumData();
            }
        });

        Button cancelButton = findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the activity
            }
        });
    }

    private void savePremiumData() {
        String bankingInfo = bankingInfoEditText.getText().toString().trim();
        String billingInfo = billingInfoEditText.getText().toString().trim();
        String creditCardInfo = creditCardInfoEditText.getText().toString().trim();
        String expirationDate = expirationDateEditText.getText().toString().trim();
        String csv = csvEditText.getText().toString().trim();

        // Check if all fields are filled out
        if (!TextUtils.isEmpty(bankingInfo) && !TextUtils.isEmpty(billingInfo) &&
                !TextUtils.isEmpty(creditCardInfo) && !TextUtils.isEmpty(expirationDate) && !TextUtils.isEmpty(csv)) {
            // Update the user's document in Firestore with premium data
            updatePremiumData(bankingInfo, billingInfo, creditCardInfo, expirationDate, csv);
        } else {
            // Inform the user that all fields must be filled out
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return; // Add this line to prevent further execution if fields are not filled out
        }
    }

    private void updatePremiumData(String bankingInfo, String billingInfo, String creditCardInfo,
                                   String expirationDate, String csv) {
        String userId = FireBaseUtil.currentUserId();

        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId);

        // Create a map with the premium data
        Map<String, Object> premiumData = new HashMap<>();
        premiumData.put("bankingInfo", bankingInfo);
        premiumData.put("billingInfo", billingInfo);
        premiumData.put("creditcardInfo", creditCardInfo);
        premiumData.put("expirationDate", expirationDate);
        premiumData.put("csv", csv);

        // Update the user's document with the premium data
        userRef.update(premiumData)
                .addOnSuccessListener(aVoid -> {
                    // Update the plan field to 1
                    userRef.update("plan", 1)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Premium data saved successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after successful update
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to update the plan field
                                Toast.makeText(this, "Failed to update plan field", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to update premium data
                    Toast.makeText(this, "Failed to save premium data", Toast.LENGTH_SHORT).show();
                });
    }
}
