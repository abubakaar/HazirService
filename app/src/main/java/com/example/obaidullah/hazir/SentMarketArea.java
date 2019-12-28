package com.example.obaidullah.hazir;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SentMarketArea extends Fragment {

String SndArea;
ImageButton SentNext,SentBack,SentMap;
Bundle args = new Bundle();
    String searched_location_lati, searched_location_longi;
    String position_lat, position_lang;
    PlaceAutocompleteFragment autocompleteFragment;
    private LocationManager locationManager;
    private final  int requestcode=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sent_market_area, container, false);
        // Inflate the layout for this fragment

        SentBack=(ImageButton)view.findViewById(R.id.SentBack);
        SentNext=(ImageButton)view.findViewById(R.id.SentNext);
        SentMap=(ImageButton)view.findViewById(R.id.SentMapIcon);



        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(getActivity(),"Allow gps", Toast.LENGTH_LONG).show();
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationget();
        }
        SentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new SendFragment(),args);
            }
        });
        SentNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Styp1", SndArea);
                args.putString("Styp2",getArguments().getString("Styp"));
                args.putString("Cnotyp3",getArguments().getString("Cnotyp2"));
                args.putString("DLat",searched_location_lati);
                args.putString("DLng",searched_location_longi);
                args.putString("PLat",position_lat);
                args.putString("PLng",position_lang);
                ChangeFragment(new RecieverMobileNameFragment(),args);
            }
        });
        SentMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "Send");
                ChangeFragment(new MapFragment(),args);
            }
        });
        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.Sendmarket);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                searched_location_lati = String.valueOf(place.getLatLng().latitude);
                searched_location_longi = String.valueOf(place.getLatLng().longitude);

                //       mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(searched_location_lati),Double.valueOf(searched_location_longi))).title("Your Location"));
                Toast.makeText(getActivity(), "Lati" + searched_location_lati + "\nLongi" + searched_location_longi, Toast.LENGTH_LONG).show();
                Log.i(TAG, "Place: " + place.getName());
                SndArea= String.valueOf(place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        return view;
    }




    public void locationget() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestcode);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location!=null)
            {
                position_lat= String.valueOf((location.getLatitude()));
                position_lang= String.valueOf(location.getLongitude());
//                Toast.makeText(getActivity(), (int) (location.getLatitude()+location.getLongitude()),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }

}
