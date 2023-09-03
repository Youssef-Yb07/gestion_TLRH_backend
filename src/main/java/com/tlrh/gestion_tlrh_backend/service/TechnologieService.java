package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Technologie;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.TechnologieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            if (!(technologie.getCollaborateurs().contains(collaborateur)&&collaborateur.getTechnologies().contains(technologie))) {
                technologie.getCollaborateurs().add(collaborateur);
                technologie = technologieRepository.save(technologie);
                System.out.println("Technologies / Collab ========================>");
                for (Collaborateur c : technologie.getCollaborateurs()) {
                    System.out.println("Collaborateur Id: " + c.getMatricule() + ", Name: " + c.getNom());
                }
            }

            return collaborateur;
        }

        throw new IllegalStateException("Technologie or Collaborateur not found");
    }


    public Map<String,Double> CalculateTauxTechnologies(){

        List<Collaborateur> collaborateurs=collaborateurRepository.findAll();

        int TotalCollaborateurs=collaborateurs.size();

        Map<String, Double> tauxTechnologies = new HashMap<>();

        for (Collaborateur collaborateur :collaborateurs){
            for (Technologie technologie :collaborateur.getTechnologies()){

                tauxTechnologies.put(technologie.getNom(),tauxTechnologies.getOrDefault(technologie.getNom(),0.0)+1);
            }
        }
        for (Map.Entry<String, Double> tech : tauxTechnologies.entrySet()) {
            double taux = (tech.getValue() / TotalCollaborateurs) * 100;
            tech.setValue(taux);
        }
        return tauxTechnologies;
    }

    public List<Technologie> getAllTechnologies() {
        if (technologieRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Technologies not found");
        } else {
            return technologieRepository.findAll();
        }
    }

    public Technologie getTechnologieById(int id) {
        Optional<Technologie> optionalTechnologie = technologieRepository.findById(id);
        if (optionalTechnologie.isPresent()) {
            return optionalTechnologie.get();
        } else {
            throw new IllegalStateException("Technologie not found");
        }
    }

}
