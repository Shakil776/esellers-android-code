package com.bd.esellers.cart.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bd.esellers.R;
import com.bd.esellers.cart.cartModel.Cart;
import com.bd.esellers.interfaces.AddorRemoveCallbacks;

import java.util.List;

import static com.bd.esellers.mainApp.MainActivity.grandTotalPlus;


import static com.bd.esellers.cart.CartListFragment.totalTV;
import static com.bd.esellers.utility.Constant.BASE_URL;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private static final String TAG = "CartAdapter ";
    private List<Cart> cartList;
    private Context context;


    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_row, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);
        holder.nameTV.setText(cart.getProductName());
        holder.priceTV.setText(String.valueOf(cart.getProductPrice()));
        holder.quantityTV.setText(String.valueOf(cart.getProductQuantity()));
        Glide.with(context)
                .load(BASE_URL + cart.getProductImg()).into(holder.productImg);

        holder.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grandTotalPlus = 0;
                holder.decBtn.setEnabled(true);

                int cartUpdateCounter = (cart.getProductQuantity());
                Log.d("counterthegun", String.valueOf(cart.getProductQuantity()));

                holder.incBtn.setEnabled(true);
                cartUpdateCounter += 1;

                cart.setProductQuantity((cartUpdateCounter));
                double cash = (cart.getProductPrice()) * (cart.getProductQuantity());

                holder.quantityTV.setText(String.valueOf(cart.getProductQuantity()));

                cart.setTotalCash(cash);

                for (int i = 0; i < cartList.size(); i++) {
                    grandTotalPlus = grandTotalPlus + cartList.get(i).getTotalCash();
                }
                Log.d("totalcashthegun", String.valueOf(grandTotalPlus));
                totalTV.setText(String.valueOf(grandTotalPlus));
            }
        });

        holder.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grandTotalPlus = 0;
                holder.incBtn.setEnabled(true);

                int cartUpdateCounter = (cart.getProductQuantity());
                Log.d("counterthegun", String.valueOf(cart.getProductQuantity()));


                if (cartUpdateCounter == 1) {
                    holder.decBtn.setEnabled(false);
                    Toast.makeText(context, "quantity can't be zero", Toast.LENGTH_SHORT).show();
                } else {
                    holder.decBtn.setEnabled(true);
                    cartUpdateCounter -= 1;
                    cart.setProductQuantity((cartUpdateCounter));
                    holder.quantityTV.setText(String.valueOf(cart.getProductQuantity()));
                    double cash = (cart.getProductPrice()) * (cart.getProductQuantity());

                    cart.setTotalCash(cash);
//                    holder.productCartPrice.setText(String.valueOf(cash));
                    for (int i = 0; i < cartList.size(); i++) {
                        grandTotalPlus = grandTotalPlus + cartList.get(i).getTotalCash();
                    }

                    Log.d("totalcashthegun", String.valueOf(grandTotalPlus));
                    totalTV.setText(String.valueOf(grandTotalPlus));

                }

            }
        });
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartList.get(position).getProduct().setAddedToCart(false);
                if (cartList.size() == 1) {
//                    cartList.remove(position);
                    cartList.clear();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartList.size());
                    grandTotalPlus = 0;
                    totalTV.setText(String.valueOf(grandTotalPlus));
                    Log.e(TAG, "onClick: 1st if clicked" );
                    ((AddorRemoveCallbacks)context).onRemoveProduct(cart);
//                    MainActivity.cart_count = cartList.size();
                }

                if (cartList.size() > 0) {
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartList.size());
                    grandTotalPlus = 0;
                    for (int i = 0; i < cartList.size(); i++) {
                        grandTotalPlus = grandTotalPlus + cartList.get(i).getTotalCash();
                    }

                    Log.d("totalcashthegun", String.valueOf(grandTotalPlus));
                    totalTV.setText(String.valueOf(grandTotalPlus));

                    Log.e(TAG, "onClick: 2nd if clicked" );
//                    MainActivity.cart_count = cartList.size();
                    ((AddorRemoveCallbacks)context).onRemoveProduct(cart);

                } else {
                    Toast.makeText(context, "no item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + cartList.size());
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, priceTV, quantityTV;
        private Button incBtn, decBtn;
        private ImageView productImg, deleteIV;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.titleTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            incBtn = itemView.findViewById(R.id.incrementBtn);
            decBtn = itemView.findViewById(R.id.decrementBtn);
            productImg = itemView.findViewById(R.id.productImg);
            deleteIV = itemView.findViewById(R.id.deleteIV);

        }
    }

    public void updateList(List<Cart> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

}
