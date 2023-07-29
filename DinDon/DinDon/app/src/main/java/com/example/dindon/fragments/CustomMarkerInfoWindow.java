package com.example.dindon.fragments;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dindon.dtofront.Pisos;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class CustomMarkerInfoWindow extends MarkerInfoWindow {
    Pisos p;
    Marker marker;
    MapView mapView;
    Activity activity;

    public CustomMarkerInfoWindow(MapView mapView, Marker marker, Pisos p, Activity activity) {
        super(R.layout.ventana_modal_marcador, mapView);
        this.marker = marker;
        this.p = p;
        this.mapView = mapView;
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onOpen(Object item) {
        Marker marker = (Marker) item;
        TextView textView1 = mView.findViewById(R.id.text_view1);
        TextView textView2 = mView.findViewById(R.id.text_view2);
        textView1.setText(p.getNombre());
        textView2.setText(p.getPrecio() + "€");

        // carga la imagen en el ImageView
        ImageView imageView = mView.findViewById(R.id.imageView);
        String urlNueva = CONSTANTS.URL_CONEXION + "/pisos/" + p.getImagenes().get(0).getRuta();
        Glide.with(this.getView().getContext())
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);

        Button button = mView.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            //cambiamos a la actividad de main_discover
            Intent intent = new Intent(this.activity, MainDiscover.class);
            intent.putExtra("piso", p);
            this.activity.startActivity(intent);
        });
        mapView.setMapCenterOffset(0, 500);

    }
}

