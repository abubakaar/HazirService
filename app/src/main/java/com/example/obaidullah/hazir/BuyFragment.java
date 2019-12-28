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
public class BuyFragment extends Fragment {

EditText PName,PPrice;
String PrdctName,PrdutPrice;
ImageButton BuyBack,BuyNext;
Bundle args= new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_buy, container, false);
        PName=(EditText)view.findViewById(R.id.NameOfProduct);
        PPrice=(EditText)view.findViewById(R.id.Price);
        BuyBack=(ImageButton)view.findViewById(R.id.BBack);
        BuyNext=(ImageButton)view.findViewById(R.id.BNext);
        BuyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment(new UserFragment(),args);
            }
        });

        BuyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrdctName=PName.getText().toString();
                PrdutPrice=PPrice.getText().toString();
                args.putString("MyType1", PrdctName);
                args.putString("MyType2", PrdutPrice);
                args.putString("Unotyp2",getArguments().getString("Unotyp1"));
                ChangeFragment(new ShopMarketAreaFragment(),args);
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
