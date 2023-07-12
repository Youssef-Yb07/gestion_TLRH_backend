package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.repositories.ArchivageRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class     CollaborateurService {

    @Autowired private CollaborateurRepository collaborateurRepository;

    @Autowired private ModelMapper modelMapper;

    @Autowired private ArchivageRepository archivageRepository;

    @Transactional
    public CollaborateurDto updateCollaborateurBy3Actors(Integer matricule, CollaborateurDto collaborateurDto) throws EntityNotFoundException {

        //Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(matricule);

        //Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collaborateur = optionalCollaborateur.get();
            //set the old values to the Archivage table and save it
            Archivage archivage=new Archivage();
            archivage.setCollaborateur(collaborateur);
            archivage.setDateArchivage(Date.valueOf(LocalDateTime.now().toLocalDate()));
            archivage.setPosteActuel(collaborateur.getPosteActuel());
            archivage.setPosteApp(collaborateur.getPosteAPP());
            archivage.setSalaire(collaborateur.getSalaireActuel());
            archivageRepository.save(archivage);

            //Assign it into association table "Collaborateur_Archivage"
            collaborateur.getArchivages().add(archivage);

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


    @Transactional
public CollaborateurDto assignCollaborateurToManager(Integer collaborateurMatricule, Integer managerMatricule) throws EntityNotFoundException {
    // Get Collaborateur by matricule
    Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(collaborateurMatricule);

    // Check if Collaborateur exists
    if (optionalCollaborateur.isPresent()) {
        Collaborateur collaborateur = optionalCollaborateur.get();

        // Get Manager RH by matricule
        Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(managerMatricule);
        if (optionalManagerRH.isPresent()) {
            Collaborateur managerRH = optionalManagerRH.get();
            // Verify if the collaborator has the role "Manager RH"
            if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                // Assign the HR Manager to the collaborator
                collaborateur.setManagerRH(managerRH);
            } else {
                throw new IllegalStateException("Le Collaborateur sélectionné n'a pas le rôle de Manager RH.");
            }
        } else {
            throw new EntityNotFoundException("Manager RH not found");
        }

        // Save the updated collaborator
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

@Transactional
public void addRandomCollaborateurs(int number) {
    for (int i = 0; i < number; i++) {
        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setMatricule(i);
        collaborateur.setSalaireActuel((int) (Math.random() * 1000));
        collaborateur.setPosteAPP("Poste APP " + i);
        collaborateur.setPosteActuel("Poste actuel " + i);
        collaborateur.setSexe("M");
        collaborateur.setNomCollaborateur("Nom " + i);
        collaborateur.setPrenomCollaborateur("Prenom " + i);
        collaborateur.setAbreviationCollaborateur("Abreviation " + i);
        collaborateur.setAncienManagerRH("Ancien Manager RH " + i);
        collaborateur.setAncien_Collaborateur(false);
        collaborateur.setSeminaireIntegration(false);
        collaborateur.setDate_Embauche(Date.valueOf(LocalDateTime.now().toLocalDate()));
        collaborateur.setDateParticipation(Date.valueOf(LocalDateTime.now().toLocalDate()));
        collaborateur.setDate_Depart(Date.valueOf(LocalDateTime.now().toLocalDate()));
        collaborateur.setMois_BAP("Mois BAP " + i);
        collaborateur.setSite("Site " + i);
        collaborateur.setBU("BU " + i);
        collaborateur.setStatut(StatutManagerRH.Active);
        collaborateurRepository.save(collaborateur);
    }
}

// create a managerRH
@Transactional
public CollaborateurDto CreateManagerRh(CollaborateurDto managerRhDto){
        Collaborateur managerRH= new Collaborateur();
        managerRH.setMatricule(managerRhDto.getMatricule());
        managerRH.setNomCollaborateur(managerRhDto.getNom());
        managerRH.setPrenomCollaborateur(managerRhDto.getPrenom());
        managerRH.setBU(managerRhDto.getBu());
        managerRH.setEmail(managerRhDto.getEmail());
        managerRH.setPassword(managerRhDto.getPassword());
        managerRH.setSite(managerRhDto.getSite());
        managerRH.setSexe(managerRhDto.getSexe());
        managerRH.setPosteAPP(managerRH.getPosteAPP());
        managerRH.setSalaireActuel(managerRhDto.getSalaireActuel());


        //assignation du role ManagerRH au coll
        Role managerRole= new Role();
        managerRole.setRole("ManagerRH");
        managerRH.getRoles().add(managerRole);
        Collaborateur NewmanagerRh=collaborateurRepository.save(managerRH);

        CollaborateurDto NewmanagerRhDto=new CollaborateurDto();
        NewmanagerRhDto.setMatricule(NewmanagerRh.getMatricule());
        NewmanagerRhDto.setNom(NewmanagerRh.getNomCollaborateur());
        NewmanagerRhDto.setPrenom(NewmanagerRh.getPrenomCollaborateur());
        NewmanagerRhDto.setBu(NewmanagerRh.getBU());
        NewmanagerRhDto.setEmail(NewmanagerRh.getEmail());
        NewmanagerRhDto.setPassword(NewmanagerRh.getPassword());
        NewmanagerRhDto.setSexe(NewmanagerRh.getSexe());
        NewmanagerRhDto.setSite(NewmanagerRh.getSite());
        NewmanagerRhDto.setPosteAPP(NewmanagerRh.getPosteAPP());
        NewmanagerRhDto.setSalaireActuel(NewmanagerRh.getSalaireActuel());

        return NewmanagerRhDto;


}


}

