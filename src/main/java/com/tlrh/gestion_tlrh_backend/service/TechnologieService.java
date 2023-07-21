package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Technologie;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.TechnologieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TechnologieService {

    @Autowired private TechnologieRepository technologieRepository;

    @Autowired private CollaborateurRepository collaborateurRepository;

    public Technologie CreateTechnologie(Technologie technologie){
        Optional<Technologie> optionalTechnologie= Optional.ofNullable(technologieRepository.findTechnologieByNom(technologie.getNom()));
        if(optionalTechnologie.isPresent()){
            throw new IllegalStateException("Technologie already exists");
        }
        else {
            return technologieRepository.save(technologie);
        }
    }
    public Collaborateur affectTechnologiesToCollaborateur(int idTechnologie, int idCollaborateur) {
        Optional<Technologie> optionalTechnologie = technologieRepository.findById(idTechnologie);
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(idCollaborateur);

        if (optionalTechnologie.isPresent() && optionalCollaborateur.isPresent()) {
            Technologie technologie = optionalTechnologie.get();
            Collaborateur collaborateur = optionalCollaborateur.get();

            if (!technologie.getCollaborateurs().contains(collaborateur)) {
                technologie.getCollaborateurs().add(collaborateur);
                technologie = technologieRepository.save(technologie);
                System.out.println("Technologies / Collab ========================>");
                for (Collaborateur c : technologie.getCollaborateurs()) {
                    System.out.println("Collaborateur Id: " + c.getMatricule() + ", Name: " + c.getNom());
                }
            }

            if (!collaborateur.getTechnologies().contains(technologie)) {
                collaborateur.getTechnologies().add(technologie);
                collaborateur = collaborateurRepository.save(collaborateur);
                System.out.println("Collaborateur / Tech ========================>");
                for (Technologie t : collaborateur.getTechnologies()) {
                    System.out.println("Technologie Id: " + t.getId() + ", Name: " + t.getNom());
                }
            }


            return collaborateur;
        }

        throw new IllegalStateException("Technologie or Collaborateur not found");
    }

}
