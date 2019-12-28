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


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment {

String Snditm;
EditText SendItem;
ImageButton SendBack, SendNext;
Bundle args= new Bundle();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_send, container, false);
        // Inflate the layout for this fragment
        SendItem=(EditText)view.findViewById(R.id.SendItem);
        SendBack=(ImageButton)view.findViewById(R.id.SBack);
        SendNext=(ImageButton)view.findViewById(R.id.SNext);
        SendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new UserFragment(),args);
            }
        });
        SendNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snditm=SendItem.getText().toString();
                args.putString("Styp",Snditm);
                args.putString("Cnotyp2",getArguments().getString("Cnotyp1"));
                ChangeFragment(new SentMarketArea(),args);
            }
        });


        return view;
    }
    public void ChangeFragment(Fragment fragment,Bundle args) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }

}
