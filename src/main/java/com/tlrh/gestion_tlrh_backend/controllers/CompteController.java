package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Compte;
import com.tlrh.gestion_tlrh_backend.service.CompteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/compte")
@AllArgsConstructor
@Tag(name = "Compte", description = "Gestion des comptes")
@CrossOrigin(origins = "*")
public class CompteController {

    @Autowired
    private CompteService compteService;
    @GetMapping("/all")
    public ResponseEntity<List<Compte>> GetComptes(){
        try {
            return new ResponseEntity<>(compteService.GetComptes(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/compteWithoutCollab")
    public ResponseEntity<List<Compte>> GetComptesWithoutCollaborateur(){
        try {
            return new ResponseEntity<>(compteService.accountWithoutCollab(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addCompte")
    public ResponseEntity<Compte> AjoutCompte(@RequestParam String email){
        try {
            return new ResponseEntity<>(compteService.AjouterCompte(email), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/AffectCompteToCollab")
    public ResponseEntity<Compte> Affectation(
            @RequestParam Integer compteId,
            @RequestParam Integer collaborateurId)throws MessagingException{
        try {
            return new ResponseEntity<>(compteService.AccountToCollab(compteId,collaborateurId), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/changePassword")
    public Compte ChangePassword(@RequestParam String email,
                                 @RequestParam String OldPassword,
                                 @RequestParam String NewPassword,
                                 @RequestParam String ConfirmedPassword) throws MessagingException {
        return compteService.ChangePassword(email,OldPassword,NewPassword,ConfirmedPassword);
    }



}
