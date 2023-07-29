package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.User;
import com.example.dindonback.loginsignup.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static com.mongodb.assertions.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class UsersControllerTest {
   private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
     void setUp() {
        testUser = new User();
        testUser.setEmail("testUser@mail.com");
    }

    @Test
     void getUserByMailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(testUser);

        User result = usersController.getUserByMail("testUser@mail.com");

        assertEquals(testUser, result);
    }

    @Test
     void getUserByIdTest() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        User result = usersController.getUserById("1");

        assertEquals(testUser, result);
    }

   @Test
    void isArrendadorTest() {
      Query expectedQuery = new Query(Criteria.where("_id").is("testId").and("tipoAcceso").is("Arrendador"));
      when(mongoTemplate.exists(expectedQuery, User.class)).thenReturn(true);

      boolean result = usersController.isArrendador("testId");

      assertTrue(result);
   }

   @Test
    void uploadImageTest() throws IOException {
      Map<String, String> body = new HashMap<>();
      body.put("image", "testBase64Image");

      ResponseEntity<String> expectedResponse = new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);

      ResponseEntity<String> result = usersController.uploadImage(body, "testId");

      assertEquals(expectedResponse, result);
   }

    @Test
    void editPreferencesTest() {
        // Preparar datos de prueba
        String id = "1";
        List<String> nuevasPreferencias = Arrays.asList("Preferencia 1", "Preferencia 2");
        User user = new User();
        user.setId(id);

        // Configurar los mocks
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Llamar al método a probar
        User result = usersController.editPreferences(nuevasPreferencias, id);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(nuevasPreferencias, result.getPreferencias());
    }

    @Test
    void editPreferencesWhenUserNotFoundTest() {
        // Preparar datos de prueba
        String id = "1";
        List<String> nuevasPreferencias = Arrays.asList("Preferencia 1", "Preferencia 2");

        // Configurar los mocks
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Llamar al método a probar
        User result = usersController.editPreferences(nuevasPreferencias, id);

        // Verificar el resultado
        assertNull(result);
    }


    @Test
     void updateUserTest() {
        // arrange
        User user = new User();
        user.setId("1");
        user.setNombre("oldName");
        user.setEmail("oldEmail@example.com");
        user.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setNombre("newName");
        updatedUser.setEmail("newEmail@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(any())).thenReturn("hashedPassword");

        // act
        ResponseEntity<String> response = authController.updateUser("1", updatedUser);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario actualizado correctamente", response.getBody());
        assertEquals("newName", user.getNombre());
        assertEquals("newEmail@example.com", user.getEmail());
        assertEquals("hashedPassword", user.getPassword());

        verify(userRepository, times(1)).save(user);
    }

    @Test
     void updateUserBTest() {
        // arrange
        User user = new User();
        user.setId("1");

        User updatedUser = new User();
        updatedUser.setFriendsRequests(Arrays.asList("request1", "request2"));
        updatedUser.setFriends(Arrays.asList("friend1", "friend2"));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // act
        ResponseEntity<String> response = authController.updateUserB("1", updatedUser);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario actualizado correctamente", response.getBody());
        assertEquals(updatedUser.getFriendsRequests(), user.getFriendsRequests());
        assertEquals(updatedUser.getFriends(), user.getFriends());

        verify(userRepository, times(1)).save(user);
    }


}
