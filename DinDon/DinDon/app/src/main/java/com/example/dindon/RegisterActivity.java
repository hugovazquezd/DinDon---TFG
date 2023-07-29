package com.example.dindon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dindon.helpers.CONSTANTS;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText emailEditText;
        EditText passwordEditText;
        EditText nameEditText;
        RadioGroup radioGroup;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.usernameEditText);
        Button registerButton = findViewById(R.id.registerButton);
        radioGroup = findViewById(R.id.radioGroup);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String tipoUsuario = radioGroup.getCheckedRadioButtonId() == R.id.arrendador ? "Arrendador" : "Arrendatario";

            if (!email.isEmpty() && !password.isEmpty()) {
                registerUser(email, password,name, tipoUsuario);
            } else {
                Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        TextView registerTextView = findViewById(R.id.LoginTextView);
        registerTextView.setOnClickListener(v -> {
            Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(registerIntent);
        });
    }

    private void registerUser(final String email, final String password, final String name, final String tipoUsuario) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email);
                jsonObject.put("password", password);
                jsonObject.put("name", name);
                jsonObject.put("tipoUsuario", tipoUsuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(json, jsonObject.toString());
            String url = CONSTANTS.URL_CONEXION + "/api/auth/signup";
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}


