package com.example.googlemap.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlemap.MarkerInfoAdapter;
import com.example.googlemap.models.Model;
import com.example.googlemap.R;
import com.example.googlemap.databinding.FragmentSearchBinding;
import com.example.googlemap.models.Locker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {

    FragmentSearchBinding binding;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker;
    private MarkerOptions markerOptions;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Marker nearestMarker;
    private double nearestMarkerDistance = Double.MAX_VALUE;
    private RecyclerView recyclerViewMarkerInfo;
    private MarkerInfoAdapter markerInfoAdapter;
    private final List<Model> markerInfoList = new ArrayList<>();
    private Model currentModel;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize 'currentModel' instance as needed
        currentModel = new Model();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentSearchBinding.inflate(inflater, container, false);
        recyclerViewMarkerInfo = binding.getRoot().findViewById(R.id.recyclerViewMarkerInfo);
        recyclerViewMarkerInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        markerInfoAdapter = new MarkerInfoAdapter(getContext(), markerInfoList);
        recyclerViewMarkerInfo.setAdapter(markerInfoAdapter);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference= FirebaseDatabase.getInstance().getReference("post");

        mapInitialize();
        return binding.getRoot();
    }

    private void mapInitialize() {
        binding.searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH
                        ||i == EditorInfo.IME_ACTION_SEARCH
                        ||keyEvent.getAction()==keyEvent.ACTION_DOWN
                        ||keyEvent.getAction()==keyEvent.KEYCODE_ENTER
                ){
                    goToSearchLocation();
                }
                return false;
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    private void goToSearchLocation() {
        String searchLocation= binding.searchEdt.getText().toString();

        Geocoder geocoder= new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list= geocoder.getFromLocationName(searchLocation,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size()>0){
            Address address = list.get(0);
            String location= address.getSubAdminArea();
            double latitude =address.getLatitude();
            double longitude= address.getLongitude();
            gotoLatLng(latitude,longitude, 17f);


        }
    }

    private void gotoLatLng(double latitude, double longitude, float v) {
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,17f);
        mMap.animateCamera(update);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               // List<Model> models = new ArrayList<>();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model model = dataSnapshot.getValue(Model.class);
                                    if(model != null)
                                        model.setKey(dataSnapshot.getKey());

                                    List<Locker> lockerList = new ArrayList<>();

                                    for(DataSnapshot bookedLockers: dataSnapshot.child("booked_lockers").getChildren())
                                    {
                                        Locker locker = bookedLockers.getValue(Locker.class);
                                        lockerList.add(locker);
                                    }

                                    model.setBookedLockers(lockerList);
                                    //models.add(model);

                                    LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());

                                    markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                            );

                                    marker = mMap.addMarker(markerOptions);
                                    marker.setTag(model);

                                    //where you handle marker click events
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker clickedMarker) {
                                            Model model = (Model) clickedMarker.getTag(); // Retrieve the custom object (Model)

                                            if (model != null) {
                                                // Clear the existing marker info list
                                                markerInfoList.clear();
                                                // Add the clicked Model object to the list
                                                markerInfoList.add(model);

                                                // Notify the adapter that the data has changed
                                                markerInfoAdapter.notifyDataSetChanged();

                                                // Animate the RecyclerView into view
                                                recyclerViewMarkerInfo.setVisibility(View.VISIBLE);
                                                recyclerViewMarkerInfo.animate().translationY(0).setDuration(300);
                                            }
                                            // Check if the clicked marker is the nearest marker
                                            if (clickedMarker.equals(nearestMarker)) {
                                                // Display the info window for the nearest marker
                                                clickedMarker.showInfoWindow();
                                            }

                                            return true;// Consume the click event to prevent the default behavio

                                        }
                                    });
                                }

                             }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error
                                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                        mMap.setMyLocationEnabled(true);
                        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Call the updateNearestMarker method from the Model class
                                currentModel.calculateNearestLocation(location, mMap, databaseReference);

//                                // Reset nearest marker information
//                                nearestMarker = null;
//                                nearestMarkerDistance = Double.MAX_VALUE;
//
//                                // Iterate through the markers in the database
//                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                            Model model = dataSnapshot.getValue(Model.class);
//                                            LatLng markerLatLng = new LatLng(model.getLatitude(), model.getLongitude());
//
//                                            // Calculate the distance between your location and the marker
//                                            Location markerLocation = new Location("Marker Location");
//                                            markerLocation.setLatitude(model.getLatitude());
//                                            markerLocation.setLongitude(model.getLongitude());
//                                            float distance = location.distanceTo(markerLocation);
//
//                                            // Check if this marker is closer than the current nearest marker
//                                            if (distance < nearestMarkerDistance) {
//                                                nearestMarkerDistance = distance;
//
//                                                // Remove the previous nearest marker if it exists
//                                                if (nearestMarker != null) {
//                                                    nearestMarker.remove();
//                                                }
//
//                                                // Add the new nearest marker
//                                                nearestMarker = mMap.addMarker(new MarkerOptions()
//                                                        .position(markerLatLng)
//                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//                                                        .title(model.getAddress())
//                                                        .snippet("This is the closest locker to your location"));
//
//
//                                                // Show the info window for the nearest marker
//                                                nearestMarker.showInfoWindow();
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                        // Handle database error
//                                        Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                                nearestMarker = mMap.addMarker(new MarkerOptions()
//                                        .title("Nearest Available Lockers"));

                                // Center the map on Grand mosque location
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.4259317, 39.8271968), 14));
                                /*------------------------------------------------------------------------------------*/

                            }
                        });
                    }
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse){
                        Toast.makeText(getContext(),"Permission"+
                                permissionDeniedResponse.getPermissionName()+""+"was denied!", Toast.LENGTH_SHORT).show();

                    }
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest , PermissionToken permissionToken){
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

        //mMap.setOnInfoWindowClickListener(this);
        mMap.setContentDescription(String.valueOf(this));
    }


}