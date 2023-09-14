package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.dto.UpdateBy3ActorsDto;
import com.tlrh.gestion_tlrh_backend.entity.*;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import com.tlrh.gestion_tlrh_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborateurService {

    @Autowired
    private CollaborateurRepository collaborateurRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DiplomeRepository diplomeRepository;
    @Autowired
    private TechnologieRepository technologieRepository;

    // @Autowired
    // private ModelMapper modelMapper;
    @Autowired
    private EmailsService emailsService;

    @Autowired
    private ArchivageRepository archivageRepository;
    @Autowired
    private ArchivageService archivageService;

    @Transactional
    public Collaborateur updateCollaborateurBy3Actors(Integer matricule, UpdateBy3ActorsDto collaborateurDto)
            throws EntityNotFoundException, MessagingException {
        // Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(matricule);
        // Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collaborateur = optionalCollaborateur.get();
            // set the old values to the Archivage table and save it
            Archivage archivage = new Archivage();
            archivage.setCollaborateur(collaborateur);
            archivage.setDateArchivage(new Date(System.currentTimeMillis()));
            archivage.setPosteActuel(collaborateur.getPosteActuel());
            archivage.setPosteApp(collaborateur.getPosteAPP());
            archivage.setSalaire(collaborateur.getSalaireActuel());
            archivageRepository.save(archivage);
            // Initialize the archivages list if it is null
            if (collaborateur.getArchivages() == null) {
                collaborateur.setArchivages(new ArrayList<>());
            }
            // Assign it into association table "Collaborateur_Archivage"
            collaborateur.getArchivages().add(archivage);
            // Update Collaborateur with new values
            collaborateur.setSalaireActuel(collaborateurDto.getSalaireActuel());
            collaborateur.setPosteAPP(collaborateurDto.getPosteAPP());
            // Update Manager RH
            if (collaborateurDto.getManagerRH() != null) {
                // Get Manager RH by matricule
                Optional<Collaborateur> optionalManagerRH = collaborateurRepository
                        .findById(collaborateurDto.getManagerRH());
                if (optionalManagerRH.isPresent()) {
                    Collaborateur managerRH = optionalManagerRH.get();
                    // Verify if the ID in the DTO corresponds to a collaborateur with the role
                    // "Manager RH"
                    if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                        // Affect the old Manager RH to the attribute "AncienManagerRH"
                        collaborateur.setAncienManagerRH(collaborateur.getManagerRH().getPrenom());
                        // Affect the new Manager RH to the attribute "ManagerRH"
                        collaborateur.setManagerRH(managerRH);
                        AffectationEmails(collaborateur);

                    } else {
                        throw new IllegalStateException("Le Collaborateur sélectionné n'a pas le rôle de Manager RH.");
                    }
                } else {
                    throw new EntityNotFoundException("Manager RH not found");
                }
            }
            // Save Collaborateur updated
            collaborateurRepository.save(collaborateur);

            return collaborateur;
        } else {
            throw new EntityNotFoundException("Collaborateur not found");
        }
    }

    public void AffectationEmails(Collaborateur collaborateur) throws MessagingException {
        String collaborateurMail = collaborateur.getEmail();
        Collaborateur manager = collaborateur.getManagerRH();
        if (manager != null) {
            String managerMail = manager.getEmail();
            String sexe = manager.getSexe().equals("Female") ? "Mrs" : "Mr";
            emailsService.SendEmail(managerMail,
                    "Hi Dear Your new Employee is : " + collaborateur.getPrenom() +
                            " " + collaborateur.getNom() + " the email is : " + collaborateurMail +
                            " " + collaborateur.getNom() + " the email is : " + collaborateurMail
                            + "with the Id : " + collaborateur.getMatricule() + " . The Programme will be : " +
                            "-After 3 months : BPE . -After 6 months : BIP. -After 12 months : BAP ",
                    "New Employee ");

            emailsService.SendEmail(collaborateurMail,
                    "Hi Dear Your new Manager Rh is : " + manager.getPrenom() +
                            " " + manager.getNom() + " the email is : " + managerMail,
                    "New Manager RH  ");
        }
    }

    private void WelcomeEmail(Collaborateur collaborateur) throws MessagingException {
        String collaborateurMail = collaborateur.getEmail();
        emailsService.SendEmail(collaborateurMail,
                "Hi Dear " + collaborateur.getPrenom() + " " + collaborateur.getNom()
                        + "welcome to SQLI .  " + " Your ID is : " + collaborateur.getMatricule()
                        + " The date of integration is : " + collaborateur.getDate_Embauche()
                        + "The period will be 3 to 5 months , if it's 5 months you will be declared in the second months "
                        +
                        "Thank you and welcome another time to SQLI . ",
                " SQLI ");
        for (Collaborateur collabs : collaborateurRepository.findAll()) {
            if (collabs.getRoles().stream().anyMatch(col -> col.getRole().equals("Ambassadeur RH"))) {
                emailsService.SendEmail(collaborateurMail,
                        "Hi Dear " + collabs.getPrenom() + " " + collabs.getNom()
                                + " . a new employee has integrated SQLI the name is :" + collaborateur.getNom() + " "
                                + collaborateur.getPrenom()
                                + "with the Id : " + collaborateur.getMatricule() + " .",
                        " New Employee ");
            }
        }
    }

    @Scheduled(cron = "0 24 20 * * *")
    public void SendInvitations() throws MessagingException {
        for (Collaborateur collaborateur : CollaborateurAfterX(2)) {
            Collaborateur manager = collaborateur.getManagerRH();
            if (manager != null) {
                emailsService.SendEmail(manager.getEmail(),
                        "Rappel Bilan de periode d'essai pour : " + collaborateur.getPrenom() + " "
                                + collaborateur.getNom()
                                + " Avec numero de Matricule " + collaborateur.getMatricule()
                                + " If you want to make it 5 months ," +
                                " it's the time to let the employee know it .",
                        "Bilan de periode d'essai");
            }
        }
        for (Collaborateur collaborateur : CollaborateurAfterX(3)) {
            Collaborateur manager = collaborateur.getManagerRH();
            if (manager != null) {
                emailsService.SendEmail(manager.getEmail(),
                        "Rappel Bilan de periode d'essai pour : " + collaborateur.getPrenom() + " "
                                + collaborateur.getNom()
                                + " Avec numero de Matricule " + collaborateur.getMatricule(),
                        "Bilan de periode d'essai");
            }
        }
        for (Collaborateur collaborateur : CollaborateurAfterX(6)) {
            Collaborateur manager = collaborateur.getManagerRH();
            if (manager != null) {
                emailsService.SendEmail(manager.getEmail(),
                        "BIP Bilan intermediaire de performance : " + collaborateur.getPrenom()
                                + " " + collaborateur.getNom() + " With the Id : " + collaborateur.getMatricule(),
                        "Bilan de periode d'essai");
            }
        }
        for (Collaborateur collaborateur : CollaborateurAfterX(12)) {
            Collaborateur manager = collaborateur.getManagerRH();
            if (manager != null) {
                emailsService.SendEmail(manager.getEmail(),
                        "Bilan Annuel de performance Pour : " + collaborateur.getPrenom()
                                + " " + collaborateur.getNom() + " With the Id : " + collaborateur.getMatricule(),
                        "BAP");
            }
        }
    }

    private List<Collaborateur> CollaborateurAfterX(int amount) {
        List<Collaborateur> collaborateurs = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Collaborateur collaborateur : collaborateurRepository.findAll()) {
            calendar.setTime(collaborateur.getDate_Embauche());
            calendar.add(Calendar.MONTH, amount);
            Date afterXMonths = new Date(calendar.getTimeInMillis());
            if (isTheSame(new Date(System.currentTimeMillis()), afterXMonths, amount)) {
                collaborateurs.add(collaborateur);
            }
        }
        return collaborateurs;
    }

    private boolean isTheSame(Date date1, Date date2, int amount) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && (amount == 3 || amount == 6))
            return true;
        if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && amount == 12)
            return true;
        return false;
    }

    @Transactional
    public Collaborateur assignCollaborateurToManager(Integer collaborateurMatricule, Integer managerMatricule)
            throws EntityNotFoundException, MessagingException {
        // Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(collaborateurMatricule);

        // Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collaborateur = optionalCollaborateur.get();

            // Get Manager RH by matricule
            Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(managerMatricule);
            if (optionalManagerRH.isPresent()) {
                Collaborateur managerRH = optionalManagerRH.get();
                // Verify if the collaborator status is "Active"
                if (managerRH.getStatut().equals(StatutManagerRH.Active)) {
                    // Verify if the collaborator has the role "Manager RH"
                    if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                        // Assign the HR Manager to the collaborator
                        collaborateur.setManagerRH(managerRH);
                        AffectationEmails(collaborateur);

                    } else {
                        throw new IllegalStateException("Le Collaborateur sélectionné n'a pas le rôle de Manager RH.");
                    }
                } else {
                    throw new IllegalStateException("Le Collaborateur sélectionné n'est pas actif.");
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
    public List<Collaborateur> assignCollaborateursToManager(List<Integer> collaborateurMatricules,
            Integer managerMatricule) throws EntityNotFoundException {
        // Get Manager RH by matricule
        Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(managerMatricule);
        if (optionalManagerRH.isPresent()) {
            Collaborateur managerRH = optionalManagerRH.get();

            // Verify if the collaborator status is "Active"
            if (managerRH.getStatut().equals(StatutManagerRH.Active)) {
                // Verify if the manager has the role "Manager RH"
                if (managerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                    // Assign the HR Manager to the collaborators
                    List<Collaborateur> collaborateurs = collaborateurRepository.findAllById(collaborateurMatricules);
                    collaborateurs.forEach(collaborateur -> {
                        collaborateur.setManagerRH(managerRH);
                        try {
                            AffectationEmails(collaborateur);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    List<Collaborateur> updatedCollaborateurs = collaborateurRepository.saveAll(collaborateurs);

                    return updatedCollaborateurs;

                } else {
                    throw new IllegalStateException("Le Manager sélectionné n'a pas le rôle de Manager RH.");
                }
            } else {
                throw new IllegalStateException("Le Collaborateur sélectionné n'est pas actif.");
            }
        } else {
            throw new EntityNotFoundException("Manager RH not found");
        }
    }

    @Transactional
    public Collaborateur updateCollaborateurByManager(Integer collaborateurMatricule, Collaborateur collaborateur)
            throws EntityNotFoundException {
        // Get Collaborateur by matricule
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(collaborateurMatricule);

        // Check if Collaborateur exists
        if (optionalCollaborateur.isPresent()) {
            Collaborateur collab = optionalCollaborateur.get();
            collaborateur.setMatricule(collab.getMatricule());

            // set the old values to the Archivage table and save it
            Archivage archivage = new Archivage();
            archivage.setCollaborateur(collab);
            archivage.setDateArchivage(Date.valueOf(LocalDateTime.now().toLocalDate()));
            archivage.setPosteActuel(collab.getPosteActuel());
            archivage.setPosteApp(collab.getPosteAPP());
            archivage.setSalaire(collab.getSalaireActuel());
            archivageRepository.save(archivage);

            // Initialize the archivages list if it is null
            if (collaborateur.getArchivages() == null) {
                collaborateur.setArchivages(new ArrayList<>());
            }
            // Assign it to the association table "Collaborateur_Archivage"
            collaborateur.getArchivages().add(archivage);

            // Save the updated collaborator
            collaborateurRepository.save(collaborateur);

            return collaborateur;

        } else {
            throw new EntityNotFoundException("Collaborateur not found");
        }
    }

    // create a managerRH
    public Collaborateur createManagerRh(Integer matricule) throws IllegalStateException, MessagingException {
        // Check if the coll already exists in bd
        Optional<Collaborateur> existingCollaborateur = collaborateurRepository.findById(matricule);
        if (existingCollaborateur.isPresent()) {
            Collaborateur collaborateur = existingCollaborateur.get();
            // Check if the collaborator already has the "Manager RH" role
            boolean hasManagerRHRole = collaborateur.getRoles().stream()
                    .anyMatch(role -> role.getRole().equals("Manager RH"));
            if (hasManagerRHRole) {
                throw new IllegalStateException("Collaborator already has the Manager RH role.");
            }
            // Assign the "Manager RH" role to the coll
            collaborateur.setStatut(StatutManagerRH.Active);
            Role managerRole = roleService.findByRole("Manager RH");
            collaborateur.getRoles().add(managerRole);
            collaborateurRepository.save(collaborateur);
            WelcomeEmail(collaborateur);

            return collaborateur;
        } else {
            throw new IllegalStateException("Collaborator does not exist.");
        }
    }

    public Collaborateur createCollaborateur(Collaborateur collab) throws IllegalStateException, MessagingException {
        // Vérifie si le collaborateur existe déjà dans la base de données
        Optional<Collaborateur> existingCollaborateur = collaborateurRepository.findById(collab.getMatricule());
        if (existingCollaborateur.isPresent()) {
            throw new IllegalStateException("Le collaborateur existe déjà.");
        } else {
            // Crée une nouvelle instance de collaborateur
            Collaborateur collaborateur = new Collaborateur();
            collaborateur.setMatricule(collab.getMatricule());
            collaborateur.setNom(collab.getNom());
            collaborateur.setEmail(collab.getEmail());
            collaborateur.setPrenom(collab.getPrenom());
            collaborateur.setSexe(collab.getSexe());
            collaborateur.setSite(collab.getSite());
            collaborateur.setAbreviationCollaborateur(collab.getAbreviationCollaborateur());
            collaborateur.setBU(collab.getBU());
            collaborateur.setDate_Embauche(collab.getDate_Embauche());
            collaborateur.setMois_BAP(collab.getMois_BAP());
            collaborateur.setEmail(collab.getEmail());
            collaborateur.setSalaireActuel(collab.getSalaireActuel());
            collaborateur.setPosteActuel(collab.getPosteActuel());
            collaborateur.setPosteAPP(collab.getPosteAPP());
            collaborateur.setAncienManagerRH(collab.getAncienManagerRH());
            collaborateur.setManagerRH(collab.getManagerRH());
            collaborateur.setDateParticipation(collab.getDateParticipation());
            collaborateur.setRoles(collab.getRoles());
            collaborateur.setDiplomes(collab.getDiplomes());
            collaborateur.setTechnologies(collab.getTechnologies());

            if(collaborateur.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))){
                collaborateur.setStatut(StatutManagerRH.Active);
            }
            // Enregistre le collaborateur dans la base de donnée
                collaborateurRepository.save(collaborateur);
            WelcomeEmail(collaborateur);
            AffectationEmails(collaborateur);
            return collaborateur;
        }
    }

    @Transactional
    public Collaborateur ActivateStatusManagerRH(Integer idManagerRH) {
        Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(idManagerRH);
        if (optionalManagerRH.isPresent()) {
            Collaborateur ManagerRH = optionalManagerRH.get();
            if (ManagerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                ManagerRH.setStatut(StatutManagerRH.Active);
                collaborateurRepository.save(ManagerRH);
                return ManagerRH;
            } else {
                throw new IllegalStateException("Collaborator does not have the Manager RH role.");
            }
        } else {
            throw new IllegalStateException("Collaborator does not exist.");
        }
    }

    @Transactional
    public Collaborateur DesactivateStatusManagerRH(Integer idManagerRH) {
        Optional<Collaborateur> optionalManagerRH = collaborateurRepository.findById(idManagerRH);
        if (optionalManagerRH.isPresent()) {
            Collaborateur ManagerRH = optionalManagerRH.get();
            if (ManagerRH.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                ManagerRH.setStatut(StatutManagerRH.Desactive);
                collaborateurRepository.save(ManagerRH);
                return ManagerRH;
            } else {
                throw new IllegalStateException("Collaborator does not have the Manager RH role.");
            }
        } else {
            throw new IllegalStateException("Collaborator does not exist.");
        }
    }

    public List<Collaborateur> getAllCollaborateurs() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        return collaborateurs;
    }
    public Collaborateur GetCollaborateurById(Integer id){
        Collaborateur collaborateur = collaborateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not exist with id :" + id));
        return collaborateur;
    }

    public List<Collaborateur> getNonAffectedCollabs() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        List<Collaborateur> nonAffectedCollabs = new ArrayList<>();
        for (Collaborateur collaborateur : collaborateurs) {
            if (collaborateur.getManagerRH() == null) {
                nonAffectedCollabs.add(collaborateur);
            }
        }
        return nonAffectedCollabs;
    }

    public List<Collaborateur> getManagerRHByStatutActivated() {
        // check if the collaborators has a role "Manager RH"
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        List<Collaborateur> ActivatedManagers = new ArrayList<>();
        for (Collaborateur collaborateur : collaborateurs) {
            if (collaborateur.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                if (collaborateur.getStatut().equals(StatutManagerRH.Active)) {
                    ActivatedManagers.add(collaborateur);
                }
            }
        }

        if (ActivatedManagers.isEmpty()) {
            System.err.println("No Manager RH Active found.");
        }
        return ActivatedManagers;
    }

    public List<Collaborateur> getManagerRHByStatutDisactivated() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        List<Collaborateur> DesactivatedManagers = new ArrayList<>();
        for (Collaborateur collaborateur : collaborateurs) {
            if (collaborateur.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                if (collaborateur.getStatut().equals(StatutManagerRH.Desactive)) {
                    DesactivatedManagers.add(collaborateur);
                }
            }
        }
        if (DesactivatedManagers.isEmpty()) {
            System.err.println("No Manager RH Desactive found.");
        }
        return DesactivatedManagers;
    }

    public List<Collaborateur> saveCollaborateursToDatabase(MultipartFile file) {
        List<Collaborateur> collaborateurs = new ArrayList<>();

        if (ExcelService.isValidExcelFile(file)) {
            try {
                collaborateurs = ExcelService.getCollaborateursDataFromExcel(file.getInputStream());
                this.collaborateurRepository.saveAll(collaborateurs);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }

        return collaborateurs;
    }

    public List<Collaborateur> getManagerWithoutAccount() {
        List<Collaborateur> managerRh = this.getAllManagerRH();
        List<Collaborateur> managers = new ArrayList<>();
        for (Collaborateur manager : managerRh) {
            if (manager.getCompte() == null) {
                managers.add(manager);
            }
        }
        return managers;
    }
    public List<Collaborateur> getCollabWithoutAccount() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findCollaborateursByCompteIsNull();

        return collaborateurs;
    }
    public List<Collaborateur> getAllManagerRH() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        List<Collaborateur> managers = new ArrayList<>();
        for (Collaborateur collaborateur : collaborateurs) {
            if (collaborateur.getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))) {
                managers.add(collaborateur);
            }
        }
        return managers;
    }

    public List<Collaborateur> getNonManagerRH() {
        Role role = roleService.findByRole("Manager RH");
        return collaborateurRepository.findCollaborateursByRolesNotContaining(role);
    }

    public double FemaleRatio() {
        int totalFemales = 0;
        int totalMales = 0;
        double ratio;

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        for (Collaborateur collaborateur : collaborateurs) {
            String sexe = collaborateur.getSexe().toLowerCase();

            if ("f".equals(sexe) || "femme".equals(sexe)) {
                totalFemales++;
            } else if ("m".equals(sexe) || "homme".equals(sexe)) {
                totalMales++;
            }
        }

        if (totalFemales == 0 && totalMales == 0) {
            return 0.0;
        }
        ratio = ((double) totalFemales / (totalFemales + totalMales)) * 100;

        return ratio;
    }

    public double MaleRatio() {
        int totalFemales = 0;
        int totalMales = 0;
        double ratio;

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        for (Collaborateur collaborateur : collaborateurs) {
            String sexe = collaborateur.getSexe().toLowerCase();

            if ("f".equals(sexe) || "femme".equals(sexe)) {
                totalFemales++;
            } else if ("m".equals(sexe) || "homme".equals(sexe)) {
                totalMales++;
            }
        }

        if (totalFemales == 0 && totalMales == 0) {
            return 0.0;
        }
        ratio = ((double) totalMales / (totalFemales + totalMales)) * 100;

        return ratio;
    }

    private int maxDate() {
        int maxDate = Integer.MIN_VALUE;
        for (Collaborateur collaborateur : collaborateurRepository.findAll()) {
            LocalDate localDate = collaborateur.getDate_Embauche().toLocalDate();
            Integer year = localDate.getYear();
            if (year > maxDate) {
                maxDate = year;
            }
        }

        return maxDate;
    }

    private Integer minDate() {
        Integer minDate = Integer.MAX_VALUE;
        for (Collaborateur collaborateur : collaborateurRepository.findAll()) {
            LocalDate localDate = collaborateur.getDate_Embauche().toLocalDate();
            Integer year = localDate.getYear();
            if (year < minDate) {
                minDate = year;
            }
        }
        return minDate;
    }

    public Map<Integer, Integer> getRecruitmentEvolution() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        Map<Integer, Integer> hash = new HashMap<>();
        Integer min = minDate();
        Integer max = maxDate();
        for (Integer i = min - 1; i <= max + 1; i++) {
            Integer numberOfCollabs = 0;
            for (Collaborateur collaborateur : collaborateurs) {
                LocalDate localDate = collaborateur.getDate_Embauche().toLocalDate();
                int year = localDate.getYear();
                if (year == i) {
                    numberOfCollabs++;
                }
            }
            hash.put(i, numberOfCollabs);
        }
        return hash;
    }

    public Map<Integer, Integer> DepartParAnnee() {

        Map<Integer, Integer> DepartByYear = new HashMap<>();

        for (Collaborateur collaborateur : collaborateurRepository.findAll()) {

            if (collaborateur.isAncien_Collaborateur() && collaborateur.getDate_Depart() != null) {

                int departureYear = collaborateur.getDate_Depart().toLocalDate().getYear();

                DepartByYear.put(departureYear, DepartByYear.getOrDefault(departureYear, 0) + 1);
            }
        }
        return DepartByYear;
    }

    public Map<Integer, Double> getSalaryEvolution(Integer id) {
        Map<Integer, Double> archiveMap = archivageService.getSalary(id);
        Collaborateur collaborateur = collaborateurRepository.findById(id).get();
        Integer CurrentYear = LocalDate.now().getYear();
        if(!archiveMap.isEmpty()) {
            Integer LastSalaryEvaluation = archiveMap.keySet().stream().max(Integer::compareTo).get();
            for (Integer i = LastSalaryEvaluation; i <= CurrentYear; i++) {
                if (!archiveMap.containsKey(i)) {
                    archiveMap.put(i, collaborateur.getSalaireActuel());
                }
                else {
                    archiveMap.put(i, (archiveMap.get(i) + collaborateur.getSalaireActuel()) / 2);
                }
            }
        }
        else {
//                archiveMap.put(CurrentYear-1,0.0);
                archiveMap.put(CurrentYear, collaborateur.getSalaireActuel());
//                archiveMap.put(CurrentYear+1,0.0);
        }
        return archiveMap;
    }

    public Map<Integer, Integer> ArriveeParAnnee() {

        Map<Integer, Integer> ArriveeByYear = new HashMap<>();

        for (Collaborateur collaborateur : collaborateurRepository.findAll()) {
            if (collaborateur.getDate_Embauche() != null) {

                int AnneeArrivee = collaborateur.getDate_Embauche().toLocalDate().getYear();

                ArriveeByYear.put(AnneeArrivee, ArriveeByYear.getOrDefault(AnneeArrivee, 0) + 1);

            }
        }
        return ArriveeByYear;
    }

    public Map<Integer, Integer> calculateEffectifs1stJanvier() {
        Map<Integer, Integer> effectifMap = new HashMap<>();
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        // Supposons que collaborateurs contient la liste de collaborateurs

        Integer startYear = minDate();
        Integer endYear = maxDate();

        for (int year = startYear; year <= endYear; year++) {
            int count = 0;
            LocalDate january1st = LocalDate.of(year, 1, 1);

            for (Collaborateur collaborateur : collaborateurs) {
                if (collaborateur.getDate_Embauche() != null) {
                    LocalDate dateEmbauche = collaborateur.getDate_Embauche().toLocalDate();
                    if (!dateEmbauche.isAfter(january1st)) {
                        count++;
                    }
                }
            }

            effectifMap.put(year, count);
        }

        return effectifMap;
    }

    public Map<Integer, Double> calculateTurnoverRates() {
        Map<Integer, Integer> departsByYear = DepartParAnnee();
        Map<Integer, Integer> arriveesByYear = ArriveeParAnnee();
        Map<Integer, Integer> effectifsByYear = calculateEffectifs1stJanvier();

        Map<Integer, Double> turnoverRates = new HashMap<>();

        Set<Integer> years = new HashSet<>();
        years.addAll(departsByYear.keySet());
        years.addAll(arriveesByYear.keySet());
        years.addAll(effectifsByYear.keySet());

        for (int year : years) {
            int departs = departsByYear.getOrDefault(year, 0);
            int arrivees = arriveesByYear.getOrDefault(year, 0);
            int effectif = effectifsByYear.getOrDefault(year, 0);

            double turnoverRate = ((departs + arrivees) / 2.0) / effectif;
            turnoverRates.put(year, turnoverRate * 100);
        }

        return turnoverRates;
    }

    public Map<Integer,List<String>> EvolutionPostAPP(Integer collaborateurId) {

        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new IllegalArgumentException("Collaborator does not exist!!!!!!!!"));

        List<Archivage> archivages = collaborateur.getArchivages();
        Map<Integer, List<String>> posteByYear = new HashMap<>();

        for (Archivage archivage : archivages) {
            int year = archivage.getDateArchivage().toLocalDate().getYear();

            posteByYear.computeIfAbsent(year, k -> new ArrayList<>())
                    .add(archivage.getPosteApp());
        }

        int currentYear = java.time.LocalDate.now().getYear();
        String posteActuel = collaborateur.getPosteActuel();
        posteByYear.computeIfAbsent(currentYear, k -> new ArrayList<>())
                .add(posteActuel);

            return posteByYear;
        }



        public Map<String, Integer> TechnologiesParNiveau(Integer collaborateurId) {

            Optional<Collaborateur> collaborateur = collaborateurRepository.findById(collaborateurId);
            if (!collaborateur.isPresent()) {
                return new HashMap<>();
            }
            List<Technologie> technologies = collaborateur.get().getTechnologies();
            Map<String, Integer> technologiesParNiveau = new HashMap<>();
            for (Technologie technologie : technologies) {
                String nomTechnologie = technologie.getNom();
                int niveauTechnologie = technologie.getNiveau();
                technologiesParNiveau.put(nomTechnologie, niveauTechnologie);
            }


            return technologiesParNiveau;
        }
    

    public Map<Date, Double> getYearlyAverageSalary(Integer collaborateurId) {
        Map<Date, Double> yearlyAverageSalary = new HashMap<>();

        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new IllegalArgumentException("Collaborator does not exist!!!!!!!!"));
        if (collaborateur == null) {
            return yearlyAverageSalary; // Return empty map if collaborateur not found
        }

        List<Archivage> archivages = collaborateur.getArchivages();
        if (archivages == null || archivages.isEmpty()) {
            return yearlyAverageSalary; // Return empty map if no archivages
        }

        Map<Integer, Double> yearlyTotalSalary = new HashMap<>();
        Map<Integer, Integer> yearlyArchivageCount = new HashMap<>();

        for (Archivage archivage : archivages) {
            if (archivage.getDateArchivage() != null && archivage.getSalaire() != null) {
                int year = archivage.getDateArchivage().getYear() + 1900;
                yearlyTotalSalary.put(year, yearlyTotalSalary.getOrDefault(year, 0.0) + archivage.getSalaire());
                yearlyArchivageCount.put(year, yearlyArchivageCount.getOrDefault(year, 0) + 1);
            }
        }

        for (Map.Entry<Integer, Double> entry : yearlyTotalSalary.entrySet()) {
            int year = entry.getKey();
            double totalSalary = entry.getValue();
            int archivageCount = yearlyArchivageCount.getOrDefault(year, 1); // Avoid division by zero
            double averageSalary = totalSalary / archivageCount;
            yearlyAverageSalary.put(new Date(year - 1900, 0, 1), averageSalary);
        }

        return yearlyAverageSalary;
    }

    public List<Collaborateur> findCollabsAssociatedToManagerRH(Integer idManagerRH){
        Optional<Collaborateur> optionalManagerRH=collaborateurRepository.findById(idManagerRH);

        List<Collaborateur> collabsAssociatedToManagerRH=new ArrayList<>();

        if(optionalManagerRH.isPresent()){
            if(optionalManagerRH.get().getRoles().stream().anyMatch(role -> role.getRole().equals("Manager RH"))){
               for (Collaborateur collaborateur :collaborateurRepository.findAll()){
                   if(collaborateur.getManagerRH() !=null && collaborateur.getManagerRH().equals(optionalManagerRH.get())){
                       collabsAssociatedToManagerRH.add(collaborateur);
                   }
               }
            }
            else{
                throw new IllegalStateException("Collaborator does not have the Manager RH role.");
            }
        }
        else {
            throw new IllegalStateException("Collaborator does not exist.");
        }
        return collabsAssociatedToManagerRH;
    }

    public Collaborateur GetCollaborateurByID(Integer matricule){
        Optional<Collaborateur> optionalCollaborateur=collaborateurRepository.findById(matricule);
        if(optionalCollaborateur.isPresent()){
            return optionalCollaborateur.get();
        }
        else{
            throw new IllegalStateException("Collaborator does not exist.");
        }
    }


    public Map<Date, Double> getSalaryData(int collabId,Date startDate,Date endDate) {
        Map<Date, Double> salaryData = new HashMap<>();

        Collaborateur collaborateur = collaborateurRepository.findById(collabId)
                .orElseThrow(() -> new IllegalArgumentException("Collaborator does not exist!!!!!!!!"));
        if (collaborateur == null) {
            return salaryData; // Return empty map if collaborateur not found
        }

        List<Archivage> archivages = collaborateur.getArchivages();
        if (archivages == null || archivages.isEmpty()) {
            return salaryData; // Return empty map if no archivages
        }

        for (Archivage archivage : archivages) {
            if (archivage.getDateArchivage() != null && archivage.getSalaire() != null) {
                Date dateArchivage = archivage.getDateArchivage();
                if (dateArchivage.after(startDate) && dateArchivage.before(endDate)) {
                    salaryData.put(dateArchivage, archivage.getSalaire());
                }
            }
        }

        return salaryData;
    }


}
