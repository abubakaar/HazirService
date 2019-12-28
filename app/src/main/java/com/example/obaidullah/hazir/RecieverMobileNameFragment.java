package com.example.obaidullah.hazir;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecieverMobileNameFragment extends Fragment {

    String RName,RMbl;
    EditText ReceiverName,ReceiverMobile;
    ImageButton RcvrBck,RcvrNxt;
    Bundle args = new Bundle();
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reciever_mobile_name, container, false);
        ReceiverName=(EditText)view.findViewById(R.id.ReceiverName);
        ReceiverMobile=(EditText)view.findViewById(R.id.ReceiverMobile);
        RcvrBck=(ImageButton)view.findViewById(R.id.ReceiverBack);
        RcvrNxt=(ImageButton)view.findViewById(R.id.ReceiverNext);

        RcvrBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("","");
                ChangeFragment(new SentMarketArea(),args);
            }
        });
        RcvrNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RName=ReceiverName.getText().toString();
                RMbl=ReceiverMobile.getText().toString();
                args.putString("type","recievernamembl");
                theurl+="InsertSend/"+getArguments().getString("Styp2")+"/"+getArguments().getString("Styp1")+"/"+RName+"/"+RMbl+"/"+getArguments().getString("Cnotyp3")+"/"+"Pending"+"/"+getArguments().getString("PLat")+"/"+getArguments().getString("PLng")+"/"+getArguments().getString("DLat")+"/"+getArguments().getString("DLng");
                callservice(theurl);

            }
        });

        // Inflate the layout for this fragment
        return view;
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
                    s = jo.getString("InsertSendResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        ChangeFragment(new SendPlaceOrderFragment(),args);
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
