package com.tlrh.gestion_tlrh_backend.controller;
import com.tlrh.gestion_tlrh_backend.entity.Compte;
import com.tlrh.gestion_tlrh_backend.service.CompteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compte")
@AllArgsConstructor
public class CompteController {
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
    @PostMapping("/addCompte/{email}")
    public ResponseEntity<Compte> AjoutCompte(@PathVariable String email){
        try {
            return new ResponseEntity<>(compteService.AjouterCompte(email), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/CompteToCollab")
    public ResponseEntity<Compte> Affectation(
            @RequestParam Integer compteId,
            @RequestParam Integer collaborateurId){
        try {
            return new ResponseEntity<>(compteService.AccountToCollab(compteId,collaborateurId), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
