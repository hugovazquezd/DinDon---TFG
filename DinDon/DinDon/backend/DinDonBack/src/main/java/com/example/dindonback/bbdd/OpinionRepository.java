package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Opinions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends MongoRepository<Opinions, String> {
}
