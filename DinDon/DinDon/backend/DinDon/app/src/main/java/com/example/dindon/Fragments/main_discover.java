package com.example.dindon.Fragments;

import static com.example.dindon.Helpers.MapHelper.createDrawableFromView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.dindon.DTOFront.ImageMetadata;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.Helpers.DiscoverHelpers.ImagePageAdapter;
import com.example.dindon.Helpers.MapHelper;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class main_discover extends AppCompatActivity {
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

        Pisos piso = (Pisos) getIntent().getSerializableExtra("piso");

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

    }

}


