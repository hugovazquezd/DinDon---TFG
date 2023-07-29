package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Pisos;
import com.example.dindonback.DTOs.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class usersController {
    private final UserRepository usersRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public usersController(UserRepository usersRepository, MongoTemplate mongoTemplate) {
        this.usersRepository = usersRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/{mail}")
    public User getUserByMail(@PathVariable String mail) {
        return usersRepository.findByEmail(mail);
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable String id) {
        return usersRepository.findById(id).orElse(null);
    }

    @GetMapping("/imagenes/{imagen}")
    public ResponseEntity<byte[]> getImagenUsuarios(@PathVariable String imagen) throws IOException {
        String[] extensiones = {".png", ".jpg", ".jpeg"};
        File file = null;
        String rutaBase = "/Users/hugo/Documents/Cuarto/TFG/fotosUsuarios/";

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

    @GetMapping("/busqueda/{busqueda}")
    public boolean isArrendador(@PathVariable String busqueda) {
        Query query = new Query(Criteria.where("_id").is(busqueda).and("TipoAcceso").is("Arrendador"));
        return mongoTemplate.exists(query, User.class);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadImage(@RequestBody Map<String, String> body, @PathVariable String id) {
        String base64Image = body.get("image");
        byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Image);
        try {
            // Aquí elige la ubicación de la imagen en el servidor
            Path destinationFile = Paths.get("/Users/hugo/Documents/Cuarto/TFG/fotosUsuarios/"
                    + id + ".jpg");
            Files.write(destinationFile, decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);
    }


}
