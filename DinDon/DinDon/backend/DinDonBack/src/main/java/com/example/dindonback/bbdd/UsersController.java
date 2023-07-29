package com.example.dindonback.bbdd;

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

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserRepository usersRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UsersController(UserRepository usersRepository, MongoTemplate mongoTemplate) {
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
        String[] formatos = {".png", ".jpg", ".jpeg"};
        String rutaBase = "/Users/hugo/Documents/Cuarto/TFG/fotosUsuarios/";
        File fichero = null;

        for (String ext : formatos) {
            fichero = new File(rutaBase + imagen + ext);
            if (fichero.exists()) {
                break;
            }
        }

        if (fichero.exists()) {
            HttpHeaders cabeceras = new HttpHeaders();
            byte[] imagenBytes = FileUtils.readFileToByteArray(fichero);
            cabeceras.setContentType(MediaType.IMAGE_JPEG);
            cabeceras.setContentLength(imagenBytes.length);
            return new ResponseEntity<>(imagenBytes, cabeceras, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/busqueda/{busqueda}")
    public boolean isArrendador(@PathVariable String busqueda) {
        Query query = new Query(Criteria.where("_id").is(busqueda).and("tipoAcceso").is("Arrendador"));
        return mongoTemplate.exists(query, User.class);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadImage(@RequestBody Map<String, String> body, @PathVariable String id) {
        String base64Image = body.get("image");
        byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Image);
        try {
            Path destinationFile = Paths.get("/Users/hugo/Documents/Cuarto/TFG/fotosUsuarios/"
                    + id + ".jpg");
            Files.write(destinationFile, decodedBytes);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al guardar imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public User editPreferences(@RequestBody List<String> nuevasPreferencias, @PathVariable String id) {
        User user = usersRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPreferencias(nuevasPreferencias);
            return usersRepository.save(user);
        } else {
            return null;
        }
    }

}
