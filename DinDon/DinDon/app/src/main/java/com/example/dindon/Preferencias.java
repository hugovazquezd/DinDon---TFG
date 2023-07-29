package com.example.dindon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Preferencias extends AppCompatActivity {
    private ChipGroup chipGroupGeneral;
    private ChipGroup chipGroupSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        chipGroupSeleccionado = findViewById(R.id.chipGroupGeneral);
        chipGroupGeneral = findViewById(R.id.chipGroupSeleccionado);

        setupChips();
        BBDDManager bbddManager = new BBDDManager(this);
        Button aceptar = findViewById(R.id.aceptar);
        aceptar.setOnClickListener(si -> {
                    List<String> preferencias = new ArrayList<>();
                    for (int i = 0; i < chipGroupSeleccionado.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupSeleccionado.getChildAt(i);
                        preferencias.add(chip.getText().toString());
                    }
                    bbddManager.getUserData(
                            u -> bbddManager.editUserPreferences(
                                    u.getId(),
                                    preferencias,
                                    t -> this.navigateToMain(true),
                                    no -> Log.e("fallo: ", no.getMessage())
                            ),
                            e -> Log.e("fallo: ", e.getMessage())
                            , new PreferencesManager(this).getUserEmail()
                    );
                }
        );
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable icon = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(tool -> {
            BBDDManager bbddManager = new BBDDManager(this);
            bbddManager.getUserData(
                    user -> bbddManager.isUserArrendador(user.getId(), this::navigateToMain,
                            error -> Log.e("mal: ", error.getMessage())),
                    error -> Log.e("fallo: ", error.getMessage()),
                    new PreferencesManager(this).getUserEmail()
            );
        });

    }

    private void setupChips() {
        BBDDManager bbddManager = new BBDDManager(this);
        bbddManager.getUserData(
                user -> {
                    // Crear un conjunto con las preferencias del usuario para la búsqueda eficiente
                    Set<String> userPreferences = new HashSet<>(user.getPreferencias());

                    for (String chipName : CONSTANTS.CHIPS) {
                        Chip chip = new Chip(this);
                        chip.setText(chipName);
                        chip.setChipBackgroundColorResource(R.color.colorC);
                        // Si las preferencias del usuario contienen este chip, agrégalo al grupo seleccionado
                        if (userPreferences.contains(chipName)) {
                            chipGroupSeleccionado.addView(chip);
                        } else {
                            // de lo contrario, agrégalo al grupo general
                            chipGroupGeneral.addView(chip);
                        }

                        chip.setOnClickListener(v -> {
                            if (chipGroupGeneral.indexOfChild(chip) != -1) {
                                chipGroupGeneral.removeView(chip);
                                chipGroupSeleccionado.addView(chip);
                            } else if (chipGroupSeleccionado.indexOfChild(chip) != -1) {
                                chipGroupSeleccionado.removeView(chip);
                                chipGroupGeneral.addView(chip);
                            }
                        });
                    }
                },
                error -> Log.e("fallo: ", error.getMessage()),
                new PreferencesManager(this).getUserEmail()
        );
    }


    private void navigateToMain(boolean arrendador) {
        Intent intent;
        if (arrendador) {
            intent = new Intent(this, MainActivity1.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.putExtra("START_FRAGMENT", 3);
        startActivity(intent);
        finish();
    }


}
