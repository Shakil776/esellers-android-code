package com.bd.esellers.product;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bd.esellers.R;
import com.bd.esellers.vendor.Vendor;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorDetailsFragment extends Fragment {

    private TextView vNameTV, vEmailTv, vAddressTV, vContactTV, vDescTV;
    private Vendor vendor;
    private Bundle bundle;

    public VendorDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bundle = this.getArguments();
        if (bundle != null) {
            vendor = (Vendor) bundle.getSerializable("vendor");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vendor_details, container, false);
        vNameTV=view.findViewById(R.id.vNameTV);
        vEmailTv=view.findViewById(R.id.vEmailTV);
        vAddressTV=view.findViewById(R.id.vAddressTV);
        vContactTV=view.findViewById(R.id.vContactTV);
        vDescTV=view.findViewById(R.id.vDescTV);

        vNameTV.setText(vendor.getName());
        vEmailTv.setText(vendor.getEmail());
        vAddressTV.setText(vendor.getAddress());
        vContactTV.setText(vendor.getMobile());
        vDescTV.setText(vendor.getVendorDescription());

        return view;
    }

}
