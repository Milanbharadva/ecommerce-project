package com.webmintinfotech.ecom.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.databinding.ActPayStackBinding;
import com.webmintinfotech.ecom.utils.CreditCardTextFormatter;
import org.json.JSONException;

import java.util.Calendar;

public class ActPayStack extends AppCompatActivity {

    private Transaction transaction;
    private Charge charge;
    private String amount = "";
    private String publicKey = "";
    private String email = "";
    private ActPayStackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActPayStackBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        amount = getIntent().getStringExtra("amount") != null ? getIntent().getStringExtra("amount") : "";
        publicKey = getIntent().getStringExtra("public_key") != null ? getIntent().getStringExtra("public_key") : "";
        email = getIntent().getStringExtra("email") != null ? getIntent().getStringExtra("email") : "";
        setContentView(view);
        initViews();
    }

    private void initViews() {
        addTextWatcherToEditText();
        binding.btnPay.setText(getString(R.string.pay_amount, " GHS") + amount);
        handleClicks();
    }

    private void addTextWatcherToEditText() {
        // Make button UnClickable for the first time
        binding.btnPay.setEnabled(false);
        binding.btnPay.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_opaque));

        // Make the button clickable after detecting changes in the input field
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = binding.etCardNumber.getText().toString();
                String s2 = binding.etExpiry.getText().toString();
                String s3 = binding.etCvv.getText().toString();

                // Check if they are empty, make button unclickable
                if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                    binding.btnPay.setEnabled(false);
                    binding.btnPay.setBackground(ContextCompat.getDrawable(ActPayStack.this, R.drawable.btn_round_opaque));
                }

                // Check the length of all edit text, if it meets the required length, make button clickable
                if (s1.length() >= 16 && s2.length() == 5 && s3.length() == 3) {
                    binding.btnPay.setEnabled(true);
                    binding.btnPay.setBackground(ContextCompat.getDrawable(ActPayStack.this, R.drawable.btn_border_blue_bg));
                }

                // If edit text doesn't meet the required length, make button unclickable
                if (s1.length() < 16 || s2.length() < 5 || s3.length() < 3) {
                    binding.btnPay.setEnabled(false);
                    binding.btnPay.setBackground(ContextCompat.getDrawable(ActPayStack.this, R.drawable.btn_round_opaque));
                }

                // Add a slash to expiry date after the first two characters (month)
                if (s2.length() == 2) {
                    if (start == 2 && before == 1 && !s2.contains("/")) {
                        binding.etExpiry.setText(getString(R.string.expiry_space, s2.charAt(0)));
                        binding.etExpiry.setSelection(1);
                    } else {
                        binding.etExpiry.setText(getString(R.string.expiry_slash, s2));
                        binding.etExpiry.setSelection(3);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Add text watcher
        binding.etCardNumber.addTextChangedListener(new CreditCardTextFormatter());
        binding.etExpiry.addTextChangedListener(watcher);
        binding.etCvv.addTextChangedListener(watcher);
    }

    private void handleClicks() {
        binding.btnPay.setOnClickListener(v -> {
            if (com.webmintinfotech.ecom.utils.Common.isCheckNetwork(ActPayStack.this)) {
                binding.loadingPayOrder.setVisibility(View.VISIBLE);
                binding.btnPay.setVisibility(View.GONE);
                doPayment();
            } else {
                Toast.makeText(ActPayStack.this, "Please check your internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doPayment() {
        // Set public key
        PaystackSdk.setPublicKey(publicKey);

        // Initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());
        charge.setAmount(Integer.parseInt(amount));
        charge.setCurrency("GHS");
        charge.setEmail(email);
        charge.setReference("payment" + Calendar.getInstance().getTimeInMillis());

        try {
            charge.putCustomField("Charged From", "Android SDK");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        doChargeCard();
    }

    private Card loadCardFromForm() {
        // Validate fields
        String cardNumber = binding.etCardNumber.getText().toString().trim();
        String expiryDate = binding.etExpiry.getText().toString().trim();
        String cvc = binding.etCvv.getText().toString().trim();

        // Formatted values
        String cardNumberWithoutSpace = cardNumber.replace(" ", "");
        String monthValue = expiryDate.substring(0, Math.min(expiryDate.length(), 2));
        String yearValue = expiryDate.substring(Math.max(expiryDate.length() - 2, 0));

        // Build card object with ONLY the number, update the other fields later
        Card card = new Card.Builder(cardNumberWithoutSpace, 0, 0, "").build();

        // Update the cvc field of the card
        card.setCvc(cvc);

        // Validate expiry month
        int month = 0;
        try {
            month = Integer.parseInt(monthValue);
        } catch (Exception ignored) {
        }
        card.setExpiryMonth(month);

        // Validate expiry year
        int year = 0;
        try {
            year = Integer.parseInt(yearValue);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }

    private void doChargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // Hide loading
                binding.loadingPayOrder.setVisibility(View.GONE);
                binding.btnPay.setVisibility(View.VISIBLE);
                ActPayStack.this.transaction = transaction;
                Toast.makeText(ActPayStack.this, "Payment was successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("id", transaction.getReference().toString());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                ActPayStack.this.transaction = transaction;
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // Stop loading
                binding.loadingPayOrder.setVisibility(View.GONE);
                binding.btnPay.setVisibility(View.VISIBLE);

                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying an error
                ActPayStack.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    ActPayStack.this.doChargeCard();
                    return;
                }

                if (transaction.getReference() != null) {
                    Toast.makeText(ActPayStack.this, error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ActPayStack.this, error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.loadingPayOrder.setVisibility(View.GONE);
    }
}