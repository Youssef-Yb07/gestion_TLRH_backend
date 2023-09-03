package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Diplome;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import com.tlrh.gestion_tlrh_backend.service.DiplomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/diplome")
@AllArgsConstructor
@Tag(name = "Diplome", description = "Gestion des diplomes")
@CrossOrigin(origins = "*")
public class DiplomeController {

    @Autowired
    private CollaborateurService collaborateurService;
    @Autowired
    private DiplomeService diplomeService;

    @PostMapping("craeteDiplome")
    public ResponseEntity<Diplome> createDiplome(@RequestBody Diplome diplome) {
        try {
            Diplome createdDiplome = diplomeService.createDiplome(diplome);
            return new ResponseEntity<>(createdDiplome, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/{diplomeId}/collaborateurs/{collaborateurId}")
    public ResponseEntity<Diplome> affectDiplomeToACollaborateur(
            @PathVariable int diplomeId,
            @PathVariable int collaborateurId
    ) {
        try {

            Diplome affectedDiplome = diplomeService.AffectDiplomeToACollaborateur(diplomeId, collaborateurId);
            return new ResponseEntity<>(affectedDiplome, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get/DiplomaRatios")
    public ResponseEntity<Map<String, Double>> getDiplomaRatios() {
        try {
            Map<String, Double> ratios = diplomeService.getDiplomaRatios();
            System.out.println(ratios);
            return new ResponseEntity<>(ratios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/id")
    public ResponseEntity<Diplome> getDiplomeById(@RequestParam int id){
        try{
            return new ResponseEntity<>(diplomeService.getDiplomeById(id),HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de la recuperation du diplome");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<Diplome>> getAllDiplomes(){
        try{
            return new ResponseEntity<>(diplomeService.getAllDiplomes(),HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de la recuperation des diplomes");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
