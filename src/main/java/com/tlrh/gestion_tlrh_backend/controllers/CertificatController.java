package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Certificat;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.service.CertificatService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certificat")
@Tag(name = "Certificat", description = "Gestion des certifications")
@CrossOrigin(origins = "*")
public class CertificatController {

    @Autowired
    private CertificatService certificatService;
    @PostMapping("/createCertificat")
    public ResponseEntity<Certificat> createCertificat(@RequestBody Certificat certificat){
        try{
            return new ResponseEntity<>(certificatService.createCertificat(certificat), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de la creation du certificat");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/affectCertificatsToCollaborateur")
    public ResponseEntity<Collaborateur > affectCertificatsToCollaborateur(@RequestParam int idCertificat, @RequestParam int idCollaborateur){
        try{
            return new ResponseEntity<>(certificatService.affectCertificatsToCollaborateur(idCertificat,idCollaborateur),HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de l'affectation du certificat au collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/id")
    public ResponseEntity<Certificat> getCertificatById(@RequestParam int id){
        try{
            return new ResponseEntity<>(certificatService.getCertificatById(id),HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de la recuperation du certificat");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Certificat>> getAllCertificats(){
        try{
            return new ResponseEntity<>(certificatService.getAllCertificats(),HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de la recuperation des certificats");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}