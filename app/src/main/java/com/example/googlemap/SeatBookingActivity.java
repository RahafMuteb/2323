package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.googlemap.models.Locker;
import com.example.googlemap.models.Model;
import com.example.googlemap.utils.Globals;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SeatBookingActivity extends AppCompatActivity {

    Model selectedModel;
    List<Locker> lockerList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);

        // Retrieve the selected Model object from the intent
        Intent intent = getIntent();
        if (intent != null) {
            selectedModel = Globals.currentPost;

            if (selectedModel != null) {

                lockerList = createLockerList(selectedModel.getNumberOfLockers()); // Create a list of Locker objects
                setLockerAvailability();
                GridView gridViewLockers = findViewById(R.id.gridViewLockers);

                // Initialize the LockerAdapter with the dynamic list of lockers
                LockerAdapter lockerAdapter = new LockerAdapter(this, lockerList);
                gridViewLockers.setAdapter(lockerAdapter);

                // Set item click listeners if needed to handle locker selection or booking.
                gridViewLockers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        boolean lockerStatus = lockerList.get(position).isAvailable();
                        if(lockerStatus)
                        {
                            Intent newIntent = new Intent(SeatBookingActivity.this, LocketInfoActivity.class);
                            Globals.currentPost = selectedModel;
                            Globals.selectedLocker = lockerList.get(position);
                            startActivity(newIntent);
                        }
                    }
                });
            }
        }
    }

    private void setLockerAvailability()
    {
        for(int i = 0; i < selectedModel.getBookedLockers().size(); i++)
        {
            Locker bookedLocker = selectedModel.getBookedLockers().get(i);

            Locker locker = lockerList.get(bookedLocker.getId());
            locker.setAvailable(false);
            lockerList.set(bookedLocker.getId(), locker);
        }

    }

    private List<Locker> createLockerList(int numberOfLockers) {
        List<Locker> lockerList = new ArrayList<>();

        // Create Locker objects based on the number of lockers
        for (int i = 0; i < numberOfLockers; i++) {
            Locker locker = new Locker(); // You should populate Locker with necessary data
            locker.setId(i);
            locker.setPrice(selectedModel.getPrice());
            lockerList.add(locker);
        }

        return lockerList;
    }
}
