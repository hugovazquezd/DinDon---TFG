package com.example.dindon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dindon.helpers.LoginHelper.ApiService;
import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.LoginRequest;
import com.example.dindon.dtofront.LoginResponse;
import com.example.dindon.helpers.LoginHelper.ApiClient;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    PreferencesManager preferencesManager;
    public static final String LOGGER = "com.example.dindon.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BBDDManager bbddManager = new BBDDManager(getApplicationContext());

        preferencesManager = new PreferencesManager(this);

        if (preferencesManager.isLoggedIn()) {
            checkUserTypeAndNavigate(bbddManager);
        }

        initializeViews();

        loginButton.setOnClickListener(view -> loginUser(bbddManager));
    }

    private void checkUserTypeAndNavigate(BBDDManager bbddManager) {
        bbddManager.getUserData(
                user -> bbddManager.isUserArrendador(user.getId(), this::navigateToMain,
                        error -> Logger.getLogger(LOGGER).info("Error: " + error.getMessage())),
                error -> Logger.getLogger(LOGGER).info(error.getMessage()),
                preferencesManager.getUserEmail()
        );
    }

    private void navigateToMain(boolean arrendador) {
        Intent intent;
        if (arrendador) {
            intent = new Intent(LoginActivity.this, MainActivity1.class);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    private void loginUser(BBDDManager bbddManager) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        ApiService apiService = ApiClient.getInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, password));

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    handleSuccessfulLogin(response.body(), bbddManager);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Mostrar mensaje de error
            }
        });
    }

    private void handleSuccessfulLogin(LoginResponse loginResponse, BBDDManager bbddManager) {
        if (loginResponse != null) {
            String email = loginResponse.getEmail();
            preferencesManager.setUserEmail(email);
            preferencesManager.setLoggedIn(true);

            bbddManager.getUserData(
                    user -> {
                        preferencesManager.setUserName(user.getNombre());
                        bbddManager.isUserArrendador(user.getId(), this::navigateToMain,
                                error -> Logger.getLogger(LOGGER).info("El error es: " + error.getMessage()));
                    },
                    error -> Logger.getLogger(LOGGER).info("El error es: " + error.getMessage()),
                    preferencesManager.getUserEmail()
            );
        }
    }

}