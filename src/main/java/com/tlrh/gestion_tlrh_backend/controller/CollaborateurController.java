package com.tlrh.gestion_tlrh_backend.controller;

import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.dto.UpdateBy3ActorsDto;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collaborateur")
public class CollaborateurController {

    @Autowired
    private CollaborateurService collaborateurService;

    @PutMapping("/updateBy3Actors")
    public ResponseEntity<Collaborateur> UpdateCollaborateurBy3Actors(@RequestParam Integer matricule,@RequestBody UpdateBy3ActorsDto collaborateurDto) {
        try {
            return new ResponseEntity<>(collaborateurService.updateCollaborateurBy3Actors(matricule, collaborateurDto), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while updating Collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignCollaborateurToManager")
    public ResponseEntity<Collaborateur> assignCollaborateurToManager(@RequestParam Integer collaborateurMatricule, @RequestParam Integer managerMatricule) {
        try {
            return new ResponseEntity<>(collaborateurService.assignCollaborateurToManager(collaborateurMatricule, managerMatricule), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while assigning Collaborateur to Manager");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignCollaborateursToManager")
    public ResponseEntity<List<CollaborateurDto>> assignCollaborateursToManager(
            @RequestParam List<Integer> collaborateurMatricules,
            @RequestParam Integer managerMatricule) {
        try {
            List<CollaborateurDto> updatedCollaborateurs = collaborateurService.assignCollaborateursToManager(collaborateurMatricules, managerMatricule);
            return new ResponseEntity<>(updatedCollaborateurs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/{matricule}/updateByManager")
//    public ResponseEntity<CollaborateurDto> updateCollaborateurByManager(
//            @PathVariable("matricule") Integer matricule,
//            @RequestBody CollaborateurDto collaborateurDto) {
//        try {
//            CollaborateurDto updatedCollaborateur = collaborateurService.updateCollaborateurByManager(matricule, collaborateurDto);
//            return new ResponseEntity<>(updatedCollaborateur, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (IllegalStateException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    //Add random Collaborators for testing purposes
    @PostMapping("/addRandomCollaborateurs")
    public ResponseEntity addRandomCollaborateurs(@RequestParam Integer number) {
        try {
            collaborateurService.addRandomCollaborateurs(number);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while adding Collaborateurs");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //create managerRH
    @PostMapping("/createManagerRH")
    public ResponseEntity<Collaborateur> createManagerRH(@RequestBody CollaborateurDto managerDto) {
    try {
        Collaborateur NewmanagerDto = collaborateurService.createManagerRh(managerDto);
        return new ResponseEntity<>(NewmanagerDto, HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while creating Manager RH");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PutMapping("/ActivateStatusManagerRH")
    public ResponseEntity<Collaborateur> ActivateStatusManagerRH(@RequestParam Integer matricule) {
    try {
        return new ResponseEntity<>(collaborateurService.ActivateStatusManagerRH(matricule), HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while updating Collaborateur");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
    @PutMapping("/DesactivateStatusManagerRH")
    public ResponseEntity<Collaborateur> DesactivateStatusManagerRH(@RequestParam Integer matricule) {
    try {
        return new ResponseEntity<>(collaborateurService.DesactivateStatusManagerRH(matricule), HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while updating Collaborateur");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/get/all")
    public ResponseEntity<List<Collaborateur>> GetAllCollaborateurs(){
        try {
            return new ResponseEntity<>(collaborateurService.getAllCollaborateurs(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/ManagerRH/activated")
    public ResponseEntity<List<Collaborateur>> getManagerRHByStatutActivated(){
        try {
            return new ResponseEntity<>(collaborateurService.getManagerRHByStatutActivated(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/ManagerRH/Desactivated")
    public ResponseEntity<List<Collaborateur>> getManagerRHByStatutDesactivated(){
        try {
            return new ResponseEntity<>(collaborateurService.getManagerRHByStatutDisactivated(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
