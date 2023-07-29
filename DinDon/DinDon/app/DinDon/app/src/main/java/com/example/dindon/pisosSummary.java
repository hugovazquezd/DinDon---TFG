package com.example.dindon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.Fragments.Chat_fragment;
import com.example.dindon.Fragments.Discover;
import com.example.dindon.Fragments.Mapa;
import com.example.dindon.Fragments.MenuFragmento;
import com.example.dindon.Fragments.Perfil;
import com.example.dindon.Helpers.CONSTANTS;
import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.bbddconection.BBDDManager;

public class pisosSummary extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pisos_summary);
        PreferencesManager preferencesManager = new PreferencesManager(this);
        LinearLayout propertiesContainer = findViewById(R.id.properties_container);
        BBDDManager bbddManager = new BBDDManager(this);
        bbddManager.getUserData(
                user -> bbddManager.getPisosbyUser(user.getId(),
                        pisosList -> {
                            for (Pisos p : pisosList) {
                                View cardView = getLayoutInflater().inflate(R.layout.pisos_cardview, propertiesContainer, false);
                                ImageView imageView = cardView.findViewById(R.id.property_image);
                                TextView nameView = cardView.findViewById(R.id.property_name);
                                nameView.setText(p.getDireccion());
                                obtenerImagenDesdeServidor(p.getImagenes().get(0).getRuta(), imageView, this);
                                propertiesContainer.addView(cardView);
                            }
                        },
                        error -> {
                        }
                ),
                error -> {
                },
                preferencesManager.getUserEmail()
        );
        setupToolbar(findViewById(R.id.toolbar));

        Button nuevoPiso= findViewById(R.id.newPiso);
        nuevoPiso.setOnClickListener(v -> {
            Intent intent = new Intent(this, newPiso.class);
            startActivity(intent);
        });
    }

    public void obtenerImagenDesdeServidor(String ruta, ImageView imageView, Context context) {
        String urlNueva = CONSTANTS.URL_CONEXION + "/pisos/" + ruta;
        Glide.with(context)
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable icon = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            Intent intent = new Intent(this, MainActivity1.class);
            intent.putExtra("START_FRAGMENT", 3);
            startActivity(intent);

        });
    }


}

