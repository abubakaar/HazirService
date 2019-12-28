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
public class GetFragment extends Fragment {
EditText PItem;
String PrclItm;
ImageButton GetBack, GetNext;
Bundle args = new Bundle();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_get, container, false);
        // Inflate the layout for this fragment
        PItem=(EditText)view.findViewById(R.id.ParcelItem);
        GetBack=(ImageButton)view.findViewById(R.id.GBack);
        GetNext=(ImageButton)view.findViewById(R.id.GNext);
        GetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new UserFragment(),args);
            }
        });
        GetNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrclItm=PItem.getText().toString();
                args.putString("Ptyp",PrclItm);
                args.putString("GCnotyp2",getArguments().getString("GCnotyp1"));
                ChangeFragment(new GetPickAreaFragment(),args);
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
