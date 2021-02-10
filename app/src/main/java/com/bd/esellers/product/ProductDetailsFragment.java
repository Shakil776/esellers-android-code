package com.bd.esellers.product;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.bd.esellers.R;
import com.bd.esellers.cart.cartModel.Cart;
import com.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.bd.esellers.interfaces.ShowHideIconListener;
import com.bd.esellers.mainApp.MainActivity;
import com.bd.esellers.product.productModel.Product;

import com.bd.esellers.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {

    private TextView productTV, priceTV, shortDecTV, longDescTV, vendorTV, manufacTV;
    private ImageView productIV, brandIV;
    private Button cartBtn, buyBtn;
    private RatingBar ratingBar;
    private Context context;
    private Toolbar toolbar;
    private Product product;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product= (Product) bundle.getSerializable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_details, container, false);

        ((ShowHideIconListener) getActivity()).showBackIcon();

        productTV=view.findViewById(R.id.nameTV);
        priceTV=view.findViewById(R.id.priceTV);
        shortDecTV=view.findViewById(R.id.desc1ET);
        longDescTV=view.findViewById(R.id.desc2ET);
        vendorTV=view.findViewById(R.id.vendorTV);
        manufacTV=view.findViewById(R.id.menufacTV);
        productIV=view.findViewById(R.id.productIV);
        brandIV=view.findViewById(R.id.brandIV);
        cartBtn=view.findViewById(R.id.cartBtn);
        buyBtn=view.findViewById(R.id.buyBtn);
        ratingBar=view.findViewById(R.id.rating);

        toolbar = ((MainActivity) getActivity()).findViewById(R.id.myToolbar);
        toolbar.setTitle("Product Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        shortDecTV.setText(product.getShortDescription());
        longDescTV.setText(product.getLongDescription());
//        vendorTV.setText(product.getVendorName());
        manufacTV.setText(product.getManufacturerName());
        productTV.setText(product.getProductName());
        priceTV.setText(product.getProductPrice());
        Glide.with(this).load(Constant.BASE_URL+product.getProductImage()).into(productIV);

        productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullView(product);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart();
                cart.setProductId(product.getId());
                cart.setProductName(product.getProductName());
                cart.setProductImg(product.getProductImage());
                cart.setProduct(product);
                cart.setProductQuantity(1);
                cart.setSize("");
                cart.setColor("");
                cart.setProductPrice(Double.parseDouble(product.getProductPrice()));
                cart.setTotalCash(Double.parseDouble(product.getProductPrice()));

                if (!product.isAddedToCart()) {
                    product.setAddedToCart(true);
                    ((AddorRemoveCallbacks)context).onAddProduct(cart);

                } else {
                    Toast.makeText(context, "already added into cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



    private void fullView(Product product) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View fullView= inflater.inflate(R.layout.fullscreeen,null,false);
        final Dialog fullScreenDilog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        fullScreenDilog.setContentView(fullView);

        Button closeBtn=fullScreenDilog.findViewById(R.id.btnClose);
        PhotoView fullScreenView=fullScreenDilog.findViewById(R.id.fullView);
        Glide.with(this).load(Constant.BASE_URL+product.getProductImage()).into(fullScreenView);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDilog.dismiss();
            }
        });

        fullScreenDilog.show();
    }

    @Override
    public void onDetach() {
        ((ShowHideIconListener) getActivity()).showHamburgerIcon();
        super.onDetach();
    }
}
