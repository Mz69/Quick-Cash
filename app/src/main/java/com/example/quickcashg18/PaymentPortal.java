package com.example.quickcashg18;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

//code from Paypal tutorials in CSCI3130

public class PaymentPortal extends AppCompatActivity {
    ActivityResultLauncher activityResultLauncher;

    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";

    private static PayPalConfiguration config;
    Button  btnPayNow;
    TextView edtPay;
    EditText edtTip;
    TextView paymentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_portal);
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Config.PAYPAL_CLIENT_ID);
        edtPay= findViewById(R.id.edtPay);
        Intent intent= getIntent();
        String payAmount=intent.getStringExtra("pay_key");
        edtPay.setText(payAmount);
        edtTip=findViewById(R.id.edtTip);
        btnPayNow = findViewById(R.id.btnPayNow);
        paymentTV = findViewById(R.id.idTVStatus);

        // initiallizing Activity Launcher and database
        initializeActivityLauncher();
        btnPayNow.setOnClickListener(v -> {
            String tip = edtTip.getText().toString();
            if ((Validation.isNumeric(tip) && Double.parseDouble(tip) < 0) ||
                !Validation.isValidDoubleField(tip)) {
                Toast.makeText(PaymentPortal.this,
                        "Please enter a valid tip amount", Toast.LENGTH_LONG)
                        .show();
            } else {
                processPayment();
           }
        });
    }
    private void initializeActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirmation != null) {
                        try {
                            String paymentDetails = confirmation.toJSONObject().toString(4);
                            JSONObject payObj = new JSONObject(paymentDetails);
                            String payID = payObj.getJSONObject("response").getString("id");
                            String state = payObj.getJSONObject("response").getString("state");
                            paymentTV.setText("Payment " + state + "\n with payment id is " + payID);
                        } catch (JSONException e) {
                            Log.e("Error", "an extremely unlikely failure occurred:", e);
                        }
                    }
                } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    Log.d(TAG, "Launcher Result invalid");
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Log.d(TAG, "Launcher Result Cancelled");
                }
            }
        });
    }
    public void processPayment() {

        double toBePaid = Double.parseDouble(edtPay.getText().toString());
        String tip = edtTip.getText().toString();
        if (!tip.isEmpty()) {
            toBePaid += Double.parseDouble(tip);
        }
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(toBePaid), "CAD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        activityResultLauncher.launch(intent);
        finish();
    }
}



