package com.example.obaidullah.hazir;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceManFragment extends Fragment implements RoutingListener, OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    String position_lat,position_long;
    TextView Req_Cntct,ItmNAme,ItemPrice;
    TextView SRevrName,RevrMobile,SendItmName,SREN_NO;
    TextView GSndrName,GSndrMbl,GetItmName,GetReq_No;
    Switch availabilitySwitch;
    Button workdone;
    Boolean isAvail=false;
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();
    String U_Cno,D_Cno;
    String STATUS="coming";
    Handler handler=new Handler();
    RequestQueue rq;
    String itmid,cus_pick_positon_lati,cus_pick_positon_longi,itmname,itmprice,Revrname,RevrMbl,GetSenderName,GetSenderMbl ,Senditmname,GetitmName;
    Bundle args= new Bundle();
    int Shop=0,Send=0,Get=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_service_man, container, false);
        availabilitySwitch=(Switch) view.findViewById(R.id.Switch_Avail);
        workdone=(Button)view.findViewById(R.id.ServiceConfirm);
        workdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("wrk", "workdone");
                 if(workdone.getText().equals("Work Done")) //ya jab request accept hogi, to chale a wo kam ha, laqin abi sirf testing kar rhy is lye comment ha
                 {
                     jsonparse_Get_WorkType();
                 }else
                     if(workdone.getText().equals("Send Work Done")) {
                         args.putString("wrk", "Sendworkdone");
                         SENDjsonparse_Get_WorkType();

                     }
                     else
                         if(workdone.getText().equals("Get Work Done"))
                         {args.putString("wrk", "Getworkdone");
                             Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
                            Getjsonparse_Get_WorkType();
                         }

            }
        });
        DeliveryBoyData DD= new DeliveryBoyData();
        D_Cno=MainScreenFragment.UCno;
        UserData ud= new UserData();
        U_Cno=MainScreenFragment.UCno;

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.SMap);
        if (supportMapFragment == null) {
            FragmentManager fn = getFragmentManager();
            FragmentTransaction ft = fn.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.Map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        availabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //jab ya json parse call hoga to, availabilty on ho jae gi, handyman ki. wo query me kia hoa kaam
                if (isChecked==true)
                {
                    String temp= theurl;
                    temp+="DeliveryBoy_Available/"+position_lat+"/"+position_long+"/"+D_Cno;
//                    theurl+="handyman_Available/"+position_lat+"/"+position_lang+"/"+handy_contact;
                    Toast.makeText(getActivity(),"Call service wla bi", Toast.LENGTH_SHORT).show();
                    callservice_availabilty(temp);
                    runnable.run();
//                    runable1.run();
                }
                else if (isChecked==false)
                {
                    handler.removeCallbacks(runnable);
                    handler.removeCallbacks(runable1);
                    handler.removeCallbacks(runable2);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void updateMap(Location location) {
        LatLng userlocation = new LatLng(location.getLatitude(), location.getLongitude());
        position_lat= String.valueOf(location.getLatitude());
        position_long= String.valueOf(location.getLongitude());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,15));
        //      mMap.addMarker(new MarkerOptions().position(userlocation).title("Your Location"));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.untitled)));


    }

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



    private Runnable runable2= new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
            Getjsonparse_Arrays();
            handler.postDelayed(this,8000);
        }
    };

    private Runnable runable1= new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
            SENDjsonparse_Arrays();
            handler.postDelayed(this,8000);
        }
    };
    public void callservice_availabilty(String address)
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                //Toast.makeText(getActivity(),"Added", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jo = new JSONObject(s);
                    s = jo.getString("DeliveryBoy_AvailableResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();;
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
    private  Runnable runnable= new Runnable() {
        @Override
        public void run() {

                Toast.makeText(getActivity(), "CHAL RHA", Toast.LENGTH_SHORT).show();
                jsonparse_Arrays();

               // SENDjsonparse_Arrays();

            handler.postDelayed(this,8000);
        }
    };

    /////////////////////////////////////////////////////

    private  void  getRouteToMarker(LatLng destination)
    {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(Double.valueOf(position_lat),Double.valueOf(position_long)), destination)
                .build();
        routing.execute();
    }
