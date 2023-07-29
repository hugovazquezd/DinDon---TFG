package com.example.dindon.Helpers.ChatHelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.R;

public class ChatDialog {

    public interface ChatDialogListener {
        void onDialogPositiveClick(String sender, String receiver);
    }

    public ChatDialog(Context context, ChatDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ingrese los nombres de usuario:");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_inputs, null);
        builder.setView(view);
        PreferencesManager preferencesManager = new PreferencesManager(context);
        String userEmail = preferencesManager.getUserEmail();
        EditText senderEditText = view.findViewById(R.id.senderEditText);
        EditText receiverEditText = view.findViewById(R.id.receiverEditText);
        senderEditText.setText(userEmail);
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String sender = senderEditText.getText().toString();
            String receiver = receiverEditText.getText().toString();
            listener.onDialogPositiveClick(sender, receiver);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
