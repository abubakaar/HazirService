package com.example.obaidullah.hazir;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistorFragment extends Fragment {
String  uname, ucontact, upass, uconPass;
EditText Name,Cno,Pass,Cpass;
Button Sgup;
Bundle args= new Bundle();
    Static_IP sip= new Static_IP();
   String theurl= sip.geturl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registor, container, false);

        Name=(EditText)view.findViewById(R.id.input_name);
        Cno=(EditText)view.findViewById(R.id.Cntct);
        Pass=(EditText)view.findViewById(R.id.input_password);
        Cpass=(EditText)view.findViewById(R.id.input_cpassword);

        Sgup=(Button)view.findViewById(R.id.RegistorForm);
        Sgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=Name.getText().toString();
                ucontact=Cno.getText().toString();
                upass=Pass.getText().toString();
                uconPass=Cpass.getText().toString();
               Bundle bundle= getArguments();
             String type= bundle.getString("MyType");

                theurl+="InsertUser/"+uname+"/"+ucontact+"/"+upass+"/"+uconPass+"/"+type;
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
                    s = jo.getString("InsertUserResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        ChangeFragment(new UserFragment(),args);
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