////////////////////////////////////////////////////////






    ////////////////////////////////////////////////////////
    public void promptview() throws ParseException //prompt of HANDY RECIEVE request
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_prom_receive_reqst, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);
//
        Req_Cntct=(TextView) promptsView.findViewById(R.id.User_Contact);
        ItmNAme=(TextView)promptsView.findViewById(R.id.ItemName);
        ItemPrice=(TextView)promptsView.findViewById(R.id.ItemPrive);
       // final Date c = Calendar.getInstance().getTime();

//        System.out.println(newDateString);
//        final String s="null";

        // set dialog message

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                 handler.removeCallbacks(runnable);
                                //jo hnadyman sign in ha uska naam and contact daal dena idr    **(or bad me user ko isi contact k naam wale ki image show karana)**
                                String temp= theurl;
                                temp+="DeliveryBoy_response_to_user/"+D_Cno+"/"+position_lat+"/"+position_long+"/"+U_Cno;
//                                goingURL+="handyman_response_to_user/"+handy_name+"/"+handy_contact+"/"+position_lat+"/"+position_lang+"/"+user_name+"/"+user_contact;
                                callservice(temp);


                                LatLng destination = new LatLng(Double.valueOf(cus_pick_positon_lati), Double.valueOf(cus_pick_positon_longi));
                                mMap.clear();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,15));
                                mMap.addMarker(new MarkerOptions().position(destination).title("Destination Location"));




                                try {
                                    boolean success = mMap.setMapStyle(
                                            MapStyleOptions.loadRawResourceStyle(
                                                    getActivity(), R.raw.mapstyle));

                                    if (!success) {
                                        Log.e("MapsActivityRaw", "Style parsing failed.");
                                    }
                                } catch (Resources.NotFoundException e) {
                                    Log.e("MapsActivityRaw", "Can't find style.", e);
                                }

                                getRouteToMarker(destination);
                                workdone.setText("Work Done");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void Sendpromptview() throws ParseException //prompt of HANDY RECIEVE request
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View SENDpromptsView = li.inflate(R.layout.send_custom_prom_receive_reqst, null);

        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());

        alertDialogBuilder1.setView(SENDpromptsView);
//
        SREN_NO=(TextView) SENDpromptsView.findViewById(R.id.Send_User_Contact);
        SendItmName=(TextView)SENDpromptsView.findViewById(R.id.SendItemName);
        SRevrName=(TextView)SENDpromptsView.findViewById(R.id.SendReciverName);
        RevrMobile=(TextView)SENDpromptsView.findViewById(R.id.SendReciverMobile);
        // final Date c = Calendar.getInstance().getTime();

//        System.out.println(newDateString);
        final String s="null";

        // set dialog message

        alertDialogBuilder1
                .setCancelable(false)
                .setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                handler.removeCallbacks(runable1);
                                //jo hnadyman sign in ha uska naam and contact daal dena idr    **(or bad me user ko isi contact k naam wale ki image show karana)**
                                String temp= theurl;
                                temp+="SEND_DeliveryBoy_response_to_user/"+D_Cno+"/"+position_lat+"/"+position_long+"/"+U_Cno;
//                                goingURL+="handyman_response_to_user/"+handy_name+"/"+handy_contact+"/"+position_lat+"/"+position_lang+"/"+user_name+"/"+user_contact;
                                Sendcallservice(temp);


                                LatLng destination = new LatLng(Double.valueOf(cus_pick_positon_lati), Double.valueOf(cus_pick_positon_longi));
                                mMap.clear();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,15));
                                mMap.addMarker(new MarkerOptions().position(destination).title("Destination Location"));




                                try {
                                    boolean success = mMap.setMapStyle(
                                            MapStyleOptions.loadRawResourceStyle(
                                                    getActivity(), R.raw.mapstyle));

                                    if (!success) {
                                        Log.e("MapsActivityRaw", "Style parsing failed.");
                                    }
                                } catch (Resources.NotFoundException e) {
                                    Log.e("MapsActivityRaw", "Can't find style.", e);
                                }

                                getRouteToMarker(destination);
                                workdone.setText("Send Work Done");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder1.create();

        // show it
        alertDialog.show();
    }

