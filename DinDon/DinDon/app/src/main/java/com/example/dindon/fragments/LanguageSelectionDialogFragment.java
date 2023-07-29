package com.example.dindon.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.dindon.R;


public class LanguageSelectionDialogFragment extends DialogFragment {

    public interface LanguageSelectionListener {
        void onLanguageSelected(String languageCode);
    }

    private LanguageSelectionListener mListener;

    public static LanguageSelectionDialogFragment newInstance() {
        return new LanguageSelectionDialogFragment();
    }

    public void setListener(LanguageSelectionListener listener) {
        mListener = listener;
    }

    @SuppressLint("NonConstantResourceId")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.language_selection_dialog, null);

        RadioGroup radioGroup = view.findViewById(R.id.language_selection);
        Button confirmButton = view.findViewById(R.id.confirm_button);


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString("language", "es");

        switch (selectedLanguage) {
            case "es":
                radioGroup.check(R.id.spanish);
                break;
            case "en":
                radioGroup.check(R.id.english);
                break;
            case "gl":
                radioGroup.check(R.id.galician);
                break;
            case "ca":
                radioGroup.check(R.id.catalan);
                break;
            default:
                radioGroup.check(R.id.spanish);
                break;
        }

        confirmButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String languageCode = "";
            switch (selectedId) {
                case R.id.spanish:
                    languageCode = "es";
                    break;
                case R.id.english:
                    languageCode = "en";
                    break;
                case R.id.galician:
                    languageCode = "gl";
                    break;
                case R.id.catalan:
                    languageCode = "ca";
                    break;
                default:
                    languageCode = "es";
                    break;
            }

            if (mListener != null) {
                mListener.onLanguageSelected(languageCode);
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}