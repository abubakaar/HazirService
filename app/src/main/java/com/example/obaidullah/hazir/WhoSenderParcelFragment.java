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
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhoSenderParcelFragment extends Fragment {
    EditText BuySndrNaam, BuySndrMbl;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ImageButton GetSndrBack,GetSndrNxt;
    int SelectedID;
    Bundle args = new Bundle();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_who_sender_parcel, container, false);
        BuySndrNaam =(EditText)view.findViewById(R.id.BuySenderName);
        BuySndrMbl =(EditText)view.findViewById(R.id.BuySenderMobile);
        radioGroup=(RadioGroup)view.findViewById(R.id.RadioGroup);
        SelectedID=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)view.findViewById(SelectedID);
        GetSndrBack=(ImageButton)view.findViewById(R.id.BuySenderBack);
        GetSndrNxt=(ImageButton)view.findViewById(R.id.BuySenderNext);
        GetSndrNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new SendPlaceOrderFragment(),args);
            }
        });
        GetSndrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("", "");
                ChangeFragment(new SendPlaceOrderFragment(),args);
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
}
