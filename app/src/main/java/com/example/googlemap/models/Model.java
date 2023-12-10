package com.example.googlemap.models;

import android.location.Location;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;


public class Model implements Serializable {

    String id;
    String Name,Address,Size;
    Double latitude;
    Double longitude;
    private String Key;
    private int price;

    private List<Locker> bookedLockers;

    // Add fields for locker details
    private int numberOfLockers;
    private String lockerSizes;
    private String availabilityStatus;
    // Add fields for nearest marker
    private Marker nearestMarker;
    private double nearestMarkerDistance = Double.MAX_VALUE;


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Locker> getBookedLockers() {
        return bookedLockers;
    }

    public void setBookedLockers(List<Locker> bookedLockers) {
        this.bookedLockers = bookedLockers;
    }

    public Model() {
    }

    public Model(String address, String Name, String size, Double latitude, Double longitude) {
        this.Name= Name;
        Address = address;
        Size = size;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public int getNumberOfLockers() {
        return numberOfLockers;
    }

    public String getLockerSizes() {
        return lockerSizes;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public Marker getNearestMarker() {
        return nearestMarker;
    }

    public void setNearestMarker(Marker nearestMarker) {
        this.nearestMarker = nearestMarker;
    }

    public double getNearestMarkerDistance() {
        return nearestMarkerDistance;
    }

    public void setNearestMarkerDistance(double nearestMarkerDistance) {
        this.nearestMarkerDistance = nearestMarkerDistance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // Parcelable implementation
    protected Model(Parcel in) {
        // Read data from the parcel in the same order you wrote it
        Name = in.readString();
        Address = in.readString();
        Size = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public void setNumberOfLockers(int numberOfLockers) {
        this.numberOfLockers = numberOfLockers;
    }

    public void setLockerSizes(String lockerSizes) {
        this.lockerSizes =lockerSizes;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public void calculateNearestLocation(Location location, GoogleMap mMap, DatabaseReference databaseReference) {
        // Reset nearest marker information
        nearestMarker = null;
        nearestMarkerDistance = Double.MAX_VALUE;

        // Iterate through the markers in the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    LatLng markerLatLng = new LatLng(model.getLatitude(), model.getLongitude());

                    // Calculate the distance between your location and the marker
                    Location markerLocation = new Location("Marker Location");
                    markerLocation.setLatitude(model.getLatitude());
                    markerLocation.setLongitude(model.getLongitude());
                    float distance = location.distanceTo(markerLocation);

                    // Check if this marker is closer than the current nearest marker
                    if (distance < nearestMarkerDistance) {
                        nearestMarkerDistance = distance;

                        // Remove the previous nearest marker if it exists
                        if (nearestMarker != null) {
                            nearestMarker.remove();
                        }
                        // Add the new nearest marker
                        nearestMarker = mMap.addMarker(new MarkerOptions()
                                .position(markerLatLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                .title(model.getAddress())
                                .snippet("This is the closest locker to your location"));

                        // Show the info window for the nearest marker
                        nearestMarker.showInfoWindow();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                // You may want to add a callback or handle this in a more robust way
                Log.e("Model", "Database error: " + error.getMessage());
            }
        });

    }


}

//    // New method to find the nearest marker
//    public static Marker findNearestMarker(Location userLocation, List<Model> models, GoogleMap mMap) {
//        Marker nearestMarker = null;
//        double nearestMarkerDistance = Double.MAX_VALUE;
//
//        for (Model model : models) {
//            LatLng markerLatLng = new LatLng(model.getLatitude(), model.getLongitude());
//
//            // Calculate the distance between the user's location and the marker
//            Location markerLocation = new Location("Marker Location");
//            markerLocation.setLatitude(model.getLatitude());
//            markerLocation.setLongitude(model.getLongitude());
//            float distance = userLocation.distanceTo(markerLocation);
//
//            // Check if this marker is closer than the current nearest marker
//            if (distance < nearestMarkerDistance) {
//                nearestMarkerDistance = distance;
//
//                // Remove the previous nearest marker if it exists
//                if (nearestMarker != null) {
//                    nearestMarker.remove();
//                }
//
//                // Add the new nearest marker
//                nearestMarker = mMap.addMarker(new MarkerOptions()
//                        .position(markerLatLng)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//                        .title(model.getAddress())
//                        .snippet("This is the closest locker to your location"));
//                // Show the info window for the nearest marker
//                nearestMarker.showInfoWindow();
//            }
//        }
//
//        return nearestMarker;
//    }
