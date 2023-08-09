package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Diplome;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.DiplomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiplomeService {

    @Autowired
    private DiplomeRepository diplomeRepository;
    @Autowired
    private CollaborateurRepository collaborateurRepository;

    public Diplome createDiplome(Diplome diplome) throws Exception {
        //check if diplome already exist
        Optional<Diplome> existingDiplome = diplomeRepository.findById(diplome.getId());
        if (existingDiplome.isPresent()) {
            throw new Exception("This diploma already exists in the database");
        }

        // save diplome in bd
        return diplomeRepository.save(diplome);
    }

    //  affecte  Diplome to collaborateur
    public Diplome AffectDiplomeToACollaborateur(int  diplomeID, int collaborateurID) {
        Optional<Collaborateur> existingCollaborateur=collaborateurRepository.findById(collaborateurID);
        Optional<Diplome> existingDiplome=diplomeRepository.findById(diplomeID);
        if(!existingCollaborateur.isPresent()){
            throw new RuntimeException("Collaborateur entity doesn't exist !");
        }
        if(!existingDiplome.isPresent()){
            throw new RuntimeException("Diplome entity doesn't exist !");

        }
        Diplome diplome=existingDiplome.get();
        Collaborateur collaborateur=existingCollaborateur.get();
        if (diplome.getCollaborateur()==collaborateur)
        {
            throw new RuntimeException("this diploma is already affected to collaborateur");
        }

        diplome.setCollaborateur(collaborateur);

        System.out.println("diplome to collaboatteur done");

        return diplomeRepository.save(diplome);

    }


}
