package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Technologie;
import com.tlrh.gestion_tlrh_backend.service.TechnologieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/technologie")
@Tag(name = "Technologie", description = "Gestion des Technologies")
@CrossOrigin(origins = "*")
public class TechnologieController {

    @Autowired private TechnologieService technologieService;
    @PostMapping("/create")
    public ResponseEntity<Technologie> createTechnologie(@RequestBody Technologie technologie){
        try{
            return new ResponseEntity<>(technologieService.CreateTechnologie(technologie), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error while creating Technologie");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/affectTechnologiesToCollaborateur")
    public ResponseEntity<Collaborateur> affectTechnologiesToCollaborateur(@RequestParam int idTechnologie, @RequestParam int idCollaborateur){
        try{
            return new ResponseEntity<>(technologieService.affectTechnologiesToCollaborateur(idTechnologie,idCollaborateur), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error while Affecting Technologie to Collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/calculate/Taux/Technologies")
    public ResponseEntity<Map<String,Double>> CalculateTauxTechnologies(){
        try{
            return new ResponseEntity<>(technologieService.CalculateTauxTechnologies(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error while Calculating Taux Technologies");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<Iterable<Technologie>> getAllTechnologies(){
        try{
            return new ResponseEntity<>(technologieService.getAllTechnologies(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error while getting all Technologies");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/By/Id")
    public ResponseEntity<Technologie> getTechnologieById(@RequestParam int id){
        try{
            return new ResponseEntity<>(technologieService.getTechnologieById(id), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error while getting Technologie by id");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
