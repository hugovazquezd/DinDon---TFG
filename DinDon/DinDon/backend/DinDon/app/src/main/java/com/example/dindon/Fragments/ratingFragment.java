package com.example.dindon.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.dindon.R;

public class ratingFragment extends DialogFragment {
    public interface ratingFragmentListener {
        void onRate(String languageCode);
    }

    public static ratingFragment newInstance() {
        return new ratingFragment();
    }

    public void setListener(ratingFragment.ratingFragmentListener listener) {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opinion, null);

        Button confirmButton = view.findViewById(R.id.btn_enviar_opinion);

        confirmButton.setOnClickListener(v -> {

        });

        builder.setView(view);
        return builder.create();
    }
}