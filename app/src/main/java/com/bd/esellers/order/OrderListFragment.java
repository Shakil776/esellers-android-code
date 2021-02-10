package com.bd.esellers.order;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bd.esellers.R;
import com.bd.esellers.interfaces.NavBackBtnPress;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.order.adapter.OrderAdapter;
import com.bd.esellers.order.orderModel.CustomerOrder;
import com.bd.esellers.order.orderModel.OrderHistoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bd.esellers.userAuth.SessionManager.ID;
import static com.bd.esellers.userAuth.SessionManager.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {


    private static final String TAG = "OrderListFragment ";
    private Toolbar toolbar;
    private RecyclerView orderRV;
    private Context context;
    private String cid;
    private List<CustomerOrder> orderList;
    private OrderAdapter adapter;
    private SharedPreferences prefs;


    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;

        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        cid = prefs.getString(ID, "No id defined");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_list, container, false);
        toolbar=view.findViewById(R.id.toolbar);
        orderRV=view.findViewById(R.id.orderRV);


        orderList=new ArrayList<>();
        adapter=new OrderAdapter(orderList, context);


        orderRV.setHasFixedSize(true);
        orderRV.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        orderRV.setAdapter(adapter);

        getOrderHistory(cid);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Order History");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavBackBtnPress)context).onNavBackBtnPress();
            }
        });
    }


    private void getOrderHistory(String cid) {
        Call<OrderHistoryResponse> call= RetrofitClient.getInstance().getApiInterface().getOrder(Integer.parseInt(cid));
        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        orderList=response.body().getCustomerOrders();
                        adapter.updateList(orderList);
                        Log.e(TAG, "onResponse: "+orderList.size() );
                    }else
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }else
                    Log.e(TAG, "onResponse: "+response.code() );
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }
}
