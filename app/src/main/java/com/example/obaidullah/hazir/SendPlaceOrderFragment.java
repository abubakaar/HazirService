package com.example.obaidullah.hazir;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendPlaceOrderFragment extends Fragment {
    TextView PD,Adrs,RcvrNaam,Rcvrmbl,EstmedDlvryChrgs,SendTotal;
    Button SndOthrPlc,SndPlcOrdr;
    UserData data= new UserData();
    String apnaContact= String.valueOf(data.con);
    String Itmname,price,DrpLoc,Rcvrname,RcvrMbl,EDP,TOTAL;
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();
    RequestQueue rq;
    Bundle args = new Bundle();
    String STATUS="Pending";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_send_place_order, container, false);
        PD=(TextView)view.findViewById(R.id.ParcelDtl);
        Adrs=(TextView)view.findViewById(R.id.SendAdr);
        RcvrNaam=(TextView)view.findViewById(R.id.SndrName);
        Rcvrmbl=(TextView)view.findViewById(R.id.SndrMobile);
        EstmedDlvryChrgs=(TextView)view.findViewById(R.id.EDCS);
        SendTotal=(TextView)view.findViewById(R.id.SendTotal);
        SndOthrPlc=(Button)view.findViewById(R.id.SendOtherPlace);
        SndPlcOrdr=(Button)view.findViewById(R.id.SendPlaceOrderConfirm);



        jsonparse_Arrays();
        SndPlcOrdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new UserFragment(),args);
            }
        });

        SndOthrPlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "SendPlaceOrderMap");
                ChangeFragment(new MapFragment(),args);
            }
        });

//Inflate the layout for this fragment
        return view;
    }
    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }

    public void jsonparse_Arrays()
    {
        String tem= theurl;

        if(getArguments().getString("type").equals("SenderNameMobile"))

        {
            SndOthrPlc.setVisibility(View.INVISIBLE);

            tem += "Get_show_billing/" + apnaContact+"/"+STATUS;
            JsonObjectRequest jsonObjectRequest12= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                    Itmname= jsonObject.getString("Item_Name");
                                    DrpLoc= jsonObject.getString("Get_Area");
                                    Rcvrname= jsonObject.getString("Sender_Name");
                                    RcvrMbl=jsonObject.getString("Sender_Mobile_No");
                                    EDP=jsonObject.getString("Delivery_Chargers");
                                    PD.setText(Itmname);
                                    Adrs.setText(DrpLoc);
                                    RcvrNaam.setText(Rcvrname);
                                    Rcvrmbl.setText(RcvrMbl);
                                    EstmedDlvryChrgs.setText(EDP);
                                    SendTotal.setText(EDP);

                                }
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
            RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest12);
        }else
            if(getArguments().getString("type").equals("GetCnfrmMap")){
                {
                    SndOthrPlc.setVisibility(View.INVISIBLE);

                    tem += "Get_show_billing/" + apnaContact+"/"+STATUS;
                    JsonObjectRequest jsonObjectRequest12 = new JsonObjectRequest(Request.Method.GET, tem, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray = response.getJSONArray("Get_show_billingResult");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Toast.makeText(getActivity(), "OKAY SUB", Toast.LENGTH_LONG).show();
                                            //text views
                                            Itmname = jsonObject.getString("Item_Name");
                                            DrpLoc = jsonObject.getString("Get_Area");
                                            Rcvrname = jsonObject.getString("Sender_Name");
                                            RcvrMbl = jsonObject.getString("Sender_Mobile_No");
                                            PD.setText(Itmname);
                                            Adrs.setText(DrpLoc);
                                            RcvrNaam.setText(Rcvrname);
                                            Rcvrmbl.setText(RcvrMbl);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }

                    });
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(jsonObjectRequest12);
                }
            }else

        if(getArguments().getString("type").equals("recievernamembl"))
        {

            tem+="send_show_billing/"+apnaContact+"/"+STATUS;
            JsonObjectRequest jsonObjectRequest22= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                    Itmname= jsonObject.getString("Item_Name");
                                    DrpLoc= jsonObject.getString("Send_Area");
                                    Rcvrname= jsonObject.getString("Reciver_Name");
                                    RcvrMbl=jsonObject.getString("Reciver_Mobile_No");
                                    PD.setText(Itmname);
                                    Adrs.setText(DrpLoc);
                                    RcvrNaam.setText(Rcvrname);
                                    Rcvrmbl.setText(RcvrMbl);

                                }

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
      RequestQueue rsq = Volley.newRequestQueue(getActivity());
            rsq.add(jsonObjectRequest22);

        }else
            if(getArguments().getString("type").equals("sndcnfmmap"))
            {
                tem+="send_show_billing/"+apnaContact+"/"+STATUS;
                JsonObjectRequest jsonObjectRequest22= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                        Itmname= jsonObject.getString("Item_Name");
                                        DrpLoc= jsonObject.getString("Send_Area");
                                        Rcvrname= jsonObject.getString("Reciver_Name");
                                        RcvrMbl=jsonObject.getString("Reciver_Mobile_No");
                                        PD.setText(Itmname);
                                        Adrs.setText(DrpLoc);
                                        RcvrNaam.setText(Rcvrname);
                                        Rcvrmbl.setText(RcvrMbl);

                                    }

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
                RequestQueue rsq = Volley.newRequestQueue(getActivity());
                rsq.add(jsonObjectRequest22);

            }
            else if(getArguments().getString("wrk").equals("Sendworkdone"))
            {SndOthrPlc.setVisibility(View.INVISIBLE);
                SndPlcOrdr.setVisibility(View.INVISIBLE);
                STATUS="Done";
                tem+="send_show_billing/"+apnaContact+"/"+STATUS;
                JsonObjectRequest jsonObjectRequest221= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                        Itmname= jsonObject.getString("Item_Name");
                                        DrpLoc= jsonObject.getString("Send_Area");
                                        Rcvrname= jsonObject.getString("Reciver_Name");
                                        RcvrMbl=jsonObject.getString("Reciver_Mobile_No");
                                        PD.setText(Itmname);
                                        Adrs.setText(DrpLoc);
                                        RcvrNaam.setText(Rcvrname);
                                        Rcvrmbl.setText(RcvrMbl);

                                    }

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
                RequestQueue rsq111 = Volley.newRequestQueue(getActivity());
                rsq111.add(jsonObjectRequest221);

            }
            else if(getArguments().getString("wrk").equals("Getworkdone"))
            {
                SndOthrPlc.setVisibility(View.INVISIBLE);
                SndPlcOrdr.setVisibility(View.INVISIBLE);
                STATUS="Done";
                tem += "Get_show_billing/" + apnaContact+"/"+STATUS;
                JsonObjectRequest jsonObjectRequest121 = new JsonObjectRequest(Request.Method.GET, tem, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("Get_show_billingResult");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Toast.makeText(getActivity(), "OKAY SUB", Toast.LENGTH_LONG).show();
                                        //text views
                                        Itmname = jsonObject.getString("Item_Name");
                                        DrpLoc = jsonObject.getString("Get_Area");
                                        Rcvrname = jsonObject.getString("Sender_Name");
                                        RcvrMbl = jsonObject.getString("Sender_Mobile_No");
                                        PD.setText(Itmname);
                                        Adrs.setText(DrpLoc);
                                        RcvrNaam.setText(Rcvrname);
                                        Rcvrmbl.setText(RcvrMbl);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }

                });
                RequestQueue queue131 = Volley.newRequestQueue(getActivity());
                queue131.add(jsonObjectRequest121);

            }


    }
}

