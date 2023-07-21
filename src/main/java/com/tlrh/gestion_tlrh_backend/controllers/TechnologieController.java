package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Technologie;
import com.tlrh.gestion_tlrh_backend.service.TechnologieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/technologie")
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

}
