package com.example.dindon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.User;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        setUserContent();
        PreferencesManager preferencesManager = new PreferencesManager(this);
        Button saveChanges = findViewById(R.id.cambiar);
        saveChanges.setOnClickListener(v -> {
            EditText correo = findViewById(R.id.correo);
            EditText nombre = findViewById(R.id.nombre);
            EditText password = findViewById(R.id.contra);
            BBDDManager bbddManager = new BBDDManager(this);
            bbddManager.getUserData(
                    user -> {
                        User user1 = new User();
                        user1.setEmail(correo.getText().toString());
                        user1.setNombre(nombre.getText().toString());
                        user1.setPassword(password.getText().toString());
                        user1.setIsArrendador(user.getIsArrendador());
                        user1.setTipoacceso(user.getTipoacceso());
                        user1.setFriends(user.getFriends());
                        user1.setFriendsRequest(user.getFriendsRequest());
                        preferencesManager.setUserName(user1.getNombre());
                        bbddManager.updateUser(
                                user.getId(), user1,
                                response -> bbddManager.isUserArrendador(user.getId(), this::navigateToMain,
                                        error -> Log.e("fail: ", error.getMessage())),
                                error -> Log.e("Este es el error", error.getMessage())
                        );
                    }, error -> Log.e("ERROR", error.getMessage()),
                    preferencesManager.getUserEmail()
            );

        });
        listenersBotonesRedes();
    }

    private void listenersBotonesRedes() {
        LinearLayout instagram = findViewById(R.id.insta);
        instagram.setOnClickListener(v -> {
            String url = "https://www.instagram.es";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

        LinearLayout linkedin = findViewById(R.id.linkedin);
        linkedin.setOnClickListener(v -> {
            String url = "https://www.linkedin.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

        LinearLayout twitter = findViewById(R.id.twitter);
        twitter.setOnClickListener(v -> {
            String url = "https://www.twitter.es";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

    }

    private void navigateToMain(boolean arrendador) {
        Intent intent;
        if (arrendador) {
            intent = new Intent(this, MainActivity1.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.putExtra("START_FRAGMENT", 3);
        startActivity(intent);
        finish();
    }

    private void setUserContent() {
        BBDDManager bbddManager = new BBDDManager(this);
        PreferencesManager preferencesManager = new PreferencesManager(this);
        bbddManager.getUserData(
                user -> {
                    EditText correo = findViewById(R.id.correo);
                    correo.setText(user.getEmail());
                    EditText nombre = findViewById(R.id.nombre);
                    nombre.setText(user.getNombre());
                }, error -> Log.e("ERROR", error.getMessage()),
                preferencesManager.getUserEmail());
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable icon = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(tool -> {
            BBDDManager bbddManager = new BBDDManager(this);
            bbddManager.getUserData(
                    user -> bbddManager.isUserArrendador(user.getId(), this::navigateToMain,
                            error -> Log.e("mal: ", error.getMessage())),
                    error -> Log.e("fallo: ", error.getMessage()),
                    new PreferencesManager(this).getUserEmail()
            );
        });

    }

}
