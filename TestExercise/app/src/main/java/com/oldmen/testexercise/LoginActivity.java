package com.oldmen.testexercise;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private TextView createNewAccount;
    private TextView skip;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.input_login);
        password = (EditText) findViewById(R.id.input_password);
        createNewAccount = (TextView) findViewById(R.id.start_registration);
        skip = (TextView) findViewById(R.id.btn_skip);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

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
                UserSessionUtils.saveSkipRegistrationMode(LoginActivity.this, UserSessionUtils.YES_KEY);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userLogin = login.getText().toString();
                String userPassword = password.getText().toString();

                if (isLoginInfoValid(userLogin, userPassword)) {

                    PostRetrofitBody body = new PostRetrofitBody();
                    body.setUsername(userLogin);
                    body.setPassword(userPassword);
                    signInUser(body);

                } else
                    Toast.makeText(LoginActivity.this, "Incorrect login data!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isLoginInfoValid(String userLogin, String userPassword) {

        login.setError(null);
        password.setError(null);

        userLogin = userLogin.trim();
        userPassword = userPassword.trim();

        if (isLoginValid(userLogin) && isPasswordValid(userPassword)) {
            return true;
        } else {

            if (!isLoginValid(userLogin)) {
                login.setError("too short!");
            }
            if (!isPasswordValid(userPassword)) {
                password.setError("too short!");
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

    private void signInUser(final PostRetrofitBody body) {

        ApiService api = RetrofitClient.getApiService(this);
        Call<PostRetrofitResponse> signInCall = api.postUserLogin(body);
        signInCall.enqueue(new Callback<PostRetrofitResponse>() {

            @Override
            public void onResponse(@NonNull Call<PostRetrofitResponse> call, @NonNull Response<PostRetrofitResponse> response) {

                PostRetrofitResponse retrofitResponse = response.body();

                if (retrofitResponse != null && retrofitResponse.getToken() != null && !retrofitResponse.getToken().equals("")) {

                    boolean isSignInSuccessed = retrofitResponse.isSuccess();

                    Log.i("info", String.valueOf(isSignInSuccessed));
                    Log.i("info", retrofitResponse.getToken());

                    if (isSignInSuccessed) {
                        UserContainer user = new UserContainer();
                        user.setUserSkipRegistrMode(UserSessionUtils.YES_KEY);
                        user.setUserLogin(body.getUsername());
                        user.setUserToken(retrofitResponse.getToken());

                        UserSessionUtils.saveSession(LoginActivity.this, user);

                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        };
                        createAlertDialog(getString(R.string.sign_in_successful), listener);

                    } else {
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };
                        createAlertDialog(getString(R.string.sign_in_failed), listener);
                        login.setText(null);
                        password.setText(null);
                    }
                } else {
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    };
                    createAlertDialog(getString(R.string.sign_in_failed), listener);
                    login.setText(null);
                    password.setText(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostRetrofitResponse> call, @NonNull Throwable t) {
                Log.e("Error", "Something wrong with registration");
            }

        });

    }

    private void createAlertDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
        alertDialog.show();
    }
}
