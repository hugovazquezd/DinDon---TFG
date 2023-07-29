package com.example.dindon;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
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
import com.example.dindon.dtofront.ImageMetadata;
import com.example.dindon.dtofront.Pisos;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;
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
import java.util.logging.Logger;

public class NewPisoActivity extends AppCompatActivity {
    ImageView selectImagesButton;
    private RequestQueue queue;
    Pisos newPiso;

    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_piso);
        setupToolbar(findViewById(R.id.toolbar));
        Button crear = findViewById(R.id.create);

        Intent intent = getIntent();
        if (intent.hasExtra("PISO")) {
            handleEditPiso(crear);
        } else {
            handleAddPiso(crear);
        }
    }

    private void handleEditPiso(Button crear) {
        String newP = getIntent().getSerializableExtra("PISO").toString();
        llenarPiso();

        crear.setOnClickListener(v -> {
            Pisos p = editarPiso();
            queue = Volley.newRequestQueue(this);
            String direccion = ((EditText) findViewById(R.id.localizacion)).getText().toString();
            String url = "https://nominatim.openstreetmap.org/search?q=" + direccion + "&format=json&addressdetails=1&limit=1&polygon_svg=1";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            if (response.length() > 0) {
                                JSONObject jsonObject = response.getJSONObject(0);
                                String lat = jsonObject.optString("lat", "");
                                String lon = jsonObject.optString("lon", "");
                                if (!lat.isEmpty() && !lon.isEmpty()) {
                                    p.setLatitud(Double.valueOf(lat));
                                    p.setLongitud(Double.valueOf(lon));
                                } else {
                                    Log.e(ERROR, "Latitude or longitude is empty");
                                }

                                BBDDManager bbddManager = new BBDDManager(this);
                                bbddManager.editPiso(newP, p,
                                        response1 -> {
                                            Log.d(SUCCESS, "Piso editado con éxito");
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("LOCALIZACION_EDITADA", p.getDireccion());
                                            setResult(Activity.RESULT_OK, resultIntent);
                                            finish();
                                        },
                                        error -> Log.e(ERROR, "Hubo un error al editar el piso", error)
                                );
                            } else {
                                Log.e(ERROR, "Empty JSON array");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(ERROR, "JSON parsing error: " + e.getMessage());
                        }
                    },
                    error -> Log.e(ERROR, "Error al obtener la latitud y longitud", error)
            );

            queue.add(request);
        });
    }

    private void handleAddPiso(Button crear) {
        queue = Volley.newRequestQueue(this);

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

            Response.Listener<Pisos> successListener = response -> Log.d(SUCCESS, "Piso añadido con éxito");
            Response.ErrorListener errorListener = error -> Log.e(ERROR, "Hubo un error al añadir el piso", error);

            try {
                crearPiso(piso -> {
                    bbddManager.addPiso(piso, successListener, errorListener);
                    this.finish();
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }


    private Pisos editarPiso() {
        Pisos piso = new Pisos();
        piso.setNombre(((EditText) findViewById(R.id.titulo)).getText().toString());
        piso.setDescripcion(((EditText) findViewById(R.id.descripcion)).getText().toString());
        piso.setPrecio(Double.valueOf(((EditText) findViewById(R.id.precio)).getText().toString()));
        piso.setHabitaciones(Integer.valueOf(((EditText) findViewById(R.id.habitaciones)).getText().toString()));
        piso.setBanos(Integer.valueOf(((EditText) findViewById(R.id.bathrooms)).getText().toString()));
        piso.setDireccion(((EditText) findViewById(R.id.localizacion)).getText().toString());

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

        piso.getServiciosDisponibles().removeAll(piso.getServiciosDisponibles());
        for (CheckBox servicio : servicios) {
            if (servicio.isChecked()) {
                piso.getServiciosDisponibles().add(servicio.getText().toString());
            }
        }
        return piso;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newPiso = new Pisos();

        if (requestCode == 777 && resultCode == RESULT_OK) {
            List<String> encodedImages = new ArrayList<>();
            TextView textView = findViewById(R.id.titulo);
            if (data.getClipData() != null) {
                handleMultipleImagesSelection(data, textView, encodedImages);
            } else if (data.getData() != null) {
                handleSingleImageSelection(data, textView, encodedImages);
            }
            uploadImage(encodedImages);
        }
    }

    private void handleMultipleImagesSelection(Intent data, TextView textView, List<String> encodedImages) {
        int count = data.getClipData().getItemCount();
        List<String> imagesUrls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Uri imageUri = data.getClipData().getItemAt(0).getUri();
            loadImageFromUri(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                encodedImages.add(encodeImage(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String ubicacion = textView.getText() + "/" + i;
            imagesUrls.add(ubicacion);
        }
        addImagesToPiso(imagesUrls);
    }

    private void handleSingleImageSelection(Intent data, TextView textView, List<String> encodedImages) {
        Uri imageUri = data.getData();
        loadImageFromUri(imageUri);
        String ubi = textView.getText() + "/" + 0;
        addImageToPiso(ubi);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            encodedImages.add(encodeImage(bitmap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromUri(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(selectImagesButton);
    }

    private void addImagesToPiso(List<String> imagesUrls) {
        for (String ubi : imagesUrls) {
            ImageMetadata image = new ImageMetadata(null, ubi);
            newPiso.getImagenes().add(image);
        }
    }

    private void addImageToPiso(String ubi) {
        ImageMetadata image = new ImageMetadata(null, ubi);
        newPiso.getImagenes().add(image);
    }

    public String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public void llenarPiso() {
        Intent intent = getIntent();
        String pisos = (String) intent.getSerializableExtra("PISO");

        BBDDManager bbddManager = new BBDDManager(this);
        bbddManager.getPisoById(pisos, piso -> {
            EditText titulo = findViewById(R.id.titulo);
            titulo.setText(piso.getNombre());
            EditText precio = findViewById(R.id.precio);
            precio.setText(String.valueOf(piso.getPrecio()));
            EditText localizacion = findViewById(R.id.localizacion);
            localizacion.setText(piso.getDireccion());
            EditText descripcion = findViewById(R.id.descripcion);
            descripcion.setText(piso.getDescripcion());

            ArrayList<String> serviciosD = (ArrayList<String>) piso.getServiciosDisponibles();
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
                for (String serv : serviciosD) {
                    if (servicio.getText().toString().equalsIgnoreCase(serv)) {
                        servicio.setChecked(true);
                    }
                }
            }


            EditText habitaciones = findViewById(R.id.habitaciones);
            habitaciones.setText(String.valueOf(piso.getHabitaciones()));
            EditText banos = findViewById(R.id.bathrooms);
            banos.setText(String.valueOf(piso.getBanos()));

            ImageView imagen = findViewById(R.id.imagen);
            // Cargar las imágenes
            Glide.with(this)
                    .load(CONSTANTS.URL_CONEXION + "/pisos/" + piso.getImagenes().get(0).getRuta())
                    .placeholder(0)
                    .error(0)
                    .dontAnimate()
                    .into(imagen);
        }, error -> {
        });


    }

    public void uploadImage(List<String> images) {
        TextView textView = findViewById(R.id.titulo);
        String url = CONSTANTS.URL_CONEXION + "/pisos/upload/" + textView.getText();
        PreferencesManager preferencesManager = new PreferencesManager(this);
        BBDDManager bbddManager = new BBDDManager(this);
        bbddManager.getUserData(
                user -> bbddManager.isUserArrendador(user.getId(), arrendador -> {

                }, error -> Logger.getLogger("Error al comprobar si el usuario es arrendador")),
                error -> Logger.getLogger("Error al obtener los datos del usuario"),
                preferencesManager.getUserEmail()
        );

        JSONArray jsonArray = new JSONArray(images);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                response -> Log.d(SUCCESS, response),
                error -> Log.e(ERROR, "Hubo un error al subir las imágenes", error)
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonArray.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(stringRequest);
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
                error -> Logger.getLogger("Error al obtener los datos del usuario"),
                preferencesManager.getUserEmail()
        );
    }

    public void obtenerLatLongDesdeDireccion(String tuDireccion, Pisos newPiso, PisoCallback
            pisoCallback) throws UnsupportedEncodingException {
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
                error -> Log.e("Volley", error.toString())

        );
        queue.add(request);
    }


}
