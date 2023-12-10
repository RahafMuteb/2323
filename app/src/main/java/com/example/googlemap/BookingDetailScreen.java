package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.googlemap.databinding.ActivityBookingDetailScreenBinding;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.Globals;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingDetailScreen extends AppCompatActivity {

    ActivityBookingDetailScreenBinding binding;
    Locker locker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_detail_screen);

        locker = Globals.selectedLocker;

        if (locker != null) {
            binding.tvLocation.setText("Location: \t" + Globals.currentPost.getAddress());
            binding.tvLockerNumber.setText("Locker Number: \t" + locker.getId());
            binding.tvReservationState.setText("Reservation State: Confirmed");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(locker.getBooking().getStartTime());
            binding.tvStartTime.setText("Start time: " + new SimpleDateFormat("MMM dd h:mm a").format(calendar.getTime()));

        }


        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.selectedLocker.getBooking().calculateTotalPayment();

                Intent intent = new Intent(BookingDetailScreen.this, BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


}