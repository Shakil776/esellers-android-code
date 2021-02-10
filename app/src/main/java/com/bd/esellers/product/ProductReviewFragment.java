package com.bd.esellers.product;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.esellers.R;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.order.reviewModel.ReviewGetResponse;
import com.bd.esellers.product.productModel.Product;
import com.bd.esellers.product.productModel.Review;
import com.bd.esellers.utility.DialogClass;
import com.bd.esellers.utility.PaginationWithHideShowScrollListener;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductReviewFragment extends Fragment {

    private static final String TAG = "ProductReviewFragment";
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private List<Review> reviewList;
    private ReviewAdapter adapter;
    private int pid;
    private DialogClass dialogClass;

    private final int startPage = 1;
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPages;
    private int currentPage;

    public ProductReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        isLastPage = false;
        isLoading = false;
        currentPage = startPage;
        totalPages = 0;
        dialogClass=new DialogClass(context);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Product product= (Product) bundle.getSerializable("product");
            pid=product.getId();
            getReview(pid);
            dialogClass.showProgressDialog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_review, container, false);

        RecyclerView recyclerView=view.findViewById(R.id.reviewRV);
        linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        reviewList=new ArrayList<>();
        adapter=new ReviewAdapter(reviewList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new PaginationWithHideShowScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.e(TAG, "loadMoreItems: called");
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNxtReview(pid);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void onHide() {
//                hideViews();
            }

            @Override
            public void onShow() {
//                showViews();
            }
        });



        return view;
    }

    private void getReview(int pid){
        Call<ReviewGetResponse> call= RetrofitClient.getInstance().getApiInterface().getAllReviews(pid,currentPage);
        call.enqueue(new Callback<ReviewGetResponse>() {
            @Override
            public void onResponse(Call<ReviewGetResponse> call, Response<ReviewGetResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        reviewList=response.body().getReviews();
                        dialogClass.closeProgressDialog();
                        totalPages = response.body().getTotalPages();
                        Log.e(TAG, "onResponse: " + reviewList.size() + "tp: " + totalPages);
                        adapter.addAllItem(reviewList);

                        if (currentPage < totalPages)
                            adapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }else {
                        Log.e(TAG, "onResponse: "+response.body().getMessage() );
                        dialogClass.closeProgressDialog();
                    }
                }else {
                    Log.e(TAG, "onResponse: "+response.code() );
                    dialogClass.closeProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ReviewGetResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                dialogClass.closeProgressDialog();
            }
        });
    }

    private void getNxtReview(int pid){
        Call<ReviewGetResponse> call= RetrofitClient.getInstance().getApiInterface().getAllReviews(pid,currentPage);
        call.enqueue(new Callback<ReviewGetResponse>() {
            @Override
            public void onResponse(Call<ReviewGetResponse> call, Response<ReviewGetResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){

                        adapter.removeLoadingFooter();
                        isLoading = false;

                        reviewList=response.body().getReviews();
                        dialogClass.closeProgressDialog();
                        totalPages = response.body().getTotalPages();
                        Log.e(TAG, "onResponse: " + reviewList.size() + "tp: " + totalPages);
                        adapter.addAllItem(reviewList);

                        if (currentPage != totalPages)
                            adapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }else {
                        Log.e(TAG, "onResponse: "+response.body().getMessage() );
//                        dialogClass.closeProgressDialog();
                    }
                }else {
                    Log.e(TAG, "onResponse: "+response.code() );
//                    dialogClass.closeProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ReviewGetResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
//                dialogClass.closeProgressDialog();
            }
        });
    }


    public class ReviewAdapter extends RecyclerView.Adapter{

        private static final String TAG = "ReviewAdapter";
        private List<Review>reviewList;

        private static final int PRODUCT_REVIEW = 0;
        private static final int LOADING = 1;
        private boolean isLoadingAdded = false;

        public ReviewAdapter(List<Review> reviewList) {
            this.reviewList = reviewList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View customView;

            switch (viewType) {
                case PRODUCT_REVIEW:
                    customView = inflater.inflate(R.layout.single_review_row, parent, false);
                    return new ReviewViewHolder(customView);
                case LOADING:
                    customView = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                    return new LoadingVH(customView);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case LOADING:
                    break;
                case PRODUCT_REVIEW:
                    ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                    final Review review = reviewList.get(position);
                    Log.e(TAG, "onBindViewHolder: " + review.getId());

                    reviewViewHolder.idTV.setText("Customer-"+review.getCustomerId());
                    reviewViewHolder.dateTV.setText(review.getCreatedAt());
                    reviewViewHolder.reviewTV.setText(review.getReview());
                    reviewViewHolder.ratingBar.setRating(Float.parseFloat(review.getRating()));
            }
        }

        @Override
        public int getItemCount() {
            Log.e(TAG, "getItemCount: " + reviewList.size());
            return reviewList == null ? 0 : reviewList.size();
        }

        @Override
        public int getItemViewType(int position) {
            Log.d(TAG, "getItemViewType: " + position);
            return (position == reviewList.size() - 1 && isLoadingAdded) ? LOADING : PRODUCT_REVIEW;
        }

        void add(Review review){
            reviewList.add(review);
            notifyItemInserted(reviewList.size() - 1);
            Log.d(TAG, "add: " + reviewList.size());
        }

        void addAllItem(List<Review> reviews){
            for (Review p : reviews)
                add(p);
        }

        void removeItem(Review review){
            int position = reviewList.indexOf(review);
            if (position > -1) {
                reviewList.remove(position);
                notifyItemRemoved(position);
            }
        }

        void clearList(){
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                removeItem(getItem(0));
            }
        }

        boolean isEmpty(){
            return getItemCount() == 0;
        }

        void addLoadingFooter(){
            isLoadingAdded = true;
            add(new Review());
        }

        void removeLoadingFooter(){
            isLoadingAdded = false;
            int position = reviewList.size() - 1;
            Review product = getItem(position);
            if (product != null) {
                reviewList.remove(position);
                notifyItemRemoved(position);
            }
        }

        Review getItem(int position){
            return reviewList.get(position);
        }


        public class ReviewViewHolder extends RecyclerView.ViewHolder {
            private TextView idTV,dateTV, reviewTV;
            private RatingBar ratingBar;


            public ReviewViewHolder(@NonNull View itemView) {
                super(itemView);
                idTV = itemView.findViewById(R.id.customerIdTV);
                dateTV = itemView.findViewById(R.id.dateTV);
                reviewTV = itemView.findViewById(R.id.reviewTV);
                ratingBar = itemView.findViewById(R.id.rating);

            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder {

            public LoadingVH(View itemView) {
                super(itemView);
            }
        }

    }

}
