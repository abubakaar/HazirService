package com.example.obaidullah.hazir;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    private MapView mapView;
//    private GoogleMap googleMap;
    GoogleMap mMap;
    LocationListener locationListener;
    LocationManager locationManager;
    SearchView SearchArea;
    String SA;
    Button SCfm,BCfm,GCfm,Shopplacemap,Sendplacemap;
    Bundle args = new Bundle();
    String position_lat,position_lang,searched_location_lati,searched_location_longi;
    PlaceAutocompleteFragment autocompleteFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
//        SearchArea=(SearchView) view.findViewById(R.id.SearchArea);
        SCfm=(Button)view.findViewById(R.id.ShopConfirm);
        BCfm=(Button)view.findViewById(R.id.BuyConfirm);
        GCfm=(Button)view.findViewById(R.id.GetConfirm);
        Shopplacemap=(Button)view.findViewById(R.id.ShopPlaceOrderConfirm);
        Sendplacemap=(Button)view.findViewById(R.id.SendPlaceOrderConfirm);

        if(getArguments().getString("type")=="Shopping"){
            BCfm.setVisibility(view.INVISIBLE);
            GCfm.setVisibility(view.INVISIBLE);
            Shopplacemap.setVisibility(view.INVISIBLE);
            Sendplacemap.setVisibility(view.INVISIBLE);
        }
        else
        if(getArguments().getString("type")=="Get"){
            BCfm.setVisibility(View.INVISIBLE);
            SCfm.setVisibility(View.INVISIBLE);
            Shopplacemap.setVisibility(View.INVISIBLE);
            Sendplacemap.setVisibility(View.INVISIBLE);
        }
        else
        if(getArguments().getString("type")=="Send"){
            SCfm.setVisibility(View.INVISIBLE);
            GCfm.setVisibility(View.INVISIBLE);
            Shopplacemap.setVisibility(View.INVISIBLE);
            Sendplacemap.setVisibility(View.INVISIBLE);
        }
        else
        if(getArguments().getString("type")=="ShopPlaceOrderMap")
        {
            SCfm.setVisibility(View.INVISIBLE);
            GCfm.setVisibility(View.INVISIBLE);
            BCfm.setVisibility(View.INVISIBLE);
            Sendplacemap.setVisibility(View.INVISIBLE);
        }
        else
        if(getArguments().getString("type")=="SendPlaceOrderMap"){
            SCfm.setVisibility(View.INVISIBLE);
            GCfm.setVisibility(View.INVISIBLE);
            SCfm.setVisibility(View.INVISIBLE);
            Shopplacemap.setVisibility(View.INVISIBLE);
        }
        SCfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new ShopPlaceOrderFragment(),args);
            }
        });
        BCfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SA=SearchArea.getQuery().toString();
                args.putString("type", "sndcnfmmap");
                ChangeFragment(new SendPlaceOrderFragment(),args);
            }
        });
        GCfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "GetCnfrmMap");
                ChangeFragment(new SendPlaceOrderFragment(),args);
            }
        });
        Shopplacemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new ShopRecieverNameMobileFragment(),args);
            }
        });
        Sendplacemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new WhoSenderParcelFragment(),args);
            }
        });
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.Map);
        if (supportMapFragment == null) {
            FragmentManager fn = getFragmentManager();
            FragmentTransaction ft = fn.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.Map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);


//////////////////////////////////////////////////////////////////////////
        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                searched_location_lati= String.valueOf(place.getLatLng().latitude);
                searched_location_longi= String.valueOf(place.getLatLng().longitude);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(searched_location_lati),Double.valueOf(searched_location_longi))).title("Your Location"));
                Toast.makeText(getActivity(),"Lati"+searched_location_lati+  "\nLongi"+searched_location_longi,Toast.LENGTH_LONG).show();
                Log.i(TAG, "Place: " + place.getName());
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

////////////////////////////////////////////////



        return view;
    }




    public void updateMap(Location location) {
        LatLng userlocation = new LatLng(location.getLatitude(), location.getLongitude());
        position_lat= String.valueOf(location.getLatitude());
        position_lang= String.valueOf(location.getLongitude());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,17));
    //    mMap.addMarker(new MarkerOptions().position(userlocation).title("Your Location"));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.untitled)));




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateMap(lastLocation);
                }
            }
        }
    }

//    @Override
        @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                updateMap(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, locationListener);
                Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(lastLocation!=null)
                {
                    updateMap(lastLocation);
                }
                if (lastLocation==null)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, locationListener);
                    Location lastLocation2=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(lastLocation!=null)
                    {
                        updateMap(lastLocation2);
                    }
                }
            }
        }
//
//        LatLng sydney = new LatLng(33.641242, 73.077360);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        //     googleMap.setMaxZoomPreference(17);
        // googleMap(sydney);

        if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            googleMap.setMyLocationEnabled(true);

//            map.setPadding(left, top, right, bottom);
            //   googleMap.setPadding(100,1400,5,0);

        }
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//        }
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            getActivity(), R.raw.mapstyle));
//
//            if (!success) {
//                Log.e("MapsActivityRaw", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("MapsActivityRaw", "Can't find style.", e);
//        }
//
//
//        LatLng sydney = new LatLng(33.641242, 73.077360);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        googleMap.setMaxZoomPreference(17);
//
//
//
//
//    }
    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }


    public void callservice(String address)
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                //Toast.makeText(getActivity(),"Added", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jo = new JSONObject(s);
                    s = jo.getString("InsertShopResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        ChangeFragment(new ShopPlaceOrderFragment(), args);
                    } else if (s.equals("False")) {
                        Toast.makeText(getActivity(), "SomethingIs Missing", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"Address Issue",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }

}
