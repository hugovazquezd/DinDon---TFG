package com.example.dindon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.Helpers.CONSTANTS;
import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.bbddconection.BBDDManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class newPiso extends AppCompatActivity {
    ImageView selectImagesButton;
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_piso);
        queue = Volley.newRequestQueue(this);
        Button crear = findViewById(R.id.create);
        setupToolbar(findViewById(R.id.toolbar));

        selectImagesButton = findViewById(R.id.imagen);
        selectImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imágenes"), 777);
        });

        crear.setOnClickListener(v -> {
            BBDDManager bbddManager = new BBDDManager(this);
            // Crear los listeners para manejar la respuesta
            Response.Listener<Pisos> successListener = response ->
                    Log.d("Success", "Piso añadido con éxito");
            Response.ErrorListener errorListener = error ->
                    Log.e("Error", "Hubo un error al añadir el piso", error);
            // Ahora usa el otro crearPiso() que tiene el callback y hace la llamada a la API
            try {
                crearPiso(piso -> {
                    // Este código se ejecutará cuando el piso se haya creado y tengamos latitud y longitud
                    bbddManager.addPiso(piso, successListener, errorListener);
                    this.finish();
                });
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 777 && resultCode == RESULT_OK) {
            List<String> encodedImages = new ArrayList<>();

            if (data.getClipData() != null) {
                // Si se seleccionan múltiples imágenes
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(0).getUri();
                    Glide.with(this)
                            .load(imageUri)
                            .placeholder(0)
                            .error(0)
                            .dontAnimate()
                            .into(selectImagesButton);
                    try {
                        // Convertir la imagen a un Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        // Codificar la imagen a base64 y añadirla a la lista
                        encodedImages.add(encodeImage(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                // Si se selecciona una única imagen
                Uri imageUri = data.getData();
                Glide.with(this)
                        .load(imageUri)
                        .placeholder(0)
                        .error(0)
                        .dontAnimate()
                        .into(selectImagesButton);
                try {
                    // Convertir la imagen a un Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    // Codificar la imagen a base64 y añadirla a la lista
                    encodedImages.add(encodeImage(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            uploadImage(encodedImages);
        }
    }

    public String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }


    public void uploadImage(List<String> images) {
        String url = CONSTANTS.URL_CONEXION + "/pisos/upload";

        JSONArray jsonArray = new JSONArray(images);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, url, jsonArray,
                response -> Log.d("Success", "Imagenes subidas con éxito"),
                error -> Log.e("Error", "Hubo un error al subir las imágenes", error)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }


    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable icon = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(v -> this.finish());
    }

    public interface PisoCallback {
        void onCallback(Pisos piso);
    }

    public void crearPiso(PisoCallback pisoCallback) throws UnsupportedEncodingException {
        Pisos newPiso = new Pisos();
        TextView nombre = findViewById(R.id.titulo);
        newPiso.setNombre(nombre.getText().toString());
        TextView precio = findViewById(R.id.precio);
        newPiso.setPrecio(Double.parseDouble(precio.getText().toString()));
        TextView localizacion = findViewById(R.id.localizacion);
        String direccion = localizacion.getText().toString();

        TextView descripcion = findViewById(R.id.descripcion);
        newPiso.setDescripcion(descripcion.getText().toString());
        newPiso.setDireccion(direccion);

        TextView habitaciones = findViewById(R.id.habitaciones);
        newPiso.setHabitaciones(Integer.parseInt(habitaciones.getText().toString()));
        TextView banos = findViewById(R.id.bathrooms);
        newPiso.setBanos(Integer.parseInt(banos.getText().toString()));

        ArrayList<CheckBox> servicios = new ArrayList<>();
        CheckBox calefaccion = findViewById(R.id.checkBox);
        servicios.add(calefaccion);
        CheckBox aire = findViewById(R.id.checkBox2);
        servicios.add(aire);
        CheckBox ascensor = findViewById(R.id.checkBox3);
        servicios.add(ascensor);
        CheckBox portero = findViewById(R.id.checkBox4);
        servicios.add(portero);
        CheckBox terraza = findViewById(R.id.checkBox5);
        servicios.add(terraza);
        CheckBox lavavajillas = findViewById(R.id.checkBox6);
        servicios.add(lavavajillas);
        CheckBox wifi = findViewById(R.id.checkBox7);
        servicios.add(wifi);

        for (CheckBox servicio : servicios) {
            if (servicio.isChecked()) {
                newPiso.getServiciosDisponibles().add(servicio.getText().toString());
            }
        }

        // Obtén la latitud y longitud desde la dirección
        obtenerLatLongDesdeDireccion(direccion, newPiso, pisoCallback);

        BBDDManager bbddManager = new BBDDManager(this);
        PreferencesManager preferencesManager = new PreferencesManager(this);
        bbddManager.getUserData(
                user -> newPiso.setPropietario(user.getId()),
                error -> System.out.println("El error es: " + error.getMessage()),
                preferencesManager.getUserEmail()
        );
    }

    public void obtenerLatLongDesdeDireccion(String tuDireccion, Pisos newPiso, PisoCallback pisoCallback) throws UnsupportedEncodingException {
        tuDireccion = tuDireccion.replace(" ", "%20");
        tuDireccion = URLEncoder.encode(tuDireccion, "UTF-8");
        String urlRequest = "https://nominatim.openstreetmap.org/search/" + tuDireccion + "?format=json&addressdetails=1&limit=1";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlRequest, null,
                response -> {
                    try {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            Double latitud = jsonObject.getDouble("lat");
                            Double longitud = jsonObject.getDouble("lon");
                            newPiso.setLatitud(latitud);
                            newPiso.setLongitud(longitud);

                            // Callback con el objeto Pisos lleno
                            pisoCallback.onCallback(newPiso);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Volley", error.toString());
                }
        );
        queue.add(request);
    }


}
