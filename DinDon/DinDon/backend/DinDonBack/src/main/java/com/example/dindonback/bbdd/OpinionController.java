package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Opinions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpinionController {

    private final OpinionRepository opinionRepository;

    @Autowired
    public OpinionController(OpinionRepository opinionRepository) {
        this.opinionRepository = opinionRepository;
    }

    @PostMapping("/opinions")
    public Opinions createOpinion(@RequestBody Opinions opinion) {
        return opinionRepository.save(opinion);
    }
}
