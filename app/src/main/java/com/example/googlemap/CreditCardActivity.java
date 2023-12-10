package com.example.googlemap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.googlemap.databinding.ActivityCreditCardBinding;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreditCardActivity extends AppCompatActivity {

    ActivityCreditCardBinding binding;
    Locker locker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_credit_card);
        locker = Globals.selectedLocker;

        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardNumber = binding.editCardNumber.getText().toString().trim();
                String cardHolder = binding.editCardHolder.getText().toString().trim();
                String cardExpiry = binding.editExpiryDate.getText().toString().trim();
                String cardCvv = binding.editCVV.getText().toString().trim();


                if(cardNumber.isEmpty())
                {
                    binding.editCardNumber.setError("Please enter card number");
                    binding.editCardNumber.requestFocus();
                    return;
                }

                if(cardHolder.isEmpty())
                {
                    binding.editCardHolder.setError("Please enter card holder");
                    binding.editCardHolder.requestFocus();
                    return;
                }

                if(cardExpiry.isEmpty())
                {
                    binding.editExpiryDate.setError("Please enter card expiry");
                    binding.editExpiryDate.requestFocus();
                    return;
                }

                if(cardCvv.isEmpty())
                {
                    binding.editCVV.setError("Please enter card CVV");
                    binding.editCVV.requestFocus();
                    return;
                }


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       showAlertDialog();

                    }
                }, 4000);

            }
        });
    }


    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Confirmed!");
        builder.setMessage("Your payment is confirmed. You can now take your belongings out of locker.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseDatabase.getInstance().getReference()
                        .child("post")
                        .child(Globals.currentPost.getKey())
                        .child("booked_lockers")
                        .child("" + locker.getId())
                        .setValue(null);


                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("bookings")
                        .child(Globals.currentPost.getKey())
                        .setValue(Globals.selectedLocker);

                Globals.selectedLocker = null;
                Globals.currentPost = null;

                Intent intent = new Intent(CreditCardActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        builder.show();
    }

    public void onClick(View mock) {
    }
}