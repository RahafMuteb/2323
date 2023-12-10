package com.example.googlemap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.googlemap.databinding.ActivityTimerBinding;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.GlobalTimer;
import com.example.googlemap.utils.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity
{

    ActivityTimerBinding binding;
    Locker locker;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        locker = Globals.selectedLocker;


        if (locker != null)
        {
            binding.tvLocation.setText("Location: \t" + Globals.currentPost.getAddress());
            binding.tvLockerNumber.setText("Locker Number: \t" + locker.getId());
            binding.tvPrice.setText("Price: \t" + locker.getPrice() + " SAR");

            GlobalTimer.callback = new GlobalTimer.TimerCallback()
            {
                @Override
                public void onTimerTick(long remainingMinutes, long remainingSeconds)
                {
                    updateTimerText(String.format(Locale.getDefault(), "%02d:%02d", remainingMinutes, remainingSeconds));
                    // Check if the timer has reached 30 minutes
                    if (remainingMinutes == 00 && remainingSeconds == 0) {
                        showAlertDialog(); // Show the alert dialog
                    }
                }
            };

        } else
        {
            binding.getRoot().setVisibility(View.GONE);
        }


        binding.btnBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Globals.selectedLocker.getBooking().startTimer();

                FirebaseDatabase.getInstance().getReference()
                        .child("post")
                        .child(Globals.currentPost.getKey())
                        .child("booked_lockers")
                        .child("" + locker.getId())
                        .setValue(Globals.selectedLocker);


                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("bookings")
                        .child(Globals.currentPost.getKey())
                        .setValue(Globals.selectedLocker);

                GlobalTimer.stopTimer();

                Intent intent = new Intent(TimerActivity.this, BookingDetailScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                GlobalTimer.stopTimer();

                FirebaseDatabase.getInstance().getReference()
                        .child("post")
                        .child(Globals.currentPost.getKey())
                        .child("booked_lockers")
                        .child("" + locker.getId())
                        .setValue(null);

                Intent intent = new Intent(TimerActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void showAlertDialog() { AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Timer Finished");
        builder.setMessage("Your reservtion has been canceled,timer of 30 min has finished.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseDatabase.getInstance().getReference()
                        .child("post")
                        .child(Globals.currentPost.getKey())
                        .child("booked_lockers")
                        .child("" + locker.getId())
                        .setValue(null);

                Intent intent = new Intent(TimerActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.show();

    }

    private void updateTimerText(String data)
    {
        binding.tvTimer.setText(data);
    }


}