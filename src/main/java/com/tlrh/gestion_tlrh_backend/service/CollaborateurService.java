package com.tlrh.gestion_tlrh_backend.service;
import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.dto.UpdateBy3ActorsDto;
import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.repositories.ArchivageRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class     CollaborateurService {

    @Autowired private CollaborateurRepository collaborateurRepository;

//    @Autowired
//    private ModelMapper modelMapper;
    @Autowired
    private EmailsService emailsService;

    @Autowired private ArchivageRepository archivageRepository;

    @Transactional
    public Collaborateur updateCollaborateurBy3Actors(Integer matricule, UpdateBy3ActorsDto collaborateurDto) throws EntityNotFoundException {

        //Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(matricule);

        //Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collaborateur = optionalCollaborateur.get();
            //set the old values to the Archivage table and save it
            Archivage archivage=new Archivage();
            archivage.setCollaborateur(collaborateur);
            archivage.setDateArchivage(new Date(System.currentTimeMillis()));
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

            return collaborateurUpdated;
        } else {
            throw new EntityNotFoundException("Collaborateur not found");
        }
    }

    public void SendMails(Collaborateur collaborateur) throws MessagingException {
        String collaborateurMail=collaborateur.getEmail();
        Collaborateur manager=collaborateur.getManagerRH();
        if(manager!=null) {
            String managerMail = manager.getEmail();
            String sexe = manager.getSexe().equals("Female") ? "Mrs" : "Mr";
            emailsService.SendEmail(managerMail,
                    "Hi Dear Your new Employee is : " + collaborateur.getPrenomCollaborateur() +
                            " " + collaborateur.getNomCollaborateur() + " the email is : " + collaborateurMail,
                    "New Employee ");
        }
        emailsService.SendEmail(collaborateurMail,
                "Hi Dear welcome to SQLI . " ,
                " SQLI ");
    }
    @Scheduled(cron = "0 25 20 * * *")
    public void SendInvitations() throws MessagingException {
        for (Collaborateur collaborateur:CollaborateurAfterX(3)) {
            Collaborateur manager=collaborateur.getManagerRH();
            if(manager!=null) {
                emailsService.SendEmail(manager.getEmail(),
                        "Bilan de periode d'essai pour : " + collaborateur.getPrenomCollaborateur() + " " + collaborateur.getNomCollaborateur()
                        , "Bilan de periode d'essai");
            }
        }
        for (Collaborateur collaborateur:CollaborateurAfterX(6)) {
            Collaborateur manager=collaborateur.getManagerRH();
            if(manager!=null) {
                emailsService.SendEmail(manager.getEmail(),
                        "BIP Bilan intermediaire de performance : " + collaborateur.getPrenomCollaborateur()
                                + " " + collaborateur.getNomCollaborateur()
                        , "Bilan de periode d'essai");
            }
        }
        for (Collaborateur collaborateur:CollaborateurAfterX(12)) {
            Collaborateur manager = collaborateur.getManagerRH();
            if (manager != null) {
                emailsService.SendEmail(manager.getEmail(),
                        "Bilan Annuel de performance Pour : " + collaborateur.getPrenomCollaborateur()
                                + " " + collaborateur.getNomCollaborateur()
                        , "BAP");
            }
        }
    }

    private List<Collaborateur> CollaborateurAfterX(int amount){
        List<Collaborateur> collaborateurs=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        for (Collaborateur collaborateur:collaborateurRepository.findAll()) {
            calendar.setTime(collaborateur.getDate_Embauche());
            calendar.add(Calendar.MONTH, amount);
            Date afterXMonths=new Date(calendar.getTimeInMillis());
            if(isTheSame(new Date(System.currentTimeMillis()),afterXMonths,amount)){
                collaborateurs.add(collaborateur);
            }
        }
        return collaborateurs;
    }
    private boolean isTheSame(Date date1,Date date2,int amount){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && (amount==3 || amount==6))
            return true;
        if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && amount==12 )
            return true;
        return false;
    }

    @Transactional
    public Collaborateur assignCollaborateurToManager(Integer collaborateurMatricule, Integer managerMatricule) throws EntityNotFoundException {
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

            return collaborateurUpdated;
        } else {
            throw new EntityNotFoundException("Collaborateur not found");
        }
    }
    @Transactional
    public List<CollaborateurDto> assignCollaborateursToManager(List<Integer> collaborateurMatricules, Integer managerMatricule) throws EntityNotFoundException {
        // Get Manager RH by matricule
        Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(managerMatricule);
        if (optionalManagerRH.isPresent()) {
            Collaborateur managerRH = optionalManagerRH.get();
            // Verify if the manager has the role "Manager RH"
            if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                // Assign the HR Manager to the collaborators
                List<Collaborateur> collaborateurs = collaborateurRepository.findAllById(collaborateurMatricules);
                collaborateurs.forEach(collaborateur -> collaborateur.setManagerRH(managerRH));
                List<Collaborateur> updatedCollaborateurs = collaborateurRepository.saveAll(collaborateurs);

                // Convert updated collaborators to CollaborateurDto list
                List<CollaborateurDto> updatedDtos = updatedCollaborateurs.stream()
                        .map(collaborateur -> {
                            CollaborateurDto dto = new CollaborateurDto();
                            dto.setMatricule(collaborateur.getMatricule());
                            dto.setSalaireActuel(collaborateur.getSalaireActuel());
                            dto.setManagerRH(collaborateur.getManagerRH().getMatricule());
                            dto.setPosteAPP(collaborateur.getPosteAPP());
                            return dto;
                        })
                        .collect(Collectors.toList());

                return updatedDtos;
            } else {
                throw new IllegalStateException("Le Manager sélectionné n'a pas le rôle de Manager RH.");
            }
        } else {
            throw new EntityNotFoundException("Manager RH not found");
        }
    }
