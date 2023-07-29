package com.example.dindonback.DTOs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageResponseTest {

    @Test
    void getMessageTest() {
        // Configurar el mensaje de prueba
        String expectedMessage = "Test message";

        // Crear una instancia de MessageResponse con el mensaje de prueba
        MessageResponse messageResponse = new MessageResponse(expectedMessage);

        // Usar el método getMessage() y comprobar que el mensaje obtenido es el esperado
        String actualMessage = messageResponse.getMessage();
        assertEquals(expectedMessage, actualMessage, "El mensaje obtenido debe ser igual al esperado.");
    }

    @Test
    void setMessageTest() {
        // Configurar los mensajes de prueba
        String initialMessage = "Initial message";
        String updatedMessage = "Updated message";

        // Crear una instancia de MessageResponse con el mensaje inicial
        MessageResponse messageResponse = new MessageResponse(initialMessage);

        // Usar el método setMessage() para cambiar el mensaje
        messageResponse.setMessage(updatedMessage);

        // Comprobar que el mensaje ha sido actualizado correctamente
        String actualMessage = messageResponse.getMessage();
        assertEquals(updatedMessage, actualMessage, "El mensaje obtenido debe ser igual al actualizado.");
    }
}
