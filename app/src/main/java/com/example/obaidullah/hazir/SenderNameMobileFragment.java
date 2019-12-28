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
public class SenderNameMobileFragment extends Fragment {
    EditText SenderName,SenderMobile;
    String PSN,PSM;
    ImageButton SndrBck,SndrNxt;
    Bundle args = new Bundle();
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sender_name_mobile, container, false);
        SenderName=(EditText)view.findViewById(R.id.SenderName);
        SenderMobile=(EditText)view.findViewById(R.id.SenderMobile);
        SndrBck=(ImageButton)view.findViewById(R.id.SenderBack);
        SndrNxt=(ImageButton)view.findViewById(R.id.SenderNext);
        SndrBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new GetPickAreaFragment(), args);
            }
        });
        SndrNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PSN=SenderName.getText().toString();
                PSM=SenderMobile.getText().toString();
                args.putString("type", "SenderNameMobile");
                theurl+="InsertGet/"+getArguments().getString("Ptyp2")+"/"+getArguments().getString("Ptyp1")+"/"+PSN+"/"+PSM+"/"+getArguments().getString("GCnotyp3")+"/"+"Pending"+"/"+getArguments().getString("GPLat")+"/"+getArguments().getString("GPLng")+"/"+getArguments().getString("GDLat")+"/"+getArguments().getString("GDLng");
                callservice(theurl);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void ChangeFragment(Fragment fragment, Bundle args) {
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
                    s = jo.getString("InsertGetResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        ChangeFragment(new SendPlaceOrderFragment(),args);
                    } else if (s.equals("False")) {
                        Toast.makeText(getActivity(), "SomethingIs Missing", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
