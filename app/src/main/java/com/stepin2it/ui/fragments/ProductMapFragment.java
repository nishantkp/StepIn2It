package com.stepin2it.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stepin2it.R;
import com.stepin2it.ui.models.WarehouseLocation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductMapFragment extends Fragment {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private String mLatitude;
    private String mLongitude;

    @BindView(R.id.maps_view)
    MapView mapsView;

    private GoogleMap mGoogleMap;

    public ProductMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location latitude and longitude array
     * @return A new instance of fragment ProductMapFragment.
     */
    public static ProductMapFragment newInstance(WarehouseLocation location) {
        ProductMapFragment fragment = new ProductMapFragment();
        Bundle args = new Bundle();
        args.putString(LATITUDE, location.getLatitude());
        args.putString(LONGITUDE, location.getLongitude());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatitude = getArguments().getString(LATITUDE);
            mLongitude = getArguments().getString(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);
        ButterKnife.bind(this, view);
        mapsView.onCreate(savedInstanceState);
        initMap();
        // Inflate the layout for this fragment
        return view;
    }

    private void initMap() {
        //mapsView.onResume();
        MapsInitializer.initialize(getActivity());
        mapsView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (checkLocationPermission()) {
                    //Request location updates:
                    googleMap.setMyLocationEnabled(true);
                }
                mGoogleMap = googleMap;
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                // For dropping a marker at a point on the Map
                LatLng productInfoLatLong = new LatLng(Float.parseFloat(mLatitude),
                        Float.parseFloat(mLongitude));
                googleMap.addMarker(new MarkerOptions().position(productInfoLatLong).
                        title("Product Name").snippet("Product description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition =
                        new CameraPosition.Builder().target(productInfoLatLong).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                        (cameraPosition));
                mapsView.onResume();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Call this method to check for location permission is granted or not
     *
     * @return true or false depending on location permission
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(), new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Provide location permission to show data on map", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapsView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapsView.onDestroy();
    }
}
