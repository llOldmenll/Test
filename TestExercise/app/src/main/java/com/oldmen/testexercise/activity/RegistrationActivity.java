package com.oldmen.testexercise.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oldmen.testexercise.R;
import com.oldmen.testexercise.utils.UserSessionUtils;
import com.oldmen.testexercise.api.ApiService;
import com.oldmen.testexercise.api.RetrofitClient;
import com.oldmen.testexercise.container.PostRetrofitBody;
import com.oldmen.testexercise.container.PostRetrofitResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private EditText confirmPassword;
    private TextView startSignIn;
    private TextView skip;
    private Button btnRegistrateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        login = (EditText) findViewById(R.id.registr_login);
        password = (EditText) findViewById(R.id.registr_password);
        confirmPassword = (EditText) findViewById(R.id.registr_confirm_password);
        startSignIn = (TextView) findViewById(R.id.registr_start_login);
        skip = (TextView) findViewById(R.id.registr_btn_skip);
        btnRegistrateUser = (Button) findViewById(R.id.registr_btn_registration);

        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                login.setError(null);
                String userLogin = login.getText().toString();
                userLogin = userLogin.trim();
                if (!isLoginValid(userLogin))
                    login.setError("too short!");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                password.setError(null);
                String userPassword = password.getText().toString();
                userPassword = userPassword.trim();
                if (!isPasswordValid(userPassword))
                    password.setError("too short!");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionUtils.saveSkipRegistrationMode(RegistrationActivity.this, UserSessionUtils.YES_KEY);
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                RegistrationActivity.this.finish();
                startActivity(intent);
            }});

        startSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                RegistrationActivity.this.finish();
                startActivity(intent);
            }
        });


        btnRegistrateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userLogin = login.getText().toString();
                String userPassword = password.getText().toString();
                String userConfirmPassword = confirmPassword.getText().toString();

                if(isRegInfoValid(userLogin, userPassword, userConfirmPassword)){

                    PostRetrofitBody body = new PostRetrofitBody();
                    body.setUsername(userLogin);
                    body.setPassword(userPassword);
                    registrationUser(body);

                } else
                    Toast.makeText(RegistrationActivity.this, "Incorrect registration data!", Toast.LENGTH_SHORT).show();

            }});
    }

    private boolean isRegInfoValid(String userLogin, String userPassword, String userConfirmPassword) {

        login.setError(null);
        password.setError(null);
        confirmPassword.setError(null);

        userLogin = userLogin.trim();
        userPassword = userPassword.trim();
        userConfirmPassword = userConfirmPassword.trim();

        if (isLoginValid(userLogin) && isPasswordValid(userPassword) && isConfirmPasswordValid(userPassword, userConfirmPassword)) {
            return true;
        } else {

            if (!isLoginValid(userLogin)){
                login.setError("too short!");
            }
            if (!isPasswordValid(userPassword)){
                password.setError("too short!");
                password.setText(null);
                confirmPassword.setText(null);
            }
            if (!isConfirmPasswordValid(userPassword, userConfirmPassword)){
                confirmPassword.setError("incorrect confirm");
                confirmPassword.setText(null);
                password.setText(null);
            }

            return false;
        }
    }

    private boolean isLoginValid(String login) {
        return (login != null && login.length() > 3);
    }

    private boolean isPasswordValid(String password) {
        return (password != null && password.length() > 3);
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword){
        return confirmPassword.equals(password);
    }

    private void registrationUser(PostRetrofitBody body){

        ApiService api = RetrofitClient.getApiService(this);
        Call<PostRetrofitResponse> registrCall = api.postUserRegistration(body);
        registrCall.enqueue(new Callback<PostRetrofitResponse>() {

            @Override
            public void onResponse(@NonNull Call<PostRetrofitResponse> call, @NonNull Response<PostRetrofitResponse> response) {

                PostRetrofitResponse retrofitResponse = response.body();

                if (retrofitResponse != null) {
                    boolean isRegistrated = retrofitResponse.isSuccess();

                    Log.i("info", String.valueOf(isRegistrated));

                    if(isRegistrated){

                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                RegistrationActivity.this.finish();
                                startActivity(intent);
                            }};
                        createAlertDialog(getString(R.string.registration_successful), listener);

                    }else{
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }};
                        createAlertDialog(getString(R.string.registration_failed), listener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostRetrofitResponse> call, @NonNull Throwable t) {
                Log.e("Error", "Something wrong with registration");
            }

        });
    }

    private void createAlertDialog(String message, DialogInterface.OnClickListener listener){
        AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
        alertDialog.show();
    }
}