package com.example.dindon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dindon.dtofront.Message;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.helpers.ChatHelpers.MessageAdapter;
import com.example.dindon.helpers.ChatHelpers.WebSocketHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class Chat extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private MessageAdapter messageAdapter;
    final List<Message> messageList = new ArrayList<>();
    private String sender;
    private String receiver;
    private String receiverName;
    private WebSocket webSocket;
    @SuppressLint("StaticFieldLeak")
    private static Chat instance;

    public static Chat getInstance() {
        return instance;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    private void saveMessages() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("messages", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messageList);
        editor.putString(sender + "_" + receiver, json);
        editor.apply();
    }

    public void loadMessages() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("messages", Context.MODE_PRIVATE);

        // Crea un identificador único para esta conversación
        String conversationId = sender + "_" + receiver;

        // Obtiene los mensajes de SharedPreferences
        String jsonMessages = sharedPreferences.getString(conversationId, null);

        if (jsonMessages != null) {
            // Utiliza Gson para convertir el JSON a una lista de mensajes
            Gson gson = new Gson();
            Type type = new TypeToken<List<Message>>() {
            }.getType();
            messageList.clear();
            messageList.addAll(gson.fromJson(jsonMessages, type));
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        EditText messageEditText;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Configurar RecyclerView y adaptador
        chatRecyclerView = this.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Referenciar views
        messageEditText = this.findViewById(R.id.messageEditText);
        Button sendButton = this.findViewById(R.id.sendButton);

        // Obtener el nombre del remitente y el destinatario
        sender = getIntent().getStringExtra("user");
        receiver = getIntent().getStringExtra("friendId");
        receiverName = getIntent().getStringExtra("friend");


        // Cargar mensajes previos
        loadMessages();

        setupWebSocket();
        messageAdapter = new MessageAdapter(messageList, sender);
        chatRecyclerView.setAdapter(messageAdapter);

        // Configurar Toolbar
        setupToolbar(this.findViewById(R.id.toolbar));


        // Agregar listener al botón "Enviar"
        sendButton.setOnClickListener(v -> {
            String messageContent = messageEditText.getText().toString();
            if (!messageContent.isEmpty()) {
                addMessage(sender, messageContent, receiver);

                // Enviar el mensaje a través del WebSocket
                JSONObject data = new JSONObject();
                try {
                    data.put("sender", sender);
                    data.put("content", messageContent);
                    data.put("receiver", receiver);
                } catch (JSONException e) {
                    Log.e("Chat", "Error al crear el JSON", e);
                }
                webSocket.send(data.toString());
                saveMessages();

                messageEditText.setText("");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMessage(String sender, String content, String receiver) {
        if (!isFinishing()) {
            this.runOnUiThread(() -> {
                if (!messageExistsInList(sender, content, receiver)) {
                    addNewMessageToList(sender, content, receiver);
                }
            });
        } else {
            Log.e("Chat", "activity none");
        }
    }

    private boolean messageExistsInList(String sender, String content, String receiver) {
        for (Message message : messageList) {
            if (message.getSender().equals(sender) &&
                    message.getContent().equals(content) &&
                    message.getReceiver().equals(receiver)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addNewMessageToList(String sender, String content, String receiver) {
        Message message = new Message(sender, content, receiver);
        messageList.add(message);
        if (messageAdapter != null) {
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            messageAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
        } else {
            Log.e("Chat", "MessageAdapter is null");
        }
    }


    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(icon);
        toolbar.setTitle(receiverName);
        toolbar.setNavigationOnClickListener(v ->
                this.finish()
        );
    }

    private void setupWebSocket() {
        OkHttpClient client = new OkHttpClient();
        String url = "ws://" + CONSTANTS.URL_CHAT + "/chat?username=" + sender + "&receiver=" + receiver;
        Request request = new Request.Builder().url(url).build();
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        webSocket = client.newWebSocket(request, webSocketHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