/////////////////////////////////////////////////////////////////
public void getpromptview() throws ParseException //prompt of HANDY RECIEVE request
{
    // get prompts.xml view
    LayoutInflater li = LayoutInflater.from(getActivity());
    View getpromptsView = li.inflate(R.layout.get_custom_prom_receive_reqst, null);

    AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());

    alertDialogBuilder1.setView(getpromptsView);
//
    GetReq_No=(TextView) getpromptsView.findViewById(R.id.Get_User_Contact);
    GetItmName=(TextView)getpromptsView.findViewById(R.id.GetItemName);
    GSndrName=(TextView)getpromptsView.findViewById(R.id.GetSndrName);
    GSndrMbl=(TextView)getpromptsView.findViewById(R.id.GetSndrMobile);
    // final Date c = Calendar.getInstance().getTime();

//        System.out.println(newDateString);
    final String s="null";

    // set dialog message

    alertDialogBuilder1
            .setCancelable(false)
            .setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // get user input and set it to result
                            // edit text
                            handler.removeCallbacks(runable2);
                            //jo hnadyman sign in ha uska naam and contact daal dena idr    **(or bad me user ko isi contact k naam wale ki image show karana)**
                            String temp= theurl;
                            temp+="Get_DeliveryBoy_response_to_user/"+D_Cno+"/"+position_lat+"/"+position_long+"/"+U_Cno;
//                                goingURL+="handyman_response_to_user/"+handy_name+"/"+handy_contact+"/"+position_lat+"/"+position_lang+"/"+user_name+"/"+user_contact;
                            Getcallservice(temp);


                            LatLng destination = new LatLng(Double.valueOf(cus_pick_positon_lati), Double.valueOf(cus_pick_positon_longi));
                            mMap.clear();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,15));
                            mMap.addMarker(new MarkerOptions().position(destination).title("Destination Location"));




                            try {
                                boolean success = mMap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                getActivity(), R.raw.mapstyle));

                                if (!success) {
                                    Log.e("MapsActivityRaw", "Style parsing failed.");
                                }
                            } catch (Resources.NotFoundException e) {
                                Log.e("MapsActivityRaw", "Can't find style.", e);
                            }

                            getRouteToMarker(destination);
                            workdone.setText("Get Work Done");
                        }
                    })
            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

    // create alert dialog
    AlertDialog alertDialogget = alertDialogBuilder1.create();

    // show it
    alertDialogget.show();
}


    public void callservice(String address)  //*****imp*****//ya handyman jb request accept kare ga, to uska data response table me dale ga, or user ko hum ya table dekhae gy
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

