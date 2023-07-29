package com.example.dindon;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dindon.Fragments.Chat_fragment;
import com.example.dindon.Fragments.Discover;
import com.example.dindon.Fragments.Discover1;
import com.example.dindon.Fragments.FragmentContainer;
import com.example.dindon.Fragments.LanguageSelectionDialogFragment;
import com.example.dindon.Fragments.Mapa;
import com.example.dindon.Fragments.MenuFragmento;
import com.example.dindon.Fragments.Perfil;
import com.example.dindon.Fragments.ratingFragment;
import com.example.dindon.Helpers.InfoMenu;

import java.util.Locale;


public class MainActivity1 extends AppCompatActivity implements InfoMenu, FragmentContainer, LanguageSelectionDialogFragment.LanguageSelectionListener, ratingFragment.ratingFragmentListener {
    static Fragment[] botones;
    Fragment menuClickeado;
    private boolean isImage1Displayed = true;

    @Override
    public void onLanguageSelected(String languageCode) {
        // Actualiza la configuración de idioma aquí
        setLocale(languageCode);
        reiniciarAplicacion();
        // Luego, recrea la actividad para aplicar los cambios de idioma
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
        botones[0] = new Discover1();
        botones[1] = new Chat_fragment();
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
        //Esta sin implementar
    }
}