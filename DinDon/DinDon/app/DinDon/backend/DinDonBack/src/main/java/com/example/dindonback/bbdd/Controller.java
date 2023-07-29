package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Pisos;
import com.example.dindonback.DTOs.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pisos")
public class Controller {
    private final RepositorioDePisos pisosRepository;
    private final UserRepository userRepository;


    @Autowired
    public Controller(RepositorioDePisos pisosRepository, UserRepository userRepository) {
        this.pisosRepository = pisosRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Pisos> getAllPisos() {
        System.out.println(pisosRepository.findAll());
        return pisosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pisos getPisoById(@PathVariable String id) {
        return pisosRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/{imagen}")
    public ResponseEntity<byte[]> getImagen(@PathVariable String id, @PathVariable String imagen) throws IOException {
        String[] extensiones = {".png", ".jpg", ".jpeg"};
        File file = null;
        String rutaBase = "/Users/hugo/Documents/Cuarto/TFG/fotosPisos/" + id + "/";
        for (String extension : extensiones) {
            file = new File(rutaBase + imagen + extension);
            if (file.exists()) {
                break;
            }
        }
        byte[] imagenBytes = FileUtils.readFileToByteArray(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imagenBytes.length);
        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/propietario/{user}")
    public String getUserById(@PathVariable String user) {
        User usuario = userRepository.findById(user).orElse(null);
        return usuario != null ? usuario.getNombre() : "Usuario no encontrado";
    }

    @GetMapping("/propietario/list/{user}")
    public List<Pisos> getPisosByPropietario(@PathVariable String user) {
        return pisosRepository.findByPropietario(user);
    }

    @PostMapping
    public Pisos addPiso(@RequestBody Pisos nuevoPiso) {
        return pisosRepository.save(nuevoPiso);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestBody List<String> images) {
        for (String base64Image : images) {
            byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Image);
            try {
                // Aquí elige la ubicación de la imagen en el servidor
                Path destinationFile = Paths.get("/Users/hugo/Documents/Cuarto/TFG/fotosPisos/", UUID.randomUUID().toString() + ".jpg");
                Files.write(destinationFile, decodedBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error al guardar imagen", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Imagenes subidas con éxito", HttpStatus.OK);
    }
}