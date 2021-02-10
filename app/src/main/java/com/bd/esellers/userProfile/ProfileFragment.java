package com.bd.esellers.userProfile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bd.esellers.R;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.userAuth.SessionManager;
import com.bd.esellers.userProfile.userModel.ProfileUpdateRes;
import com.bd.esellers.userProfile.userModel.Users;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.bd.esellers.userAuth.SessionManager.ADDRESS;
import static com.bd.esellers.userAuth.SessionManager.EMAIL;
import static com.bd.esellers.userAuth.SessionManager.MOBILE;
import static com.bd.esellers.userAuth.SessionManager.PHOTO;
import static com.bd.esellers.userAuth.SessionManager.PREF_NAME;
import static com.bd.esellers.userAuth.SessionManager.STATUS;
import static com.bd.esellers.userAuth.SessionManager.USER_NAME;
import static com.bd.esellers.utility.Utils.getBitmapImage;
import static com.bd.esellers.utility.Utils.getStringImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Toolbar toolbar;
    private static final int PHOTO_REQUEST_CODE = 1;
    private EditText nameET, emailET, contactET, addressET;
    private String token, name, email, contact, stringPhoto, address, id;
    private Button choseBtn, editBtn;
    private CircleImageView profileImg;
    private SharedPreferences prefs;
    private SessionManager sessionManager;
    private Context context;
    private String TAG = "ProfileFragment ";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        contactET = view.findViewById(R.id.contactET);
        addressET = view.findViewById(R.id.addressET);
        choseBtn = view.findViewById(R.id.choseBtn);
        editBtn = view.findViewById(R.id.editBtn);
        profileImg = view.findViewById(R.id.profileIV);
        toolbar = view.findViewById(R.id.toolbar);

        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        token = prefs.getString("TOKEN", "No TOKEN defined");
        id = prefs.getString("ID", "No ID defined");

        emailET.setText(prefs.getString(EMAIL, "No name defined"));
        emailET.setEnabled(false);

        if (prefs.getBoolean(STATUS, false)) {
            nameET.setText(prefs.getString(USER_NAME, "no name"));
            addressET.setText(prefs.getString(ADDRESS, "no address define"));
            contactET.setText(prefs.getString(MOBILE, "no contact define"));
            profileImg.setImageBitmap(getBitmapImage(prefs.getString(PHOTO, "no photo")));
        }




        toolbar.setTitle("User Profile");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        choseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), PHOTO_REQUEST_CODE);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                contact = contactET.getText().toString();
                address = addressET.getText().toString();

                if (name.isEmpty()) {
                    nameET.setError("Please enter name");
                } else if (contact.isEmpty()) {
                    contactET.setError("Please enter valid mobile number");
                }else if (contact.length()<11){
                    contactET.setError("mobile number must be 11 characters");
                }else if (address.isEmpty()){
                    addressET.setError("Please enter Address");
                }else if (stringPhoto==null){
                    Toast.makeText(context, "Please select photo", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(name, email, contact, stringPhoto, address);
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d(TAG, "onActivityResult: called");
            Uri filePath = data.getData();
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop() // or centerCrop
                    .override(195, 195);

            Glide
                    .with(this)
                    .asBitmap()
                    .apply(myOptions)
                    .load(filePath)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            stringPhoto = (getStringImage(resource));
                            profileImg.setImageBitmap(resource);
                            Log.d(TAG, "onResourceReady: called");
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }



    private void updateProfile(String name, String email, String contact, final String stringPhoto, String address) {

//        Log.e(TAG, "updateProfile: "+name+" "+email+" "+contact+" "+stringPhoto.length()+" "+address );
        Call<ProfileUpdateRes> call = RetrofitClient.getInstance(token).getApiInterface().updateProfile(name, email, contact, stringPhoto, address, Integer.parseInt(id));
        call.enqueue(new Callback<ProfileUpdateRes>() {
            @Override
            public void onResponse(Call<ProfileUpdateRes> call, Response<ProfileUpdateRes> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        sessionManager=new SessionManager(context);
                        Users users = response.body().getUser();
                        users.setProfileStringImg(stringPhoto);
                        users.setToken(token);
                        users.setProfileComplete(true);
                        sessionManager.createSession(users);
//                        finish();
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onResponse1: " + users.getAddress());
                        getFragmentManager().popBackStack();
                    }
                } else
                    Log.e(TAG, "onResponse2: " + response.code());
            }

            @Override
            public void onFailure(Call<ProfileUpdateRes> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(context, "Server busy !!! Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Log.d(TAG, "onStop: ");
        super.onStop();
    }
}