//****imp****   // ya if else aik function ki ha, UPDATE FUNCTION KA MESSGE NI SHOW KRA RHA IDR ME KOI

                if (s.equals("{\"DeliveryBoy_response_to_userResult\":\"added\"}"))
                {
                    Toast.makeText(getActivity(), "Okay Ja rha ho", Toast.LENGTH_SHORT).show();
                }
                else if (s.equals("{\"DeliveryBoy_response_to_userResult\":\"false\"}"))

                {
                    Toast.makeText(getActivity(), "Working, Error to work on Request", Toast.LENGTH_SHORT).show();
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

    ////////////////////////////////////////////////////////

    public void Sendcallservice(String address)  //*****imp*****//ya handyman jb request accept kare ga, to uska data response table me dale ga, or user ko hum ya table dekhae gy
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

//****imp****   // ya if else aik function ki ha, UPDATE FUNCTION KA MESSGE NI SHOW KRA RHA IDR ME KOI

                if (s.equals("{\"SEND_DeliveryBoy_response_to_userResult\":\"added\"}"))
                {
                    Toast.makeText(getActivity(), "Okay Ja rha ho", Toast.LENGTH_SHORT).show();
                }
                else if (s.equals("{\"SEND_DeliveryBoy_response_to_userResult\":\"false\"}"))

                {
                    Toast.makeText(getActivity(), "Working, Error to work on Request", Toast.LENGTH_SHORT).show();
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






    public void Getcallservice(String address)  //*****imp*****//ya handyman jb request accept kare ga, to uska data response table me dale ga, or user ko hum ya table dekhae gy
    {
        String url=address;
        StringRequest request789 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

//****imp****   // ya if else aik function ki ha, UPDATE FUNCTION KA MESSGE NI SHOW KRA RHA IDR ME KOI

                if (s.equals("{\"Get_DeliveryBoy_response_to_userResult\":\"added\"}"))
                {
                    Toast.makeText(getActivity(), "Okay Ja rha ho", Toast.LENGTH_SHORT).show();
                }
                else if (s.equals("{\"Get_DeliveryBoy_response_to_userResult\":\"false\"}"))

                {
                    Toast.makeText(getActivity(), "Working, Error to work on Request", Toast.LENGTH_SHORT).show();
                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"Address Issue",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue789 = Volley.newRequestQueue(getActivity());
        rQueue789.add(request789);
    }




    //////////////////////////////////////////////////////////////

    public void jsonparse_Arrays() //*****imp*****//ya handyman ko user request ka data dekhaye ga
    {
//        String url="http://192.168.0.103/handyWCF/Service1.svc/handyman_recieve_request/"+position_lat+"/"+position_lang+"/123456678";

        String temp= theurl;
        temp+="request_setting/"+position_lat+"/"+position_long+"/"+D_Cno;
//        temp+="show_billing/"+U_Cno;
//        URL+="request_setting/"+position_lat+"/"+position_lang+"/"+handy_category+"/"+handy_contact;
        JsonObjectRequest jsonObjectRequest123= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray= response.getJSONArray("request_settingResult");

                            if(jsonArray.length()<=0)
                            {
                                Toast.makeText(getActivity(),"length issue", Toast.LENGTH_SHORT).show();
                                runable1.run();

                            }
                            if (jsonArray.length()>0) {
                                promptview();
                                handler.removeCallbacks(runnable);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Toast.makeText(getActivity(), "OKAY SUB", Toast.LENGTH_LONG).show();
                                    //text views
                                    itmname = jsonObject.getString("Item_Name");
                                    itmprice = jsonObject.getString("Item_Price");
                                    U_Cno = jsonObject.getString("U_Contact_No");


                                    //strings
                                    cus_pick_positon_lati = jsonObject.getString("Pick_latittude");
                                    cus_pick_positon_longi = jsonObject.getString("Pick_longitude");
                                    itmid=jsonObject.getString("Item_Id");
                                    ItmNAme.setText(String.valueOf(itmname));
                                    ItemPrice.setText(String.valueOf(itmprice));
                                    Req_Cntct.setText(String.valueOf(U_Cno));


                                    //ya islye k show prompt if Array has some data other wise no need
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
       RequestQueue rQueue123 = Volley.newRequestQueue(getActivity());
        rQueue123.add(jsonObjectRequest123);
    }

    public void SENDjsonparse_Arrays() //*****imp*****//ya handyman ko user request ka data dekhaye ga
    {
//        String url="http://192.168.0.103/handyWCF/Service1.svc/handyman_recieve_request/"+position_lat+"/"+position_lang+"/123456678";

        String temp= theurl;
        temp+="Send_request_setting/"+position_lat+"/"+position_long+"/"+D_Cno;
        JsonObjectRequest jsonObjectRequestxyz= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray= response.getJSONArray("Send_request_settingResult");

                            if(jsonArray.length()<=0)
                            {   runable2.run();
                                Toast.makeText(getActivity(),"length issue", Toast.LENGTH_SHORT).show();
                            }
                            if (jsonArray.length()>0)
                            {
                                Sendpromptview();
                                handler.removeCallbacks(runable1);
                            }
                            for (int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(),"OKAY SUB",Toast.LENGTH_LONG).show();
                                //text views
                                Senditmname=jsonObject.getString("Item_Name");
                                Revrname=jsonObject.getString("Reciver_Name");
                                RevrMbl=jsonObject.getString("Reciver_Mobile_No");
                                U_Cno= jsonObject.getString("U_Contact_No");


                                //strings
                                cus_pick_positon_lati=jsonObject.getString("Pick_latittude");
                                cus_pick_positon_longi=jsonObject.getString("Pick_longitude");

                                SendItmName.setText(String.valueOf(Senditmname));
                                SRevrName.setText(String.valueOf(Revrname));
                                RevrMobile.setText(String.valueOf(RevrMbl));
                                SREN_NO.setText(String.valueOf(U_Cno));


                                //ya islye k show prompt if Array has some data other wise no need
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue rQueuexyz = Volley.newRequestQueue(getActivity());
        rQueuexyz.add(jsonObjectRequestxyz);
    }

    ////////////////////////////////////////////////////

    public void Getjsonparse_Arrays() //*****imp*****//ya handyman ko user request ka data dekhaye ga
    {
//        String url="http://192.168.0.103/handyWCF/Service1.svc/handyman_recieve_request/"+position_lat+"/"+position_lang+"/123456678";

        String temp= theurl;
        temp+="Get_request_setting/"+position_lat+"/"+position_long+"/"+D_Cno;
        JsonObjectRequest jsonObjectRequestget= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getActivity(),"CHAL RHA", Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray= response.getJSONArray("Get_request_settingResult");

                            if(jsonArray.length()<=0)
                            {   runnable.run();
                                Toast.makeText(getActivity(),"length issue", Toast.LENGTH_SHORT).show();
                            }
                            if (jsonArray.length()>0)
                            {
                                getpromptview();
                                handler.removeCallbacks(runable2);
                            }
                            for (int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(),"OKAY SUB",Toast.LENGTH_LONG).show();
                                //text views
                                GetitmName=jsonObject.getString("Item_Name");
                                GetSenderName=jsonObject.getString("Sender_Name");
                                GetSenderMbl=jsonObject.getString("Sender_Mobile_No");
                                U_Cno= jsonObject.getString("U_Contact_No");


                                //strings
                                cus_pick_positon_lati=jsonObject.getString("Pick_latittude");
                                cus_pick_positon_longi=jsonObject.getString("Pick_longitude");

                                GetItmName.setText(String.valueOf(GetitmName));
                                GSndrName.setText(String.valueOf(GetSenderName));
                                GSndrMbl.setText(String.valueOf(GetSenderMbl));
                                GetReq_No.setText(String.valueOf(U_Cno));


                                //ya islye k show prompt if Array has some data other wise no need
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue rQueueget = Volley.newRequestQueue(getActivity());
        rQueueget.add(jsonObjectRequestget);
    }




    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }
String Status="Done";
    public void jsonparse_Get_WorkType() //hndyman bill generate karny k lye PEHLE list of worktype and payemnt get kare ga, or prompt me selected worktype show kare ga
    {

        String temp=theurl;
        temp+="show_billing/"+U_Cno+"/"+Status;
//        worktype_URL+="show_billing/"+handy_category;
        JsonObjectRequest jsonObjectRequest456= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray= response.getJSONArray("show_billingResult");
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(),"OKAY SUB",Toast.LENGTH_LONG).show();

                                //text views
//                                amount_list.add(jsonObject.getString("amount").trim());
//                                worktype_list.add(jsonObject.getString("worktype").trim());

//                                StateVO stateVO = new StateVO();
//                                stateVO.setTitle(worktype_list.get(i));
//                                stateVO.setSelected(false);
//                                listVOs_work.add(stateVO);
//
//                                StateVO stateVO2 = new StateVO();
//                                stateVO2.setTitle(amount_list.get(i));
//                                stateVO2.setSelected(false);
//                                listVOs_amount.add(stateVO2);


                            }
                            ChangeFragment(new ShopPlaceOrderFragment(),args);

//
//                            ArrayAdapter<String> worktypeadpater = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, worktype_list);
//                            worktype_spinner.setAdapter(worktypeadpater);
//
//
//                           promptview_handySet_Bill();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue rQueue456 = Volley.newRequestQueue(getActivity());
        rQueue456.add(jsonObjectRequest456);

    }


    public void SENDjsonparse_Get_WorkType() //hndyman bill generate karny k lye PEHLE list of worktype and payemnt get kare ga, or prompt me selected worktype show kare ga
    {

        String temp=theurl;
        temp+="send_show_billing/"+U_Cno+"/"+STATUS;
//        worktype_URL+="show_billing/"+handy_category;
        JsonObjectRequest jsonObjectRequest456= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray= response.getJSONArray("send_show_billingResult");
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(),"OKAY SUB",Toast.LENGTH_LONG).show();

                                //text views
//                                amount_list.add(jsonObject.getString("amount").trim());
//                                worktype_list.add(jsonObject.getString("worktype").trim());

//                                StateVO stateVO = new StateVO();
//                                stateVO.setTitle(worktype_list.get(i));
//                                stateVO.setSelected(false);
//                                listVOs_work.add(stateVO);
//
//                                StateVO stateVO2 = new StateVO();
//                                stateVO2.setTitle(amount_list.get(i));
//                                stateVO2.setSelected(false);
//                                listVOs_amount.add(stateVO2);


                            }
                            ChangeFragment(new SendPlaceOrderFragment(),args);

