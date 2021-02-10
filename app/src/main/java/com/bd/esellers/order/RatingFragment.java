package com.bd.esellers.order;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bd.esellers.R;
import com.bd.esellers.interfaces.NavBackBtnPress;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.order.reviewModel.ReviewPostResponse;
import com.bd.esellers.product.productModel.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bd.esellers.userAuth.SessionManager.ID;
import static com.bd.esellers.userAuth.SessionManager.PREF_NAME;
import static com.bd.esellers.utility.Constant.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    private static final String TAG = "RatingFragment ";
    private Button sendBtn;
    private ImageView imageView;
    private RatingBar ratingBar;
    private EditText reviewET;
    private Toolbar toolbar;
    private Context context;
    private SharedPreferences preferences;
    private int productId;
    private Product product;
    private String review;
    private double rating;

    public RatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        sendBtn = view.findViewById(R.id.sendBtn);
        imageView = view.findViewById(R.id.productIV);
        reviewET = view.findViewById(R.id.reviewET);
        ratingBar = view.findViewById(R.id.rating);
        toolbar = view.findViewById(R.id.toolbar);

        preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        final String uid = preferences.getString(ID, "No id defined");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            productId = product.getId();
        }

        Glide.with(context)
                .load(BASE_URL + product.getProductImage())
                .into(imageView);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review=reviewET.getText().toString();
                rating=ratingBar.getRating();

                if (review.isEmpty()){
                    reviewET.setError("please review this item");
                }else if (rating<=0){
                    Toast.makeText(context, "Please rate this item", Toast.LENGTH_SHORT).show();
                }else {
                    sendCustomerReview(Integer.parseInt(uid), productId,review,rating);
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order History");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavBackBtnPress) context).onNavBackBtnPress();
            }
        });
    }

    private void sendCustomerReview(int cid, int pid, String review, double rating) {
        Call<ReviewPostResponse> call = RetrofitClient.getInstance().getApiInterface().postReview((int) rating, review, cid, pid);
        call.enqueue(new Callback<ReviewPostResponse>() {
            @Override
            public void onResponse(Call<ReviewPostResponse> call, Response<ReviewPostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(context, "your review have been saved", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    } else
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Log.e(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<ReviewPostResponse> call, Throwable t) {
                Log.e(TAG, "onResponse: " + t.getLocalizedMessage());
            }
        });
    }

}
