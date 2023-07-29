package com.example.dindon.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dindon.Fragments.CustomMarkerInfoWindow;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.R;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class MapHelper {

    public void addMarkers(ArrayList<Pisos> pisosList, MapView map, Activity activity) {
        for (int i = 0; i < pisosList.size(); i++) {
            GeoPoint point = new GeoPoint(pisosList.get(i).getLatitud(), pisosList.get(i).getLongitud());
            // Configurar el marcador
            Marker marker = new Marker(map);
            marker.setPosition(point);
            @SuppressLint("InflateParams") View customMarkerView = LayoutInflater.from(map.getContext()).inflate(R.layout.custom_marker, null);
            marker.setIcon(new BitmapDrawable(map.getResources(), createDrawableFromView(map.getContext(), customMarkerView)));
            marker.setInfoWindow(new CustomMarkerInfoWindow(map, marker, pisosList.get(i), activity));
            map.getOverlays().add(marker);
            CustomMarkerInfoWindow infoWindow = new CustomMarkerInfoWindow(map, marker, pisosList.get(i), activity);
            marker.setInfoWindow(infoWindow);

        }
        map.invalidate();
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void configuracionMapa(MapView map, Context context, Activity activity) {

        map.setTileSource(TileSourceFactory.WIKIMEDIA);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            GeoPoint currentLocation = new GeoPoint(42.87821, -8.54485);

            if (location != null) {
                currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(context, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
            }
            map.getController().setZoom(17.5);
            map.getController().setCenter(currentLocation);
            map.getController().animateTo(currentLocation);
        }
    }

    public void configuracionMapaConcreto(MapView map, Pisos piso) {

        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        GeoPoint currentLocation = new GeoPoint(piso.getLatitud(), piso.getLongitud());
        map.getController().setZoom(17.5);
        map.getController().setCenter(currentLocation);
        map.getController().animateTo(currentLocation);
    }

    public void addMarker(Pisos piso, MapView map) {
        GeoPoint point = new GeoPoint(piso.getLatitud(), piso.getLongitud());
        // Configurar el marcador
        Marker marker = new Marker(map);
        marker.setPosition(point);
        @SuppressLint("InflateParams") View customMarkerView = LayoutInflater.from(map.getContext()).inflate(R.layout.custom_marker, null);
        marker.setIcon(new BitmapDrawable(map.getResources(), createDrawableFromView(map.getContext(), customMarkerView)));
        marker.setInfoWindow(null);
        map.getOverlays().add(marker);
        map.invalidate();
    }


    @SuppressLint("StaticFieldLeak")
    public void buscarCiudad(String ciudad, Context context, Consumer<List<Address>> callback) {
        new AsyncTask<String, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(String... params) {
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    return geocoder.getFromLocationName(params[0], 5);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses != null && !addresses.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        callback.accept(addresses);
                    }
                }
            }
        }.execute(ciudad);
    }


    @SuppressLint("StaticFieldLeak")
    public void realizarBusqueda(String ciudad, MapView map, Context context) {
        new AsyncTask<String, Void, GeoPoint>() {
            @Override
            protected GeoPoint doInBackground(String... params) {
                try {
                    Locale locale = new Locale("es", "ES");
                    GeocoderNominatim geocoder = new GeocoderNominatim(locale, "UserAgent");
                    List<Address> addresses = geocoder.getFromLocationName(params[0], 1);

                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        return new GeoPoint(address.getLatitude(), address.getLongitude());
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(GeoPoint cityLocation) {
                if (cityLocation != null) {
                    map.getController().animateTo(cityLocation);
                }
            }
        }.execute(ciudad);
    }

}

