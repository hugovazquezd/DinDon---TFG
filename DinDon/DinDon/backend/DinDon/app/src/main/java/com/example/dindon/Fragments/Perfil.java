package com.example.dindon.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.Helpers.CONSTANTS;
import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.pisosSummary;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Perfil extends Fragment {
    ImageView perfil;
    private RequestQueue queue;

    public Perfil() {
        // Required empty public constructor
    }

    private void showLanguageDialog() {
        LanguageSelectionDialogFragment dialogFragment = LanguageSelectionDialogFragment.newInstance();
        dialogFragment.setListener((LanguageSelectionDialogFragment.LanguageSelectionListener) getActivity());
        dialogFragment.show(getChildFragmentManager(), "languageDialog");
    }

    private void showRatingDialog() {
        ratingFragment dialogFragment = ratingFragment.newInstance();
        dialogFragment.setListener((ratingFragment.ratingFragmentListener) getActivity());
        dialogFragment.show(getChildFragmentManager(), "dialog_opinion");
    }

    private void setFotoPerfil(String ruta, ImageView imageView, Context context) throws IOException {
        String urlNueva = CONSTANTS.URL_CONEXION + "/users/imagenes/" + ruta;
        Glide.with(context)
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);
    }

    public String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public void uploadImage(String image) throws UnsupportedEncodingException {
        PreferencesManager preferencesManager = new PreferencesManager(requireActivity());
        String email = preferencesManager.getUserEmail();
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
        String url = CONSTANTS.URL_CONEXION + "/users/upload/" + encodedEmail;

        JSONObject json = new JSONObject();
        try {
            json.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, json,
                response -> Log.d("Success", "Imagen subida con éxito"),
                error -> Log.e("Error", "Hubo un error al subir la imagen", error)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        queue = Volley.newRequestQueue(this.requireContext());

        Button languageButton = view.findViewById(R.id.button5);
        languageButton.setOnClickListener(v -> showLanguageDialog());

        Button ratingButton = view.findViewById(R.id.button6);
        ratingButton.setOnClickListener(v -> showRatingDialog());

        PreferencesManager preferencesManager = new PreferencesManager(requireActivity());

        TextView textView = view.findViewById(R.id.textView);
        textView.setText(preferencesManager.getUserName());

        perfil = view.findViewById(R.id.imageView2);
        try {
            setFotoPerfil(preferencesManager.getUserEmail(), perfil, requireActivity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ImageView editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> mGetContent.launch(CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())));

        Button logoutButton = view.findViewById(R.id.exit);
        logoutButton.setOnClickListener(v -> {
            preferencesManager.setLoggedIn(false);
            Intent intent = requireActivity().getPackageManager().getLaunchIntentForPackage(requireActivity().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requireActivity().startActivity(intent);

        });

        Button policy = view.findViewById(R.id.button4);
        policy.setOnClickListener(v -> {
            String url = "https://keen-genie-ed4cd9.netlify.app";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        Button pisos = view.findViewById(R.id.button3);
        pisos.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), pisosSummary.class);
            startActivity(i);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                    assert cropResult != null;
                    Uri resultUri = cropResult.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), resultUri);
                        perfil.setImageBitmap(bitmap);
                        // Codificar la imagen a base64 y subirla
                        uploadImage(encodeImage(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

}
