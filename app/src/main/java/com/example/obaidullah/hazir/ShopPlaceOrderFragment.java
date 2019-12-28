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
public class ShopPlaceOrderFragment extends Fragment {
Button ShopOtherDeliver,ShopPlaceOrder;
TextView SEC,SPP,ItemDetail,ShopAddress,STotal;
    Bundle args = new Bundle();
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();
    String Uurl=sip.geturl();
    UserData data= new UserData();
    String apnaContact= String.valueOf(data.con);
    String name,price,market,dlrychrges,Status="Pending";
    RequestQueue rq;
    static String s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop_place_order, container, false);

        // Inflate the layout for this fragment
        ShopOtherDeliver=(Button) view.findViewById(R.id.ShopOtherPlace);
        ShopPlaceOrder=(Button) view.findViewById(R.id.ShopPlaceOrder);
        ItemDetail=(TextView)view.findViewById(R.id.ItmDtl);
        ShopAddress=(TextView)view.findViewById(R.id.ShpAdr);
        STotal=(TextView)view.findViewById(R.id.ShopTotal);
        SEC=(TextView)view.findViewById(R.id.ShopDeliveryCharges);
        SPP=(TextView)view.findViewById(R.id.ShopProductPrice);

        s=getArguments().getString("itmid");

        rq = Volley.newRequestQueue(getActivity());

        jsonparse_Arrays();




        ShopPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new UserFragment(),args);
            }
        });
        ShopOtherDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("type", "ShopPlaceOrderMap");
                ChangeFragment(new MapFragment(),args);
            }
        });
        return view;
    }
    public void ChangeFragment(Fragment fragment, Bundle args)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }

    public void jsonparse_Arrays()
    {
//        String temp1=Uurl;
//        temp1+="Deliverycharges/"+getArguments().getString("picklat")+"/"+getArguments().getString("picklng")+"/"+getArguments().getString("drplat")+"/"+getArguments().getString("drplng")+"/"+apnaContact;
      String tem= theurl;
        if(getArguments().getString("wrk").equals("workdone"))
        {
            ShopOtherDeliver.setVisibility(View.INVISIBLE);
            ShopPlaceOrder.setVisibility(View.INVISIBLE);
            Status="Done";
            tem+="show_billing/"+apnaContact+"/"+Status;
            JsonObjectRequest jsonObjectRequest;

            jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                    name= jsonObject.getString("Item_Name");
                                    price= jsonObject.getString("Item_Price");
                                    market= jsonObject.getString("Shop_Market");
                                    dlrychrges=jsonObject.getString("Delivery_Chargers");

                                    price+="RS";


                                    ItemDetail.setText(name);
                                    ShopAddress.setText(market);
                                    SEC.setText(dlrychrges);
                                    SPP.setText(price);
                                    int r= Integer.parseInt(price);
                                    int s=Integer.parseInt(dlrychrges);
                                    String z=String.valueOf(r+s);
                                    z+="RS";
                                    STotal.setText(z);
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
            rq = Volley.newRequestQueue(getActivity());
            rq.add(jsonObjectRequest);
       }
 else
            if(getArguments().getString("itmid").equals(s))
            {
                tem+="show_billing/"+apnaContact+"/"+Status;
                JsonObjectRequest jsonObjectRequest;

             JsonObjectRequest   jsonObjectRequest94= new JsonObjectRequest(Request.Method.GET, tem, null,
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
                                        name= jsonObject.getString("Item_Name");
                                        price= jsonObject.getString("Item_Price");
                                        market= jsonObject.getString("Shop_Market");
                                        dlrychrges=jsonObject.getString("Delivery_Chargers");
                                        price+="RS";
                                        ItemDetail.setText(name);
                                        ShopAddress.setText(market);
                                        SEC.setText(dlrychrges);
                                        SPP.setText(price);
                                        int r= Integer.parseInt(price);
                                        int s=+Integer.parseInt(dlrychrges);
                                        String z=String.valueOf(r+s);
                                        z+="RS";
                                        STotal.setText(z);
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
                RequestQueue rq94 = Volley.newRequestQueue(getActivity());
                rq94.add(jsonObjectRequest94);
            }


    }







}
