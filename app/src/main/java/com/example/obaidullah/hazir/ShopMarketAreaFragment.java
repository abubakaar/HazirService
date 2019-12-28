package com.example.obaidullah.hazir;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */


public class ShopMarketAreaFragment extends Fragment {
    String ShopMarket;
    private final  int requestcode=1;
    ImageButton MapBtn, MarketNext, MarketBack;
    Bundle args = new Bundle();
    String searched_location_lati, searched_location_longi;
    String position_lat, position_lang;
    PlaceAutocompleteFragment autocompleteFragment;
    Static_IP sip = new Static_IP();
    String theurl = sip.geturl();
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_market_area, container, false);
        //    TxtMarket =(EditText)view.findViewById(R.id.market);
        MapBtn = (ImageButton) view.findViewById(R.id.BuyMapIcon);
        MarketBack = (ImageButton) view.findViewById(R.id.MBack);
        MarketNext = (ImageButton) view.findViewById(R.id.MNext);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(getActivity(),"Allow gps", Toast.LENGTH_LONG).show();
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationget();
        }

        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "Shopping");
                ChangeFragment(new MapFragment(), args);
            }
        });
        MarketBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new ShopMarketAreaFragment(), args);
            }
        });
        MarketNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("picklat", "searched_location_lati");
                args.putString("picklng", "searched_location_longi");
                args.putString("drplat", "position_lat");
                args.putString("drplng", "position_lang");
                theurl += "InsertShop/" + getArguments().getString("MyType1") + "/" + getArguments().getString("MyType2") + "/" + ShopMarket + "/" + getArguments().getString("Unotyp2") + "/" + "Pending" + "/" + searched_location_lati + "/" + searched_location_longi + "/" + position_lat + "/" + position_lang;
                callservice(theurl);
            }
        });

        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.market);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                searched_location_lati = String.valueOf(place.getLatLng().latitude);
                searched_location_longi = String.valueOf(place.getLatLng().longitude);


                Toast.makeText(getActivity(), "Lati" + searched_location_lati + "\nLongi" + searched_location_longi, Toast.LENGTH_LONG).show();
                Log.i(TAG, "Place: " + place.getName());
                ShopMarket = String.valueOf(place.getName());


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
                    if (s.equals(s)) {
                        args.putString("itmid",s);
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
