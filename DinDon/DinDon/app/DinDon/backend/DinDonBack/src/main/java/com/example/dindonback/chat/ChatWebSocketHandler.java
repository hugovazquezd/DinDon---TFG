package com.example.dindonback.chat;

import com.example.dindonback.DTOs.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final MessageRepository messageRepository;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parsear mensaje JSON
        ObjectMapper mapper = new ObjectMapper();
        Message parsedMessage = mapper.readValue(message.getPayload(), Message.class);

        // Si el mensaje es de inicio de sesión, establecer el nombre de usuario en la sesión del WebSocket
        if (parsedMessage.getSender().equals(parsedMessage.getContent())) {
            String username = parsedMessage.getSender();
            sessions.put(username, session);
            //loadPreviousMessages(parsedMessage.getSender(), parsedMessage.getReceiver());
            return;
        }

        // Guardar el mensaje en la base de datos
        messageRepository.save(parsedMessage);

        // Retransmitir el mensaje a otros usuarios
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession webSocketSession = entry.getValue();
            if (!webSocketSession.getId().equals(session.getId())) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String username = entry.getKey();
            WebSocketSession webSocketSession = entry.getValue();
            if (webSocketSession.getId().equals(session.getId())) {
                sessions.remove(username);
                break;
            }
        }
    }

   /* private void loadPreviousMessages(String sender, String receiver) throws IOException {
        List<Message> previousMessages = messageRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver, receiver, sender);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Loading previous messages...");

        for (Message message : previousMessages) {
            String jsonMessage = mapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(jsonMessage);
            sessions.get(sender).sendMessage(textMessage);
        }
    }*/

}
