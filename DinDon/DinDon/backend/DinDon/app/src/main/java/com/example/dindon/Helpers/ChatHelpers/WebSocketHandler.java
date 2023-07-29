package com.example.dindon.Helpers.ChatHelpers;


import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.dindon.Chat;
import com.example.dindon.Helpers.CONSTANTS;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHandler extends WebSocketListener {
    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        try {
            JSONObject data = new JSONObject(text);
            String sender = data.getString("sender");
            String content = data.getString("content");
            String receiver = data.getString("receiver");

            Chat.getInstance().addMessage(sender, content, receiver);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onOpen(WebSocket webSocket, @NonNull Response response) {
        // Acciones al abrir la conexión WebSocket:
        String username = webSocket.request().url().queryParameter("username");
        String receiver = webSocket.request().url().queryParameter("receiver");
        JSONObject message = new JSONObject();
        try {
            message.put("sender", username);
            message.put("receiver", receiver);
            message.put("content", username);
            webSocket.send(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(code, null);
    }

    private void reconnectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        String url = "ws://" + CONSTANTS.URL_CHAT + "/chat?username=" + Chat.getInstance().getSender() + "&receiver=" + Chat.getInstance().getReceiver();
        Request request = new Request.Builder().url(url).build();
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        Chat.getInstance().setWebSocket(client.newWebSocket(request, webSocketHandler));
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
        new Handler(Looper.getMainLooper()).postDelayed(this::reconnectWebSocket, 5000);
    }

}

