package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Pisos;
import com.example.dindonback.DTOs.User;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.commons.io.FileUtils;


@ExtendWith(MockitoExtension.class)
class ControllerTest {

    @InjectMocks
    private Controller controller;

    @Mock
    private RepositorioDePisos pisosRepository;

    @Mock
    private UserRepository userRepository;


    private List<Pisos> pisosList;
    private Pisos piso1;

    private Pisos testPiso;
    private User testUser;

    @InjectMocks
    private Controller userController;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        testPiso = new Pisos();
        testPiso.setNombre("Test Piso");

        testUser = new User();
        testUser.setNombre("Test User");

        pisosList = new ArrayList<>();
        piso1 = new Pisos();
        piso1.setid("1");
        Pisos piso2 = new Pisos();
        piso2.setid("2");
        pisosList.add(piso1);
        pisosList.add(piso2);

        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        User user2 = new User();
        user2.setId("2");
        userList.add(user1);
        userList.add(user2);
    }


    @Test
    void getAllPisosTest() {
        when(pisosRepository.findAll()).thenReturn(pisosList);

        List<Pisos> result = controller.getAllPisos();

        assertEquals(pisosList, result);
        verify(pisosRepository, times(1)).findAll();
    }

    @Test
    void getPisoByIdTest() {
        when(pisosRepository.findById("1")).thenReturn(Optional.of(piso1));

        Pisos result = controller.getPisoById("1");

        assertEquals(piso1, result);
        verify(pisosRepository, times(1)).findById("1");
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        String result = controller.getUserById("testUser");
        assertEquals("Test User", result);
    }

    @Test
    void getPisosByPropietarioTest() {
        when(pisosRepository.findByPropietario(anyString())).thenReturn(Collections.singletonList(testPiso));

        List<Pisos> result = controller.getPisosByPropietario("testUser");
        assertEquals(1, result.size());
        assertEquals("Test Piso", result.get(0).getNombre());
    }

    @Test
    void addPisoTest() {
        when(pisosRepository.save(any(Pisos.class))).thenReturn(testPiso);

        Pisos result = controller.addPiso(testPiso);
        assertEquals("Test Piso", result.getNombre());
    }

    @Test
    void editPisoTest() {
        when(pisosRepository.findById(anyString())).thenReturn(Optional.of(testPiso));
        when(pisosRepository.save(any(Pisos.class))).thenReturn(testPiso);

        Pisos updatedPiso = new Pisos();
        updatedPiso.setNombre("Updated Piso");

        Pisos result = controller.editPiso(updatedPiso, "testPiso");

        assertEquals("Updated Piso", result.getNombre());
    }

    @Test
    void editPisoTest_PisoNoExistente() {
        when(pisosRepository.findById(anyString())).thenReturn(Optional.empty());

        Pisos updatedPiso = new Pisos();
        updatedPiso.setNombre("Updated Piso");

        Pisos result = controller.editPiso(updatedPiso, "testPiso");

        assertNull(result);
    }


    @Test
    void testDeletePiso() {
        String id = "1";

        // Aquí no hay interacción con el sistema de archivos, por lo que no necesitamos preparar ningún mock
        controller.deletePiso(id);

        verify(pisosRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetUserById_UserFound() {
        // Creamos un objeto de tipo User
        User user = new User();
        user.setId("1");
        user.setNombre("John Doe");

        // Configuramos el comportamiento del userRepository
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Llamamos al método a probar
        String result = userController.getUserById("1");

        // Verificamos que se devuelva el nombre del usuario correcto
        assertEquals("John Doe", result);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Configuramos el comportamiento del userRepository
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Llamamos al método a probar
        String result = userController.getUserById("1");

        // Verificamos que se devuelva "Usuario no encontrado"
        assertEquals("Usuario no encontrado", result);
    }

}
