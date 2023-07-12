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

    @PutMapping("/updateBy3A ctors")
    public ResponseEntity<CollaborateurDto> UpdateCollaborateurBy3Actors(@RequestParam Integer matricule,@RequestBody CollaborateurDto collaborateurDto) {
        try {
            return new ResponseEntity(collaborateurService.updateCollaborateurBy3Actors(matricule, collaborateurDto), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while updating Collaborateur");
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignCollaborateurToManager")
    public ResponseEntity<CollaborateurDto> assignCollaborateurToManager(@RequestParam Integer collaborateurMatricule, @RequestParam Integer managerMatricule) {
        try {
            return new ResponseEntity(collaborateurService.assignCollaborateurToManager(collaborateurMatricule, managerMatricule), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while assigning Collaborateur to Manager");
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Add random Collaborators for testing purposes
    @PostMapping("/addRandomCollaborateurs")
    public ResponseEntity addRandomCollaborateurs(@RequestParam Integer number) {
        try {
            collaborateurService.addRandomCollaborateurs(number);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while adding Collaborateurs");
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //create managerRH
@PostMapping("/createManagerRH")
    public ResponseEntity<CollaborateurDto> createManagerRH(@RequestBody CollaborateurDto managerDto) {
    try {
        CollaborateurDto NewmanagerDto = collaborateurService.CreateManagerRh(managerDto);
        return new ResponseEntity<>(NewmanagerDto, HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while creating Manager RH");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}


}
