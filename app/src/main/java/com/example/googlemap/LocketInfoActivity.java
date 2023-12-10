package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.googlemap.databinding.ActivityLocketInfoBinding;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.Globals;

public class LocketInfoActivity extends AppCompatActivity {

    ActivityLocketInfoBinding binding;
    Locker locker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_locket_info);
        locker = Globals.selectedLocker;


        if(locker != null)
        {
            binding.tvLocation.setText("Location: \t" + Globals.currentPost.getAddress());
            binding.tvLockerNumber.setText("Locker Number: \t" + locker.getId());
            binding.tvPrice.setText("Price: \t" + locker.getPrice() + " SAR");
        }


        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocketInfoActivity.this, SetPinActivity.class);
                startActivity(intent);

            }
        });

    }
}