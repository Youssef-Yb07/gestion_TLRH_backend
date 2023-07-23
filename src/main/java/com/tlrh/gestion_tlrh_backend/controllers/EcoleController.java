package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Ecole;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import com.tlrh.gestion_tlrh_backend.service.EcoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ecole")
@AllArgsConstructor
public class EcoleController {

    @Autowired
    private EcoleService ecoleService;

    @Autowired
    private CollaborateurService collaborateurService;

    @PostMapping("createEcole")
    public ResponseEntity<Ecole> createEcole(@RequestBody Ecole ecole) {
        try {
            Ecole createdEcole = ecoleService.createEcole(ecole);
            return new ResponseEntity<>(createdEcole, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{ecoleId}/collaborateurs/{collaborateurId}")
    public ResponseEntity<Ecole> affectEcoleToACollaborateur(
            @PathVariable int ecoleId,
            @PathVariable int collaborateurId
    ) {
        try {

            Ecole affectedEcole = ecoleService.affectEcoleToACollaborateur(ecoleId, collaborateurId);
            return new ResponseEntity<>(affectedEcole, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