//
//                            ArrayAdapter<String> worktypeadpater = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, worktype_list);
//                            worktype_spinner.setAdapter(worktypeadpater);
//
//
//                           promptview_handySet_Bill();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue rQueue456 = Volley.newRequestQueue(getActivity());
        rQueue456.add(jsonObjectRequest456);

    }



    public void Getjsonparse_Get_WorkType() //hndyman bill generate karny k lye PEHLE list of worktype and payemnt get kare ga, or prompt me selected worktype show kare ga
    {

        String temp=theurl;
        temp+="Get_show_billing/"+U_Cno+"/"+STATUS;
//        worktype_URL+="show_billing/"+handy_category;
        JsonObjectRequest jsonObjectRequest456= new JsonObjectRequest(Request.Method.GET, temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray= response.getJSONArray("Get_show_billingResult");
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(),"OKAY SUB",Toast.LENGTH_LONG).show();

                                //text views
//                                amount_list.add(jsonObject.getString("amount").trim());
//                                worktype_list.add(jsonObject.getString("worktype").trim());

//                                StateVO stateVO = new StateVO();
//                                stateVO.setTitle(worktype_list.get(i));
//                                stateVO.setSelected(false);
//                                listVOs_work.add(stateVO);
//
//                                StateVO stateVO2 = new StateVO();
//                                stateVO2.setTitle(amount_list.get(i));
//                                stateVO2.setSelected(false);
//                                listVOs_amount.add(stateVO2);


                            }
                            ChangeFragment(new SendPlaceOrderFragment(),args);

//
//                            ArrayAdapter<String> worktypeadpater = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, worktype_list);
//                            worktype_spinner.setAdapter(worktypeadpater);
//
//
//                           promptview_handySet_Bill();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue rQueue456 = Volley.newRequestQueue(getActivity());
        rQueue456.add(jsonObjectRequest456);

    }



    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }


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
}
