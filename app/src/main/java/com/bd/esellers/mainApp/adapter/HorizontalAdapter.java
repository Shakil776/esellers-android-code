package com.bd.esellers.mainApp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bd.esellers.R;
import com.bd.esellers.cart.cartModel.Cart;
import com.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.bd.esellers.interfaces.SeeProductDetails;
import com.bd.esellers.mainApp.MainActivity;
import com.bd.esellers.mainApp.dataModel.HorizontalModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import com.bd.esellers.utility.Constant;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    private static final String TAG = "HorizontalAdapter ";
    private Context context;
    private ArrayList<HorizontalModel> hmList;
    private File imgFilePath;

    public HorizontalAdapter(Context context, ArrayList<HorizontalModel> hmList) {
        this.context = context;
        this.hmList = hmList;
        imgFilePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_product, viewGroup, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HorizontalViewHolder horizontalViewHolder, final int i) {

        final HorizontalModel horizontalModel = hmList.get(i);
        final String id = String.valueOf(horizontalModel.getProduct().getId());
        File dir = new File(imgFilePath + "/thumb/");
        final File thumbImageFile = new File(dir, id + ".jpg");

//        Log.e("url", "onBindViewHolder: "+BASE_URL+horizontalModel.getProduct().getProductImage() );


        if (!dir.exists()) {
            dir.mkdir();
        }

        if (!thumbImageFile.exists()) {
            Glide.with(context)
                    .asBitmap()
                    .load(Constant.BASE_URL + horizontalModel.getProduct().getProductImage())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            horizontalViewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            horizontalViewHolder.progressBar.setVisibility(View.GONE);
                            try {
                                FileOutputStream outputStreamthumb = new FileOutputStream(thumbImageFile);
//                        Bitmap thumbBitmap = Bitmap.createScaledBitmap(loadedImage,300,400,false);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStreamthumb);
//                                Toast.makeText(context, id + " thumb saved", Toast.LENGTH_SHORT).show();
                                Log.d("file", ": " + id + " saved");
                                outputStreamthumb.flush();
                                outputStreamthumb.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }).into(horizontalViewHolder.imageView);
        } else {
            Log.e("else", "onBindViewHolder: " + imgFilePath + "/thumb/");
            Log.e("else", "onBindViewHolder: " + dir.toString());
            Bitmap bitmap = BitmapFactory.decodeFile(dir.getPath() + "/" + id + ".jpg");
            horizontalViewHolder.progressBar.setVisibility(View.GONE);
            horizontalViewHolder.imageView.setImageBitmap(bitmap);
        }

        horizontalViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SeeProductDetails) context).onProductClick(horizontalModel.getProduct());
//                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        horizontalViewHolder.productTV.setText(horizontalModel.getProduct().getProductName());
        horizontalViewHolder.priceTV.setText(horizontalModel.getProduct().getProductPrice());
        horizontalViewHolder.ratingBar.setRating(Float.parseFloat(horizontalModel.getProduct().getRating()));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha);
        horizontalViewHolder.imageView.startAnimation(animation);


        horizontalViewHolder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart();
                cart.setProductId(horizontalModel.getProduct().getId());
                cart.setProductName(horizontalModel.getProduct().getProductName());
                cart.setProductImg(horizontalModel.getProduct().getProductImage());
                cart.setProductQuantity(1);
                cart.setProduct(horizontalModel.getProduct());
                cart.setSize("");
                cart.setColor("");
//
//                if (horizontalModel.getProduct().getAttributes()==null){
//                    cart.setSize("");
//                    cart.setColor("");
//                }else {
//                    if (horizontalModel.getProduct().getAttributes().get(0).getSize() != null)
//                        cart.setSize(horizontalModel.getProduct().getAttributes().get(0).getSize());
//                    else
//                        cart.setSize("");
//                    if (horizontalModel.getProduct().getAttributes().get(0).getColor() != null)
//                        cart.setColor(horizontalModel.getProduct().getAttributes().get(0).getColor());
//                    else
//                        cart.setColor("");
//                }
                cart.setProductPrice(Double.parseDouble(horizontalModel.getProduct().getProductPrice()));
                cart.setTotalCash(Double.parseDouble(horizontalModel.getProduct().getProductPrice()));

                if (!horizontalModel.getProduct().isAddedToCart()) {
                    horizontalModel.getProduct().setAddedToCart(true);
                    if (context instanceof MainActivity) {
                        ((AddorRemoveCallbacks) context).onAddProduct(cart);
                    }

                } else {
                    Toast.makeText(context, "already added into cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + hmList.size());
        return hmList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private TextView productTV, priceTV;
        private ImageView imageView, wishBtn;
        private ProgressBar progressBar;
        private Button cartBtn;
        private RatingBar ratingBar;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            productTV = itemView.findViewById(R.id.productTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            imageView = itemView.findViewById(R.id.imageview);
            progressBar = itemView.findViewById(R.id.progress);
            cartBtn = itemView.findViewById(R.id.cartBtn);
            wishBtn = itemView.findViewById(R.id.wishBtn);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }

}
