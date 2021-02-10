package com.bd.esellers.product.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bd.esellers.R;
import com.bd.esellers.cart.cartModel.Cart;
import com.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.bd.esellers.interfaces.PaginationListener;
import com.bd.esellers.interfaces.SeeProductDetails;
import com.bd.esellers.mainApp.MainActivity;
import com.bd.esellers.product.productModel.Product;

import java.util.List;

import com.bd.esellers.utility.Constant;

public class ProductAdapter extends RecyclerView.Adapter implements PaginationListener {

    private String TAG = "ProductAdapter ";

    // Item Type
    private static final int PRODUCT_ITEM = 0;
    private static final int FOOTER_ITEM = 1;


    private List<Product> productList;
    private Context context;


    private boolean isLoadingAdded = false;


    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View customView;

        switch (viewType) {
            case PRODUCT_ITEM:
                customView = inflater.inflate(R.layout.single_product_grid, parent, false);
                return new ProductViewHolder(customView);
            case FOOTER_ITEM:
                customView = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                return new LoadingVH(customView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: " + position);

        switch (getItemViewType(position)) {

            case FOOTER_ITEM:

                break;

            case PRODUCT_ITEM:
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                final Product newProduct = productList.get(position);
                Log.e(TAG, "onBindViewHolder: " + newProduct.getId());

                productViewHolder.priceTV.setText(newProduct.getProductPrice());
                productViewHolder.productTV.setText(newProduct.getProductName());
                productViewHolder.ratingBar.setRating(Float.parseFloat(newProduct.getRating()));

                Glide.with(context)
                        .load(Constant.BASE_URL + newProduct.getProductImage())
                        .into(productViewHolder.imageView);

                productViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SeeProductDetails) context).onProductClick(newProduct);
                    }
                });

                productViewHolder.cartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cart cart = new Cart();
                        cart.setProductId(newProduct.getId());
                        cart.setProductName(newProduct.getProductName());
                        cart.setProductImg(newProduct.getProductImage());
                        cart.setProductQuantity(1);
                        cart.setProduct(newProduct);
                        cart.setSize("");
                        cart.setColor("");


                        cart.setProductPrice(Double.parseDouble(newProduct.getProductPrice()));
                        cart.setTotalCash(Double.parseDouble(newProduct.getProductPrice()));

                        if (!newProduct.isAddedToCart()) {
                            newProduct.setAddedToCart(true);
                            if (context instanceof MainActivity) {
                                ((AddorRemoveCallbacks) context).onAddProduct(cart);
                            }

                        } else {
                            Toast.makeText(context, "already added into cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + productList.size());
        return productList == null ? 0 : productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);
        return (position == productList.size() - 1 && isLoadingAdded) ? FOOTER_ITEM : PRODUCT_ITEM;
    }

    @Override
    public void add(Product product) {
        productList.add(product);

        notifyItemInserted(productList.size() - 1);
        Log.d(TAG, "add: " + productList.size());
    }

    @Override
    public void addAllItem(List<Product> products) {
        for (Product p : products)
            add(p);
    }

    @Override
    public void removeItem(Product product) {
        int position = productList.indexOf(product);
        if (position > -1) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void clearList() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            removeItem(getItem(0));
        }
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Product());
    }

    @Override
    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = productList.size() - 1;
        Product product = getItem(position);
        if (product != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout frameLayout;
        private TextView productTV, priceTV;
        private ImageView imageView, wishBtn;
        private ProgressBar progressBar;
        private Button cartBtn;
        private RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productTV = itemView.findViewById(R.id.productTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            imageView = itemView.findViewById(R.id.imageview);
            progressBar = itemView.findViewById(R.id.progress);
            cartBtn = itemView.findViewById(R.id.cartBtn);
            wishBtn = itemView.findViewById(R.id.wishBtn);
            ratingBar = itemView.findViewById(R.id.rating);
            frameLayout = itemView.findViewById(R.id.gridFrame);


            int currentOrientation = context.getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//                Log.v("TAG", "Landscape !!!");
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
//                Log.v("TAG", "Portrait !!!");
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

//            frameLayout.post(new Runnable()
//            {
//
//                @Override
//                public void run()
//                {
//                    Log.d(TAG, "ProductViewHolder W: "+frameLayout.getWidth()+" H: "+frameLayout.getHeight());
//                    Log.d(TAG, "ProductViewHolder img W: "+imageView.getWidth()+" H: "+imageView.getHeight());
//
//                }
//            });

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}
