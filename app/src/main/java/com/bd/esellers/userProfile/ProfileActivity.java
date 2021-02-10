package com.bd.esellers.userProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bd.esellers.R;

import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.userAuth.SessionManager;
import com.bd.esellers.userProfile.userModel.ProfileUpdateRes;
import com.bd.esellers.userProfile.userModel.Users;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bd.esellers.userAuth.SessionManager.ADDRESS;
import static com.bd.esellers.userAuth.SessionManager.EMAIL;
import static com.bd.esellers.userAuth.SessionManager.MOBILE;
import static com.bd.esellers.userAuth.SessionManager.PHOTO;
import static com.bd.esellers.userAuth.SessionManager.PREF_NAME;
import static com.bd.esellers.userAuth.SessionManager.STATUS;
import static com.bd.esellers.userAuth.SessionManager.USER_NAME;
import static com.bd.esellers.utility.Utils.getBitmapImage;
import static com.bd.esellers.utility.Utils.getStringImage;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int PHOTO_REQUEST_CODE = 1;
    private EditText nameET, emailET, contactET, addressET;
    private String token, name, email, contact, stringPhoto, address, id;
    private Button choseBtn, editBtn;
    private CircleImageView profileImg;
    private SharedPreferences prefs;
    private SessionManager sessionManager;
    private String TAG = "ProfileActivity ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        contactET = findViewById(R.id.contactET);
        addressET = findViewById(R.id.addressET);
        choseBtn = findViewById(R.id.choseBtn);
        editBtn = findViewById(R.id.editBtn);
        profileImg = findViewById(R.id.profileIV);

        prefs = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
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

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("User Profile");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    Toast.makeText(ProfileActivity.this, "Please select photo", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(name, email, contact, stringPhoto, address);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        sessionManager=new SessionManager(ProfileActivity.this);
                        Users users = response.body().getUser();
                        users.setProfileStringImg(stringPhoto);
                        users.setToken(token);
                        users.setProfileComplete(true);
                        sessionManager.createSession(users);
                        finish();
                        Log.e(TAG, "onResponse1: " + users.getAddress());
                    }
                } else
                    Log.e(TAG, "onResponse2: " + response.code());
            }

            @Override
            public void onFailure(Call<ProfileUpdateRes> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(ProfileActivity.this, "Server busy !!! Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
