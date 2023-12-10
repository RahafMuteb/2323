package com.example.googlemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.googlemap.models.Locker;

import java.util.List;

public class LockerAdapter extends BaseAdapter {
    private Context context;
    private int numberOfLockers;
    private List<Locker> lockerList; // Assuming Model is a class representing locker data


    //
//    public LockerAdapter(Context context, int numberOfLockers) {
//        this.context = context;
//        this.numberOfLockers = numberOfLockers;
//    }
    public LockerAdapter(Context context, List<Locker> lockerList) {
        this.context = context;
        this.lockerList = lockerList;
    }


    @Override
    public int getCount() {
        return lockerList.size(); // Return the size of the lockerList

    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lockerView = convertView;

        if (lockerView == null) {
            lockerView = LayoutInflater.from(context).inflate(R.layout.seat_item, parent, false);
        }

        ImageView lockerImage = lockerView.findViewById(R.id.lockerImage);


        // Determine the status of the locker based on the provided list
        boolean lockerStatus = lockerList.get(position).isAvailable();
        if (lockerStatus) {
            lockerImage.setImageResource(R.drawable.bluelocker); // Set image for available locker
        } else {
            lockerImage.setImageResource(R.drawable.graaylocker); // Set image for occupied locker
        }


        return lockerView;
    }

}