package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Ecole;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.EcoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EcoleService {

    @Autowired
    private EcoleRepository ecoleRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    public Ecole createEcole(Ecole ecole){
        //check if diplome already exist
        Optional<Ecole> existingEcole = ecoleRepository.findById(ecole.getId());
        if (existingEcole.isPresent()) {
            throw new RuntimeException("This school already exists in the database");
        }

        // save diplome in bd
        return ecoleRepository.save(ecole);
    }

    public Ecole affectEcoleToACollaborateur(int  ecoleID, int collaborateurID) {
        Optional<Collaborateur> existingCollaborateur=collaborateurRepository.findById(collaborateurID);
        Optional<Ecole> existingEcole=ecoleRepository.findById(ecoleID);
        if(!existingCollaborateur.isPresent()){
            throw new RuntimeException("Collaborateur entity doesn't exist !");
        }
        if(!existingEcole.isPresent()){
            throw new RuntimeException("Ecole entity doesn't exist !");

        }
        Ecole ecole=existingEcole.get();

        // check if school's diplomas are already affected to the collaborator
        Collaborateur collaborateur=existingCollaborateur.get();
        if (ecole.getDiplomes().stream().allMatch(diplome -> diplome.getCollaborateur()==collaborateur))
        {
            throw new RuntimeException("this school's diplomas are already affected to collaborateur");
        }

        // affect school's diplomas to the collaborator
        ecole.getDiplomes().stream().forEach(diplome -> diplome.setCollaborateur(collaborateur));

        System.out.println("diplome to collaboatteur done");

        return ecoleRepository.save(ecole);
    }
}
