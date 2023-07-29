package com.example.dindon.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.Opinions;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;

public class RatingFragment extends DialogFragment {
    public interface RatingFragmentListener {
        void onRate(String languageCode);

    }

    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    public void setListener(RatingFragment.RatingFragmentListener listener) {
        // no se usa
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opinion, null);
        PreferencesManager preferencesManager = new PreferencesManager(this.requireContext());
        Button confirmButton = view.findViewById(R.id.btn_enviar_opinion);

        confirmButton.setOnClickListener(v -> {
            BBDDManager bbddManager = new BBDDManager(this.getContext());
            bbddManager.getUserData(
                    user -> {
                        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
                        float rating = ratingBar.getRating();
                        EditText opinionText = view.findViewById(R.id.comentario_opinion);
                        Opinions opinion = new
                                Opinions(user.getId(), opinionText.getText().toString(), rating);

                        bbddManager.createOpinion(opinion,
                                response -> dismiss()
                                , error -> Log.e("Error al crear opinión", error.getMessage())
                        );
                    },
                    error -> Log.e("Error al obtener datos", error.getMessage()),
                    preferencesManager.getUserEmail()
            );

        });

        builder.setView(view);
        return builder.create();
    }
}