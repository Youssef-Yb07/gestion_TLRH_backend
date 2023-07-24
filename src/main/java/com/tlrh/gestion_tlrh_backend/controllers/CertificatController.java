package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Certificat;

import com.tlrh.gestion_tlrh_backend.service.CertificatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificat")
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
    public ResponseEntity affectCertificatsToCollaborateur(@RequestParam int idCertificat, @RequestParam int idCollaborateur){
        try{
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Erreur lors de l'affectation du certificat au collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}