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
public class ShopRecieverNameMobileFragment extends Fragment {
    EditText ShopReceiverName,ShopReceiverMobile;
    ImageButton ShpRcvrBck,ShpRcvrNxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop_reciever_name_mobile, container, false);
        // Inflate the layout for this fragment
        ShopReceiverName=(EditText)view.findViewById(R.id.ShopReceiverName);
        ShopReceiverMobile=(EditText)view.findViewById(R.id.ShopReceiverMobile);
        ShpRcvrBck=(ImageButton)view.findViewById(R.id.ShopBack);
        ShpRcvrNxt=(ImageButton)view.findViewById(R.id.ShopNext);
        ShpRcvrBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new ShopPlaceOrderFragment());
            }
        });
        ShpRcvrNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new ShopPlaceOrderFragment());
            }
        });
        return view;
    }
    public void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("std").commit();
    }
}
