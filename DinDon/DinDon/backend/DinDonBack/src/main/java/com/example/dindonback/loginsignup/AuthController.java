package com.example.dindonback.loginsignup;

import com.example.dindonback.DTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.dindonback.bbdd.UserRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String jwtToken = jwtTokenProvider.generateToken(user.getEmail());
            return ResponseEntity.ok(new LoginResponse(jwtToken, "Login successful", user.getEmail()));
        } else {
            return ResponseEntity.status(401).body(new LoginResponse(null, "Invalid email or password", null));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        User user = new User(signUpRequest.getEmail(),
                bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
        user.setTipoAcceso(signUpRequest.getTipoUsuario());
        user.setNombre(signUpRequest.getName());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // Actualizar los campos de nombre y correo
        existingUser.setNombre(updatedUser.getNombre());
        existingUser.setEmail(updatedUser.getEmail());

        // Actualizar la contraseña solo si se proporciona una nueva contraseña y es distinta a la actual
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()
                && !bCryptPasswordEncoder.matches(updatedUser.getPassword(), existingUser.getPassword())) {
            String hashedPassword = bCryptPasswordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        // Guardar los cambios en la base de datos
        userRepository.save(existingUser);

        return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
    }

    @PutMapping("/B/{id}")
    public ResponseEntity<String> updateUserB(@PathVariable String id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        existingUser.setFriendsRequests(updatedUser.getFriendsRequests());
        existingUser.setFriends(updatedUser.getFriends());

        // Guardar los cambios en la base de datos
        userRepository.save(existingUser);

        return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
    }



}

