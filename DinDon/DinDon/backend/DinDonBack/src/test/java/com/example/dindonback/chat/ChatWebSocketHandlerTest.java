package com.example.dindonback.chat;

import com.example.dindonback.DTOs.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatWebSocketHandlerTest {
    @Mock
    MessageRepository messageRepository;

    @Mock
    WebSocketSession session;

    @InjectMocks
    ChatWebSocketHandler chatWebSocketHandler;

    private Map<String, WebSocketSession> sessions;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        sessions = new HashMap<>();
        // Use reflection to set the sessions field in the ChatWebSocketHandler
        Field sessionsField = ChatWebSocketHandler.class.getDeclaredField("sessions");
        sessionsField.setAccessible(true);
        sessionsField.set(chatWebSocketHandler, sessions);
    }

    @Test
    void handleTextMessage() throws Exception {
        // Crear un objeto Message
        Message testMessage = new Message();
        testMessage.setSender("testUser");
        testMessage.setContent("Hello, World!");

        // Crear un ObjectMapper y usarlo para convertir el objeto Message a JSON
        ObjectMapper mapper = new ObjectMapper();
        String messageJson = mapper.writeValueAsString(testMessage);

        // Crear un TextMessage con el JSON
        TextMessage textMessage = new TextMessage(messageJson);

        // Configurar el mock WebSocketSession para que devuelva un ID único cuando se llame a getId()
        when(session.getId()).thenReturn("1");

        // Llamar al método handleTextMessage()
        chatWebSocketHandler.handleTextMessage(session, textMessage);

        // Verificar que se llamó al método save() del repositorio de mensajes
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void handleLoginMessage() throws Exception {
        // Crear un objeto Message
        Message testMessage = new Message();
        testMessage.setSender("testUser");
        testMessage.setContent("testUser"); // Un mensaje de inicio de sesión

        // Crear un ObjectMapper y usarlo para convertir el objeto Message a JSON
        ObjectMapper mapper = new ObjectMapper();
        String messageJson = mapper.writeValueAsString(testMessage);

        // Crear un TextMessage con el JSON
        TextMessage textMessage = new TextMessage(messageJson);

        // Configurar el mock WebSocketSession para que devuelva un ID único cuando se llame a getId()
        when(session.getId()).thenReturn("1");

        // Llamar al método handleTextMessage()
        chatWebSocketHandler.handleTextMessage(session, textMessage);

        // Verificar que no se llamó al método save() del repositorio de mensajes
        verify(messageRepository, times(0)).save(any(Message.class));
    }

    @Test
     void afterConnectionClosed_ShouldRemoveSession() throws Exception {
        // Supongamos que tenemos una sesión con un usuario llamado "usuario1"
        String username = "usuario1";
        sessions.put(username, session);

        // Establece un ID falso para la sesión
        when(session.getId()).thenReturn("123");

        // Llamada al método
        chatWebSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        // Verifica que la sesión fue eliminada del mapa
        assertFalse(sessions.containsKey(username));
    }

    @Test
     void afterConnectionClosed_ShouldNotRemoveSession_WhenDifferentId() throws Exception {
        // Supongamos que tenemos una sesión con un usuario llamado "usuario1"
        String username = "usuario1";
        sessions.put(username, session);

        // Establece un ID falso para la sesión
        when(session.getId()).thenReturn("123");

        // Crea una nueva sesión falsa con un ID diferente
        WebSocketSession differentSession = mock(WebSocketSession.class);
        when(differentSession.getId()).thenReturn("456");

        // Llamada al método
        chatWebSocketHandler.afterConnectionClosed(differentSession, CloseStatus.NORMAL);

        // Verifica que la sesión original no fue eliminada del mapa
        assertTrue(sessions.containsKey(username));
    }

}
