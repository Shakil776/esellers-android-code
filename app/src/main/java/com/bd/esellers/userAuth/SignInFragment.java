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
public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment ";
    private EditText emailET, passwordET;
    private String email, password;
    private Button loginBtn, closeBtn;
    private ProgressBar progressBar;
    private TextView regTV;
    private SignInFrgmntAction action;
    private Context context;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        action= (SignInFrgmntAction) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        loginBtn = view.findViewById(R.id.signinBtn);
        closeBtn = view.findViewById(R.id.closeBtn);
        regTV = view.findViewById(R.id.regTV);
        progressBar = view.findViewById(R.id.progress_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailET.setError("Please enter valid email");
                } else if (password.isEmpty() || password.length()<8) {
                    passwordET.setError("Please enter password");
                }else {
                    doSignIn(email, password);
                }
            }
        });

        regTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.onBackToSignUp();
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


    private void doSignIn(String email, String password) {

        Call<AuthResponse> call = RetrofitClient.getInstance().getApiInterface().userSignin(email, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null && authResponse.getStatus() == 1) {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                        ((AuthCompleteListener) context).onAuthComplete(authResponse.getToken());
//                        action.onLoginComplete(authResponse.getToken());
                        Log.d("signup", "onResponse: " + response.code());
                    }else {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("signup", "server onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(context, "Server busy !!! Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface SignInFrgmntAction {
        void onBackToSignUp();
    }

}