//    @Transactional
//    public CollaborateurDto updateCollaborateurByManager(Integer collaborateurMatricule, CollaborateurDto collaborateurDto) throws EntityNotFoundException {
//        // Get Collaborateur by matricule
//        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(collaborateurMatricule);
//
//        // Check if Collaborateur exists
//        if (optionalCollaborateur.isPresent()) {
//            Collaborateur collaborateur = optionalCollaborateur.get();
//
//            //set the old values to the Archivage table and save it
//            Archivage archivage=new Archivage();
//            archivage.setCollaborateur(collaborateur);
//            archivage.setDateArchivage(Date.valueOf(LocalDateTime.now().toLocalDate()));
//            archivage.setPosteActuel(collaborateur.getPosteActuel());
//            archivage.setPosteApp(collaborateur.getPosteAPP());
//            archivage.setSalaire(collaborateur.getSalaireActuel());
//            archivageRepository.save(archivage);
//
//            //Assign it into association table "Collaborateur_Archivage"
//            collaborateur.getArchivages().add(archivage);
//
//            // Update Collaborateur with new values
//            collaborateur.setSalaireActuel(collaborateurDto.getSalaireActuel());
//            collaborateur.setPosteAPP(collaborateurDto.getPosteAPP());
//
//            // Update Manager RH
//            if (collaborateurDto.getManagerRH() != null) {
//                // Get Manager RH by matricule
//                Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(collaborateurDto.getManagerRH());
//                if (optionalManagerRH.isPresent()) {
//                    Collaborateur managerRH = optionalManagerRH.get();
//                    // Verify if the collaborator has the role "Manager RH"
//                    if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
//                        // Assign the HR Manager to the collaborator
//                        collaborateur.setManagerRH(managerRH);
//                    } else {
//                        throw new IllegalStateException("Le Collaborateur sélectionné n'a pas le rôle de Manager RH.");
//                    }
//                } else {
//                    throw new EntityNotFoundException("Manager RH not found");
//                }
//            }
//
//            // Save the updated collaborator
//            Collaborateur collaborateurUpdated = collaborateurRepository.save(collaborateur);
//
//            // Convert Collaborateur to CollaborateurDto
//            CollaborateurDto updatedDto = modelMapper.map(collaborateurUpdated, CollaborateurDto.class);
//
//            return updatedDto;
//        } else {
//            throw new EntityNotFoundException("Collaborateur not found");
//        }
//    }


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
public CollaborateurDto createManagerRh(CollaborateurDto managerDto) throws IllegalStateException {
    // Check if the coll already exists in bd
    Optional<Collaborateur> existingCollaborateur = collaborateurRepository.findById(managerDto.getMatricule());
    if (existingCollaborateur.isPresent()) {
        Collaborateur collaborateur = existingCollaborateur.get();
        // Check if the collaborator already has the "Manager RH" role
        boolean hasManagerRHRole = collaborateur.getRoles().stream()
                .anyMatch(role -> role.getRole().equals("Manager RH"));
        if (hasManagerRHRole) {
            throw new IllegalStateException("Collaborator already has the Manager RH role.");
        }

        // Assign the "Manager RH" role to the coll
        Role managerRole = new Role();
        managerRole.setRole("Manager RH");
        collaborateur.getRoles().add(managerRole);
        collaborateurRepository.save(collaborateur);

        // Convert the coll to CollDto
        CollaborateurDto collaborateurDto = new CollaborateurDto();
        collaborateurDto.setMatricule(collaborateur.getMatricule());
        collaborateurDto.setNom(collaborateur.getNomCollaborateur());
        collaborateurDto.setPrenom(collaborateur.getPrenomCollaborateur());
        collaborateurDto.setSexe(collaborateur.getSexe());
        collaborateurDto.setSite(collaborateur.getSite());
        collaborateurDto.setBu(collaborateur.getBU());
        collaborateurDto.setEmail(collaborateur.getEmail());
        collaborateurDto.setPassword(collaborateur.getPassword());
        collaborateurDto.setSalaireActuel(collaborateur.getSalaireActuel());
        collaborateurDto.setPosteAPP(collaborateur.getPosteAPP());

        return collaborateurDto;
    } else {
        throw new IllegalStateException("Collaborator does not exist.");
    }
}



}

