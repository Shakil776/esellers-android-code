package com.bd.esellers.cart;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.esellers.R;
import com.bd.esellers.cart.adapter.CartAdapter;
import com.bd.esellers.cart.cartModel.Cart;
import com.bd.esellers.interfaces.NavBackBtnPress;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bd.esellers.mainApp.MainActivity.cartSet;
import static com.bd.esellers.mainApp.MainActivity.grandTotalPlus;
import static com.bd.esellers.userAuth.SessionManager.HAS_ADDRESS;
import static com.bd.esellers.userAuth.SessionManager.LOGIN;
import static com.bd.esellers.userAuth.SessionManager.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartListFragment extends Fragment {

    private static final String TAG = "CartListFragment ";
    private RecyclerView recyclerView;
    public static TextView totalTV;
    private Button orderBtn;
    private CartAdapter adapter;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private Context context;
    private CartFrgmntAction action;


    public CartListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        action = (CartFrgmntAction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);

        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.cartRV);
        totalTV = view.findViewById(R.id.totalTV);
        orderBtn = view.findViewById(R.id.placeOrderBtn);

        List<Cart> tempArrayList = new ArrayList<>();
        tempArrayList.addAll(cartSet);
        adapter = new CartAdapter(tempArrayList, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        adapter.updateList(tempArrayList);

        for (int i = 0; i < tempArrayList.size(); i++) {
            grandTotalPlus = grandTotalPlus + tempArrayList.get(i).getTotalCash();
        }
        totalTV.setText("TK. " + String.valueOf(grandTotalPlus));
        recyclerView.setAdapter(adapter);


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean(LOGIN, false)) {       //for checking is user logged in
                    if (cartSet.size() != 0) {
                        if (prefs.getBoolean(HAS_ADDRESS,false)) {       // for checking is user update profile
                            action.onPlaceOrderClick(grandTotalPlus);
                        }else {
                            Toast.makeText(context, "Please update your profile", Toast.LENGTH_SHORT).show();
                            action.onProfileUpdate();
                        }
                    } else
                        Toast.makeText(context, "u have no item in the list", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Please signIn to place order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cart");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavBackBtnPress) context).onNavBackBtnPress();
            }
        });
    }


    public interface CartFrgmntAction {
        void onPlaceOrderClick(double total);
        void onProfileUpdate();
    }

}
