package com.example.dindon;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.fragments.ChatFragment;
import com.example.dindon.fragments.Discover;
import com.example.dindon.fragments.FragmentContainer;
import com.example.dindon.fragments.LanguageSelectionDialogFragment;
import com.example.dindon.fragments.Mapa;
import com.example.dindon.fragments.MenuFragmento;
import com.example.dindon.fragments.Perfil;
import com.example.dindon.fragments.RatingFragment;
import com.example.dindon.helpers.InfoMenu;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements InfoMenu, FragmentContainer, LanguageSelectionDialogFragment.LanguageSelectionListener, RatingFragment.RatingFragmentListener {
    Fragment[] botones;
    Fragment menuClickeado;
    private boolean isImage1Displayed = true;

    @Override
    public void onLanguageSelected(String languageCode) {
        setLocale(languageCode);
        reiniciarAplicacion();
    }

    private void reiniciarAplicacion() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Guarda el idioma seleccionado en las preferencias para usarlo cuando la aplicación se reinicie
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("language", "");
        setLocale(language);

        setContentView(R.layout.activity_main);
        botones = new Fragment[4];
        botones[0] = new Discover();
        botones[1] = new ChatFragment();
        botones[2] = new Mapa();
        botones[3] = new Perfil();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        int startFragment = getIntent().getIntExtra("START_FRAGMENT", 0);
        ft.add(R.id.opciones, botones[startFragment]);
        // Aquí agregamos la lógica para actualizar el menú
        menuClickeado = new MenuFragmento();
        Bundle bundle = new Bundle();
        bundle.putInt("BOTONSELECCIONADO", startFragment);
        menuClickeado.setArguments(bundle);
        ft.replace(R.id.menu, menuClickeado);
        ft.commit();
        ImageView imageView = findViewById(R.id.frase);
        imageView.setOnClickListener(v -> {
            if (isImage1Displayed) {
                imageView.setImageResource(R.drawable.frase_up2);
            } else {
                imageView.setImageResource(R.drawable.frase_up);
            }
            isImage1Displayed = !isImage1Displayed;
        });

    }

    @Override
    public void onMenuSelected(int botonSeleccionado) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        menuClickeado = new MenuFragmento();
        Bundle bundle = new Bundle();
        bundle.putInt("BOTONSELECCIONADO", botonSeleccionado);
        menuClickeado.setArguments(bundle);
        ft.replace(R.id.opciones, botones[botonSeleccionado]);
        ft.replace(R.id.menu, menuClickeado);
        ft.commit();
    }


    @NonNull
    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    public void onRate(String languageCode) {
        //Está sin implementar
    }
}