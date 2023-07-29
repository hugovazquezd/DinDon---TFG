package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Pisos;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDePisos extends MongoRepository<Pisos, String> {
    Optional<Pisos> findById(String id);
    List<Pisos> findAll();

    List<Pisos> findByPropietario(String propietario);
}
