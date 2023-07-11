package com.tlrh.gestion_tlrh_backend.controller;

import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collaborateur")
public class CollaborateurController {

    @Autowired private CollaborateurService collaborateurService;

    @PutMapping("/updateBy3Actors")
    public ResponseEntity<CollaborateurDto> UpdateCollaborateurBy3Actors(@RequestParam Integer matricule,@RequestBody CollaborateurDto collaborateurDto) {
        try {
            return new ResponseEntity(collaborateurService.updateCollaborateurBy3Actors(matricule, collaborateurDto), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while updating Collaborateur");
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
