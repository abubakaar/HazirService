package com.example.obaidullah.hazir;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
Bundle args= new Bundle();
Button Shop,Send,Get;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        Shop=(Button)view.findViewById(R.id.ButtonShop);
        Send=(Button)view.findViewById(R.id.ButtonSend);
        Get=(Button)view.findViewById(R.id.ButtonGet);
        // Inflate the layout for this fragment

        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Unotyp1",getArguments().getString("Unotyp"));
                ChangeFragment(new BuyFragment(),args);
            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Cnotyp1",getArguments().getString("Unotyp"));
                ChangeFragment(new SendFragment(),args);
            }
        });
        Get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("GCnotyp1",getArguments().getString("Unotyp"));
                ChangeFragment(new GetFragment(),args);
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
