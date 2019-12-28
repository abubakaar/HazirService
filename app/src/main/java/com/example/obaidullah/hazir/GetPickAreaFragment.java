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
public class GetPickAreaFragment extends Fragment {

String PArea;
String position_lat, position_lang,searched_location_lati, searched_location_longi;
PlaceAutocompleteFragment autocompleteFragment;
ImageButton PMap,PBack,PNext;
Bundle args = new Bundle();
    private LocationManager locationManager;
    private final  int requestcode=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_get_pick_area, container, false);

        PMap=(ImageButton)view.findViewById(R.id.PickMapIcon);
        PBack=(ImageButton)view.findViewById(R.id.PickBack);
        PNext=(ImageButton)view.findViewById(R.id.PickNext);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(getActivity(),"Allow gps", Toast.LENGTH_LONG).show();
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationget();
        }

        PBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new GetFragment(),args);
            }
        });
        PNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                args.putString("Ptyp1", PArea);
                args.putString("Ptyp2",getArguments().getString("Ptyp"));
                args.putString("GCnotyp3",getArguments().getString("GCnotyp2"));
                args.putString("GPLat",searched_location_lati);
                args.putString("GPLng",searched_location_longi);
                args.putString("GDLat",position_lat);
                args.putString("GDLng",position_lang);
                ChangeFragment(new SenderNameMobileFragment(),args);
            }
        });
        PMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "Get");
                ChangeFragment(new MapFragment(),args);

            }
        });

        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.GetArea);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                searched_location_lati = String.valueOf(place.getLatLng().latitude);
                searched_location_longi = String.valueOf(place.getLatLng().longitude);

                //       mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(searched_location_lati),Double.valueOf(searched_location_longi))).title("Your Location"));
                Toast.makeText(getActivity(), "Lati" + searched_location_lati + "\nLongi" + searched_location_longi, Toast.LENGTH_LONG).show();
                Log.i(TAG, "Place: " + place.getName());
                PArea= String.valueOf(place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        // Inflate the layout for this fragment
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
