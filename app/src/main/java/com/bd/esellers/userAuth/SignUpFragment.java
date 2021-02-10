package com.bd.esellers.userAuth;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bd.esellers.R;
import com.bd.esellers.interfaces.AuthCompleteListener;
import com.bd.esellers.interfaces.NavBackBtnPress;
import com.bd.esellers.networking.RetrofitClient;
import com.bd.esellers.userAuth.userAuthModels.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment ";
    private EditText  emailET,  passwordET, confirmET;
    private String  email, password, confpass;
    private ProgressBar progressBar;
    private Button regBtn,closeBtn;
    private TextView logonTV;
    private BackToSignIn backToSignin;
    private Context context;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        backToSignin = (BackToSignIn) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailET = view.findViewById(R.id.emailET);
        confirmET = view.findViewById(R.id.confirmPassET);
        passwordET = view.findViewById(R.id.passwordET);
        regBtn = view.findViewById(R.id.signupBtn);
        logonTV = view.findViewById(R.id.loginTV);
        progressBar = view.findViewById(R.id.progress_signup);
        closeBtn = view.findViewById(R.id.closeBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                confpass = confirmET.getText().toString();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailET.setError("Please enter valid email");
                } else if (password.isEmpty()) {
                    passwordET.setError("Please enter password");
                }else if (!password.equals(confpass)){
                    confirmET.setError("password not matched");
                } else {
                    doSignup(email, password);
                }

            }
        });

        logonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSignin.onBackToSignIn();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavBackBtnPress) context).onNavBackBtnPress();
            }
        });

        return view;
    }

    private void doSignup(String email, String password) {

        Call<AuthResponse> call = RetrofitClient.getInstance().getApiInterface().createUser(email, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null && authResponse.getStatus() == 1) {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                        ((AuthCompleteListener) context).onAuthComplete(authResponse.getToken());
//                        signupComplete.onSignUpComplete(authResponse.getToken());
                        Log.d(TAG, "onResponse1: " + response.code());
                    } else {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else
                    Log.d(TAG, "onResponse2: " + response.code());//this is for server side error, wrong path, wrong file name or missing
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(context, "Server busy !!! Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface BackToSignIn {
        void onBackToSignIn();
    }

}
