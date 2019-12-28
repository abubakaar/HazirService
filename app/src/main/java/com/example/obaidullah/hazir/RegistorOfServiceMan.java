package com.example.obaidullah.hazir;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistorOfServiceMan extends Fragment {
    String  Srvname, Srvcontact, Srvpass, SrvconPass,SrvCNIC,SrvVN,SrvVM,imagestring,type,availabilty,lat,lng,have_req;
    EditText SrvcName,SrvcCno,SrvcPass,SrvcCpass,SrvcCNIC,SrvcSVN,SrvcSVM;
    Button SrvcReg;
    ImageView pic;
    ImageButton cmra,gallery;

    Bundle args= new Bundle();
    Static_IP sip= new Static_IP();
    String theurl= sip.geturl();
    String UURL=sip.geturl();
    private static final int CAMERA_REQUEST = 123;
    private int RequestCode = 1;
    Handler handler=new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_registor_of_service_man, container, false);
        SrvcName=(EditText)view.findViewById(R.id.srvnametxt);
        SrvcCno=(EditText)view.findViewById(R.id.srvcontacttxt);
        SrvcCNIC=(EditText)view.findViewById(R.id.srvcnictxt);
        SrvcSVN=(EditText)view.findViewById(R.id.srvvehcltxt);
        SrvcSVM=(EditText)view.findViewById(R.id.srvvehclmdltxt);
        SrvcPass=(EditText)view.findViewById(R.id.ServiceManPass);
        SrvcCpass=(EditText)view.findViewById(R.id.ServiceManCPass);
        SrvcReg=(Button)view.findViewById(R.id.ServiceManRegister);
        pic=(ImageView)view.findViewById(R.id.iv_register_picture);
        cmra=(ImageButton)view.findViewById(R.id.CMRA);
        gallery=(ImageButton)view.findViewById(R.id.glry);
        type=getArguments().getString("MyType");
        SrvcReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Srvname=SrvcName.getText().toString();
                Srvcontact=SrvcCno.getText().toString();
                SrvCNIC=SrvcCNIC.getText().toString();
                SrvVN=SrvcSVN.getText().toString();
                SrvVM=SrvcSVM.getText().toString();
                Srvpass=SrvcPass.getText().toString();
                SrvconPass=SrvcCpass.getText().toString();
                availabilty="null";
                lat="null";
                lng="null";
                have_req="null";
                UURL+="auto_status_DeliveryBoy/"+Srvcontact;
                callservice(UURL);
                theurl+="InsertPersonInfo/"+Srvname+"/"+SrvCNIC+"/"+Srvcontact+"/"+Srvpass+"/"+SrvconPass+"/"+type+"/"+SrvVN+"/"+SrvVM;
                callserviceReg(theurl);


               // new_getBackVolleyMethod(Srvname,SrvCNIC,Srvcontact,Srvpass,SrvconPass,type,SrvVN,SrvVM,imagestring);



            }
        });

        cmra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, CAMERA_REQUEST);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent();
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery_intent, "Select Picture"), RequestCode);
            }
        });





        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
            imagestring = getEncoded64ImageStringFromBitmap(imageBitmap);

        }

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                pic.setImageBitmap(bitmap);
                imagestring = getEncoded64ImageStringFromBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
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
                    s = jo.getString("auto_status_DeliveryBoyResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        //ChangeFragment(new ServiceManFragment(),args);
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

    public void callserviceReg(String address)
    {
        String url=address;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                //Toast.makeText(getActivity(),"Added", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jo = new JSONObject(s);
                    s = jo.getString("InsertPersonInfoResult");
                    if (s.equals("True")) {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        ChangeFragment(new ServiceManFragment(),args);
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
//    public void new_getBackVolleyMethod(final String Srvname,final String SrvCNIC,final String  Srvcontact,final String  Srvpass, final String SrvconPass,final String SrvType,final String SrvVehicalNo,final String SrvVehicalModel,final String SrvImage)
//    {  try {
//
//
//        Map<String,String> jsonParams = new HashMap<String,String>();
//            jsonParams.put("Person_Name", Srvname);
//            jsonParams.put("Cnic", SrvCNIC);
//            jsonParams.put("Mobile_No", Srvcontact);
//            jsonParams.put("Password", Srvpass);
//            jsonParams.put("Confirm_Password", SrvconPass);
//            jsonParams.put("Type", SrvType);
//            jsonParams.put("Vehical_No", SrvVehicalNo);
//            jsonParams.put("Vehical_Model", SrvVehicalModel);
//            jsonParams.put("image", SrvImage);
//
//            JSONObject jsonObject_sid= new JSONObject(jsonParams);
//
//            final RequestQueue requestQueue_sid = Volley.newRequestQueue(getActivity());
//
//            String temp=theurl;
//            temp+="InsertPersonInfo/";
//            requestQueue_sid.add(new JsonObjectRequest(Request.Method.POST, temp,jsonObject_sid, new Response.Listener<JSONObject>() {
//
//
//                public void onResponse(JSONObject response)
//                {
//
//                       Toast.makeText(getActivity(), "Request Submit", Toast.LENGTH_LONG).show();
//              //         ChangeFragment(new ServiceManFragment(), args);
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    //Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
//                }
//            }) {
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> jsonParams = new HashMap<>();
//                    jsonParams.put("Person_Name", Srvname);
//                    jsonParams.put("Cnic", SrvCNIC);
//                    jsonParams.put("Mobile_No", Srvcontact);
//                    jsonParams.put("Password", Srvpass);
//                    jsonParams.put("Confirm_Password", SrvconPass);
//                    jsonParams.put("Type", SrvType);
//                    jsonParams.put("Vehical_No", SrvVehicalNo);
//                    jsonParams.put("Vehical_Model", SrvVehicalModel);
//                    jsonParams.put("image", SrvImage);
//                    return jsonParams;
//                }
//
//                @Override
//                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                    try {
//                        String jsonString = new String(response.data,
//                                HttpHeaderParser
//                                        .parseCharset(response.headers));
//                        return Response.success(
//                                new JSONObject(jsonString),
//                                HttpHeaderParser
//                                        .parseCacheHeaders(response));
//                    } catch (Exception e) {
//                        return Response.error(new ParseError(e));
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

