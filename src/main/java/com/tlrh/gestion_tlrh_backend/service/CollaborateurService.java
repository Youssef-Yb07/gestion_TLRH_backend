package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CollaborateurService {

    @Autowired private CollaborateurRepository collaborateurRepository;

    @Autowired private ModelMapper modelMapper;

    @Transactional
    public CollaborateurDto updateCollaborateurBy3Actors(Integer matricule, CollaborateurDto collaborateurDto) throws EntityNotFoundException {

        //Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(matricule);

        //Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collaborateur = optionalCollaborateur.get();
            //Update Collaborateur with new values
            collaborateur.setSalaireActuel(collaborateurDto.getSalaireActuel());
            collaborateur.setPosteAPP(collaborateurDto.getPosteAPP());

            //Update Manager RH
            if (collaborateurDto.getManagerRH() != null) {
                //Get Manager RH by matricule
                Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(collaborateurDto.getManagerRH());
                if (optionalManagerRH.isPresent()) {
                    Collaborateur managerRH = optionalManagerRH.get();
                    // Verify if the ID in the DTO corresponds to a collaborateur with the role "Manager RH"
                    if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                        //Affect the old Manager RH to the attribute "AncienManagerRH"
                        collaborateur.setAncienManagerRH(collaborateur.getManagerRH().getPrenomCollaborateur());
                        //Affect the new Manager RH to the attribute "ManagerRH"
                        collaborateur.setManagerRH(managerRH);
                    } else {
                        throw new IllegalStateException("Le Collaborateur sélectionné n'a pas le rôle de Manager RH.");
                    }
                } else {
                    throw new EntityNotFoundException("Manager RH not found");
                }
            }

            //Save Collaborateur updated
            Collaborateur collaborateurUpdated = collaborateurRepository.save(collaborateur);

            //Convert Collaborateur to CollaborateurDto
            CollaborateurDto updatedDto = new CollaborateurDto();
            updatedDto.setMatricule(collaborateurUpdated.getMatricule());
            updatedDto.setSalaireActuel(collaborateurUpdated.getSalaireActuel());
            updatedDto.setManagerRH(collaborateurUpdated.getManagerRH().getMatricule());
            updatedDto.setPosteAPP(collaborateurUpdated.getPosteAPP());

            return updatedDto;
        } else {
            throw new EntityNotFoundException("Collaborateur not found");
        }
    }




}
