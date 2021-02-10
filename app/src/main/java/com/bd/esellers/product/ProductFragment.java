
package com.bd.esellers.product;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.esellers.R;
import com.bd.esellers.category.categoryModel.Category;
import com.bd.esellers.database.DatabaseQuery;
import com.bd.esellers.interfaces.CategoryListener;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.product.adapter.ProductAdapter;
import com.bd.esellers.product.productModel.Product;
import com.bd.esellers.product.productModel.ProductResponse;
import com.bd.esellers.utility.PaginationWithHideShowScrollListener;


import java.util.ArrayList;
import java.util.List;

import com.bd.esellers.utility.ImageHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment ";
    private RecyclerView recyclerView;
    private Context context;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private TextView productTypeTV;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout catLL, catImgLayout, titleLL;
    private String catName;
//    private Category category;
    private int cid;

    public static final int PAGE_START = 1;
    public static boolean isLoading;
    public static boolean isLastPage;
    public static int TOTAL_PAGES;
    public static int currentPage;

//    private LinearLayoutManager linearLayoutManager;

    public ProductFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        isLastPage = false;
        isLoading = false;
        currentPage = PAGE_START;
        TOTAL_PAGES = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
//            cid = bundle.getInt("catId");
            catName = bundle.getString("categoryName");
            cid = bundle.getInt("categoryId");
        }
        Log.e(TAG, "onAttach: id:" + cid + "cp: " + currentPage + "tp: " + TOTAL_PAGES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.productRV);
        catLL = view.findViewById(R.id.catLayout);
        titleLL = view.findViewById(R.id.titleLL);
        catImgLayout = view.findViewById(R.id.imgLayout);
        productTypeTV = view.findViewById(R.id.productTypeTV);
        recyclerView.setHasFixedSize(true);
        productTypeTV.setText(catName);
        setCategory();
//        linearLayoutManager= new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(context, 2);

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return position == 0 ? 2 : 1;
//            }
//        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, context);
//        productAdapter.addCategory();
        recyclerView.setAdapter(productAdapter);

        recyclerView.addOnScrollListener(new PaginationWithHideShowScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.e(TAG, "loadMoreItems: called");
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage(cid);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
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
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        loadFirstPage(cid);

        return view;
    }

    private void loadFirstPage(int cid) {
        Log.d(TAG, "loadFirstPage: " + currentPage);
        Call<ProductResponse> call = RetrofitClient.getInstance().getApiInterface().getProductsByCat(cid, currentPage);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        productList = response.body().getProducts();
                        TOTAL_PAGES = response.body().getTotalPages();
                        Log.e(TAG, "onResponse: " + productList.size() + "tp: " + TOTAL_PAGES);


                        productAdapter.addAllItem(productList);


                        if (currentPage < TOTAL_PAGES)
                            productAdapter.addLoadingFooter();
                        else
                            isLastPage = true;

                    }
                } else
                    Log.e(TAG, "onResponse else: " + response.code());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }


    private void loadNextPage(int cid) {
        Log.d(TAG, "loadNextPage: cp: " + currentPage);
        Call<ProductResponse> call = RetrofitClient.getInstance().getApiInterface().getProductsByCat(cid, currentPage);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        productAdapter.removeLoadingFooter();
                        isLoading = false;

                        productList = response.body().getProducts();
                        TOTAL_PAGES = response.body().getTotalPages();
                        Log.e(TAG, "loadNextPage onResponse: " + productList.size());


                        productAdapter.addAllItem(productList);


                        if (currentPage != TOTAL_PAGES)
                            productAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "loadNextPage onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void setCategory() {
        DatabaseQuery query = new DatabaseQuery(context);
        List<Category> categories = query.getCategory();
        for (Category c : categories) {
            setCatIcon(c);
        }
    }

    public void setCatIcon(final Category catIcon) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cat, null, false);
        ImageView imageView = view.findViewById(R.id.catImg);
        TextView textView = view.findViewById(R.id.catTxt);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setMaxLines(2);
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        textView.setText(catIcon.getCategoryName());
        Log.e(TAG, "setCatIcon: " + catIcon.getCategoryName());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parms.setMargins(20, 0, 20, 0);
        view.setLayoutParams(parms);

        imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(catIcon.getBitmap(), 120));
        catImgLayout.addView(view);
        imageView.setId(catIcon.getId());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CategoryListener) context).onCatIconClick(catIcon);
            }
        });
    }

    private void hideViews() {
        catLL.animate()
                .translationY(-catLL.getHeight())
                .alpha(1.0f)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        catLL.setVisibility(View.GONE);
                    }
                });
        titleLL.animate().translationY(-titleLL.getHeight())
                .alpha(1.0f)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        titleLL.setVisibility(View.GONE);
                    }
                });

    }

    private void showViews() {
        catLL.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        catLL.setVisibility(View.VISIBLE);
                    }
                });

        titleLL.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        titleLL.setVisibility(View.VISIBLE);
                    }
                });
    }

}
