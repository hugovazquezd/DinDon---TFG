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
import java.util.List;

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

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadImage(@RequestBody List<String> images, @PathVariable String id) {
        int name = 0;
        for (String base64Image : images) {
            byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Image);
            try {
                Path directory = Paths.get("/Users/hugo/Documents/Cuarto/TFG/fotosPisos/" + id);
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                Path destinationFile = directory.resolve(name + ".jpg");
                Files.write(destinationFile, decodedBytes);
                name++;
            } catch (IOException e) {
                return new ResponseEntity<>("Error al guardar imagen", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Imagenes subidas con éxito", HttpStatus.OK);
    }

    //PARA BORRAR UN PISO
    @DeleteMapping("/{id}")
    public void deletePiso(@PathVariable String id) {
        pisosRepository.deleteById(id);
    }

    //editar un piso
    @PutMapping("/edit/{id}")
    public Pisos editPiso(@RequestBody Pisos nuevoPiso, @PathVariable String id) {
        Pisos piso = pisosRepository.findById(id).orElse(null);
        if (piso != null) {
            piso.setNombre(nuevoPiso.getNombre());
            piso.setPrecio(nuevoPiso.getPrecio());
            piso.setDescripcion(nuevoPiso.getDescripcion());
            piso.setDireccion(nuevoPiso.getDireccion());
            piso.setHabitaciones(nuevoPiso.getHabitaciones());
            piso.setBanos(nuevoPiso.getBanos());
            piso.setServiciosDisponibles(nuevoPiso.getServiciosDisponibles());
            piso.setLatitud(nuevoPiso.getLatitud());
            piso.setLongitud(nuevoPiso.getLongitud());
            return pisosRepository.save(piso);
        } else {
            return null;
        }
    }


}