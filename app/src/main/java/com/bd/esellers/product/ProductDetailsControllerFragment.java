package com.bd.esellers.product;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.bd.esellers.R;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.product.productModel.Product;
import com.bd.esellers.utility.DialogClass;
import com.bd.esellers.vendor.Vendor;
import com.bd.esellers.vendor.VendorResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsControllerFragment extends Fragment {


    private static final String TAG = "ProdctDtlsContrlrFrgmnt";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Product product;
    private Vendor vendor;
    private Bundle bundle;
    private Context context;
    private DialogClass dialogClass;


    public ProductDetailsControllerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        dialogClass=new DialogClass(context);
        bundle = this.getArguments();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            vendor = new Vendor();
//            getVendorInfo();
//            dialogClass.showProgressDialog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details_controller, container, false);


        viewPager = view.findViewById(R.id.myViewPager);
        tabLayout = view.findViewById(R.id.myTabLayout);


//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite));

        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Review"));
//        tabLayout.addTab(tabLayout.newTab().setText("Vendor Info"));

        tabLayout.setSelectedTabIndicatorColor(Color.RED);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite_border_black_24dp);

//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                tab.getIcon().setColorFilter(Color.parseColor("#FFF52844"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(Color.parseColor("#4B0912"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }


//    private void getVendorInfo() {
//
//        Call<VendorResponse> call = RetrofitClient.getInstance().getApiInterface().getVendor(Integer.parseInt(product.getVendorId()));
//        call.enqueue(new Callback<VendorResponse>() {
//            @Override
//            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getStatus() == 1) {
//                        List<Vendor> vendors = response.body().getVendor();
//                        vendor = vendors.get(0);
//                        dialogClass.closeProgressDialog();
//                    } else
//                        Log.e(TAG, "onResponse: " + response.body().getMessage());
//                } else
//                    Log.e(TAG, "onResponse: " + response.code());
//            }
//
//            @Override
//            public void onFailure(Call<VendorResponse> call, Throwable t) {
//                Log.e(TAG, "onResponse: " + t.getLocalizedMessage());
//                dialogClass.closeProgressDialog();
//            }
//        });
//    }




    private class TabPagerAdapter extends FragmentPagerAdapter {

        private int tabCount;

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            switch (i) {
                case 0:
                    bundle.putSerializable("product", product);
                    fragment = new ProductDetailsFragment();
                    fragment.setArguments(bundle);
                    return fragment;

                case 1:
                    bundle.putSerializable("product", product);
                    fragment = new ProductReviewFragment();
                    fragment.setArguments(bundle);
                    return fragment;

//                case 2:
//                    bundle.putSerializable("vendor", vendor);
//                    fragment = new VendorDetailsFragment();
//                    fragment.setArguments(bundle);
//                    return fragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


//    private void showProgressDialog() {
//        alertDialog = new Dialog(context);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setContentView(R.layout.loading_progress);
//        alertDialog.setCancelable(true);
//        final ProgressBar progressBar = alertDialog.findViewById(R.id.circular_progressBar);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();
//    }

}
