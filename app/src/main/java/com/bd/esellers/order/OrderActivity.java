package com.bd.esellers.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.bd.esellers.R;
import com.bd.esellers.interfaces.NavBackBtnPress;
import com.bd.esellers.order.adapter.OrderAdapter;
import com.bd.esellers.order.orderModel.OrderDetails;
import com.bd.esellers.product.productModel.Product;


public class OrderActivity extends AppCompatActivity implements NavBackBtnPress, OrderAdapter.OrderDetailsClick,
        OrderDetailsFragment.OrdrFrgmntAction {

    private static final String TAG = "OrderActivity ";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        fragment = new OrderListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

//    @Override
//    public void onBackPressed() {
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count > 0) {
//            getFragmentManager().popBackStack();
//
//            //additional code
//        } else {
////            getSupportFragmentManager().popBackStack();
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onNavBackBtnPress() {
        finish();
    }

    @Override
    public void onDetailsClick(OrderDetails details) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("details", details);
        Fragment fragment = new OrderDetailsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        Log.d(TAG, "onDetailsClick: clicked");
    }

    @Override
    public void onBackToListClick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onReviewClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        Fragment fragment = new RatingFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}
