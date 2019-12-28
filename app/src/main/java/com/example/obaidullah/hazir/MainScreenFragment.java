package com.example.obaidullah.hazir;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class MainScreenFragment extends Fragment {


    EditText Name,Pass;
    Button SignIn,Reg;
    Integer selected;
    Intent i;
  public static String UCno,Upss;
    Bundle args = new Bundle();
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        Name = (EditText)view.findViewById(R.id.Name);
        Pass=(EditText)view.findViewById(R.id.Password);
        SignIn =(Button)view.findViewById(R.id.btn_SignIn);
        Reg=(Button)view.findViewById(R.id.Signup);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UCno=Name.getText().toString();
                Upss=Pass.getText().toString();
                args.putString("Unotyp",UCno);
                theurl+="Login/"+UCno+"/"+Upss;
                callservice(theurl);

//                ChangeFragment(new UserFragment(),args);

            }
        });


      Reg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String[] select={"User", "Delivery Boy"};
              AlertDialog dialog=new AlertDialog.Builder(getActivity())
                      .setTitle("Register As")
                      .setSingleChoiceItems(select, 3, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              selected = which;
                              if (selected==0)
                              {
                                  args.putString("MyType","User");
                                  Toast.makeText(getActivity(), "You Will Registor From User", Toast.LENGTH_SHORT).show();
                                  ChangeFragment(new RegistorFragment(),args);
                                  dialog.dismiss();

                              }
                              else if (selected==1)
                              {
                                  args.putString("MyType","DelieveryBoy");
                                  Toast.makeText(getActivity(), "You Will Registor From Delivery Boy", Toast.LENGTH_SHORT).show();
                                  ChangeFragment(new RegistorOfServiceMan(),args);
                                  dialog.dismiss();
                              }

                          }
                      })

                      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                              }
                      }).create();
              dialog.show();

          }


      });
        return view;
    }

    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragment.setArguments(args);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("std").commit();
    }


    public void callservice(String address)
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                //Toast.makeText(getActivity(),"Added", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getActivity(),"Added", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject js = new JSONObject(s);
                        String a =  js.getString("LoginResult");
                        if (a.equals("User"))
                        {
                            Toast.makeText(getActivity(),"Login", Toast.LENGTH_SHORT).show();
                            ChangeFragment(new UserFragment(),args);
                        }
                        else
                        if (a.equals("DelieveryBoy"))
                        {
                            Toast.makeText(getActivity(),"Login", Toast.LENGTH_SHORT).show();
                            ChangeFragment(new ServiceManFragment(),args);

                        }
                        else
                            Toast.makeText(getActivity(), "Contact dn't exist", Toast.LENGTH_SHORT).show();

                    }catch (Exception e) {
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
