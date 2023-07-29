package com.example.dindon.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dindon.dtofront.ImageMetadata;
import com.example.dindon.dtofront.Pisos;
import com.example.dindon.helpers.DiscoverHelpers.ImagePageAdapter;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;
import com.example.dindon.helpers.MapHelper;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainDiscover extends AppCompatActivity {
    MapHelper mapHelper = new MapHelper();

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_discover);
        Pisos piso;
        if (getIntent().getSerializableExtra("PISO") != null) {
            piso = (Pisos) getIntent().getSerializableExtra("PISO");

        } else {
            piso = (Pisos) getIntent().getSerializableExtra("piso");
        }

        ViewPager viewPager = findViewById(R.id.viewPager);
        List<ImageMetadata> imagenes = piso.getImagenes();

        ImagePageAdapter imagePagerAdapter = new ImagePageAdapter(this, imagenes);
        viewPager.setAdapter(imagePagerAdapter);

        TextView titulo = findViewById(R.id.house_title);
        BBDDManager bbddManager = new BBDDManager(getApplicationContext());
        bbddManager.getUserById(piso.getPropietario(),
                titulo::setText, error -> titulo.setText(piso.getPropietario()));

        TextView descripcion = findViewById(R.id.house_description);
        descripcion.setText(piso.getDescripcion());

        TextView precio = findViewById(R.id.precio_amount);
        precio.setText(piso.getPrecio().toString() + "€");

        TextView direccion = findViewById(R.id.location);
        direccion.setText(piso.getDireccion());
        MapView mapView = findViewById(R.id.map);
        mapHelper.configuracionMapaConcreto(mapView, piso);
        mapHelper.addMarker(piso, mapView);

        ImageView flecha = findViewById(R.id.flecha);
        flecha.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        });

        ImageView din = findViewById(R.id.din);
        din.setOnClickListener(v -> {
            //Me gusta
            PreferencesManager prefe = new PreferencesManager(getApplicationContext());
            String propietario = piso.getPropietario();
            bbddManager.getUserById(
                    usuarioPropietario -> {
                        AtomicBoolean esAmigo = new AtomicBoolean(false);
                        List<String> friendRequests = usuarioPropietario.getFriendsRequest();
                        if (friendRequests != null) {
                            bbddManager.getUserData(
                                    yo -> {
                                        if (yo.getFriends() != null) {
                                            for (String amigo : usuarioPropietario.getFriends()) {
                                                if (amigo.equals(yo.getId())) {
                                                    esAmigo.set(true);
                                                    break;
                                                }
                                            }
                                            for (String amigo : friendRequests) {
                                                if (amigo.equals(yo.getId())) {
                                                    esAmigo.set(true);
                                                    break;
                                                }
                                            }
                                        }
                                        if (!esAmigo.get()) {
                                            friendRequests.add(yo.getId());
                                            bbddManager.updateUserB(usuarioPropietario.getId(),
                                                    usuarioPropietario,
                                                    s -> Log.d("Discover", "Se ha enviado la petición de amistad"),
                                                    t -> Log.d("Discover", "No se ha enviado la petición de amistad")
                                            );
                                        }
                                    },
                                    error -> Log.e("Discover", "Error al obtener el usuario propietario", error),
                                    prefe.getUserEmail()
                            );
                        }
                    },
                    error -> Log.e("Discover", "Error al obtener el usuario propietario", error),
                    propietario
            );
            finish();
            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        });

        ImageView don = findViewById(R.id.don);
        don.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        });


    }

}


