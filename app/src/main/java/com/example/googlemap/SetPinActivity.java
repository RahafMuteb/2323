package com.example.googlemap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.googlemap.databinding.ActivitySetPinBinding;
import com.example.googlemap.models.Booking;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.GlobalTimer;
import com.example.googlemap.utils.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SetPinActivity extends AppCompatActivity {

    ActivitySetPinBinding binding;
    String otp = "";
    Locker locker;
    boolean[] values = new boolean[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_pin);
        locker = Globals.selectedLocker;

        binding.btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[13] = true;
                otp = otp + "0";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[0] = true;
                otp = otp + "1";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[1] = true;
                otp = otp + "2";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[2] = true;
                otp = otp + "3";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[4] = true;
                otp = otp + "4";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[5] = true;
                otp = otp + "5";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[6] = true;
                otp = otp + "6";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[8] = true;
                otp = otp + "7";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[9] = true;
                otp = otp + "8";
                binding.tvPin.setText(otp);
            }
        });

        binding.btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[10] = true;
                otp = otp + "9";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[3] = true;
                otp = otp + "A";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[7] = true;
                otp = otp + "B";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[11] = true;
                otp = otp + "C";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[15] = true;
                otp = otp + "D";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[12] = true;
                otp = otp + "*";
                binding.tvPin.setText(otp);
            }
        });

        binding.btnHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values[14] = true;
                otp = otp + "#";
                binding.tvPin.setText(otp);
            }
        });


        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Booking booking = new Booking();
                booking.setBookingId(UUID.randomUUID().toString());
                booking.setOtp(Globals.booleanArrayToString(values));
                booking.setEmail(Globals.user.getEmail());


                locker.setBooking(booking);

                Globals.selectedLocker = locker;

                FirebaseDatabase.getInstance().getReference()
                        .child("post")
                        .child(Globals.currentPost.getKey())
                        .child("booked_lockers")
                        .child("" + locker.getId())
                        .setValue(locker);

                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation!");
        builder.setMessage("Your reservation is confirmed and Locker password is created. Please reach locker in 30 minutes and acquire the locker or else it will be assigned to someone else.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                GlobalTimer.startTimer(locker.getBooking().getStartTime());

                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("bookings")
                        .child(Globals.currentPost.getKey())
                        .setValue(Globals.selectedLocker);

                Intent intent = new Intent(SetPinActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.show();
    }
}