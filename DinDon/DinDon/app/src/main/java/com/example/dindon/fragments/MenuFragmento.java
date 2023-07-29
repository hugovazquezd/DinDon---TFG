package com.example.dindon.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dindon.helpers.InfoMenu;
import com.example.dindon.R;


public class MenuFragmento extends Fragment {
    public MenuFragmento() {
    }

    private final int[] botones = {
            R.id.btndiscover,
            R.id.btnchat,
            R.id.btnmapa,
            R.id.btnperfil
    };

    private final int[] botonesPintados = {
            R.drawable.btndiscoverselected,
            R.drawable.btnchatselected,
            R.drawable.btnmapaselected,
            R.drawable.btnperfilselected
    };

    private int botonSeleccionado = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View menuPrincipal = inflater.inflate(R.layout.fragment_menu_fragmento, container, false);

        if (getArguments() != null) {
            botonSeleccionado = getArguments().getInt("BOTONSELECCIONADO");
        }
        ImageButton btn;

        for (int i = 0; i < botones.length; i++) {
            btn = (ImageButton) menuPrincipal.findViewById(botones[i]);
            if (botonSeleccionado == i) {
                btn.setImageResource(botonesPintados[i]);
            }
            final int seleccionado = i;
            btn.setOnClickListener(v -> {
                Activity estaActividad = getActivity();
                assert estaActividad != null;
                ((InfoMenu) estaActividad).onMenuSelected(seleccionado);
            });
        }

        return menuPrincipal;
    }
}
