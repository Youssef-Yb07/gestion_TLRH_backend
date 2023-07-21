package com.tlrh.gestion_tlrh_backend.service;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Compte;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CompteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompteService {
    private CompteRepository compteRepository;
    private CollaborateurRepository collaborateurRepository;
    public Compte AjouterCompte(String compteEmail){
        Optional<Compte> optionalCompte=compteRepository.findCompteByEmail(compteEmail);
        if(optionalCompte.isPresent()){
            throw new IllegalStateException("Already Exist");
        }
        Compte compte=new Compte(null,compteEmail,generatePassword());
        return compteRepository.save(compte);
    }
    public Compte AccountToCollab(Integer compteId,Integer collaborateurId){
        Optional<Compte> optionalCompte=compteRepository.findById(compteId);
        Optional<Collaborateur> optionalCollab=collaborateurRepository.findById(collaborateurId);
        if (!(optionalCompte.isPresent() && optionalCollab.isPresent())) {
            throw new IllegalArgumentException("Both Compte and Collaborateur entities should be present.");
        }
        Compte compte = optionalCompte.get();
        Collaborateur collaborateur = optionalCollab.get();
        compte.setCollaborateur(collaborateur);
        return compteRepository.save(compte);
    }
    private String generatePassword(){
            String Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            String passwordBuilder ="";
            for (int i = 0; i < 13; i++) {
                int index = (int) (Math.random() * Chars.length());
                passwordBuilder+= Chars.charAt(index);
            }
            return passwordBuilder;
    }
    }



