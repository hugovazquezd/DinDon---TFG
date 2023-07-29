package com.example.dindon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dindon.DTOFront.User;
import com.example.dindon.Helpers.LoginHelper.ApiClient;
import com.example.dindon.Helpers.LoginHelper.ApiService;
import com.example.dindon.DTOFront.LoginRequest;
import com.example.dindon.DTOFront.LoginResponse;
import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.bbddconection.BBDDManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BBDDManager bbddManager = new BBDDManager(getApplicationContext());

        preferencesManager = new PreferencesManager(this);

        if (preferencesManager.isLoggedIn()) {
            bbddManager.getUserData(
                    user -> {
                        bbddManager.isUserArrendador(user.getId(), arrendador -> {
                            if (arrendador) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, error -> System.out.println("El error es: " + error.getMessage()));
                    },
                    error -> System.out.println("El error es: " + error.getMessage()),
                    preferencesManager.getUserEmail()
            );
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            ApiService apiService = ApiClient.getInstance().create(ApiService.class);
            Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, password));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            String email = loginResponse.getEmail();
                            preferencesManager.setUserEmail(email);
                            preferencesManager.setLoggedIn(true);
                            BBDDManager bbddManager = new BBDDManager(getApplicationContext());

                            bbddManager.getUserData(
                                    user -> {
                                        preferencesManager.setUserName(user.getNombre());
                                        bbddManager.isUserArrendador(user.getId(), arrendador -> {
                                            if (arrendador) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, error -> System.out.println("El error es: " + error.getMessage()));
                                    },
                                    error -> System.out.println("El error es: " + error.getMessage()),
                                    preferencesManager.getUserEmail()
                            );


                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    // Mostrar mensaje de error
                }
            });
        });

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

    }
}
