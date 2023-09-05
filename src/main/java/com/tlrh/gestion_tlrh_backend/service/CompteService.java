package com.tlrh.gestion_tlrh_backend.service;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Compte;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CompteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompteService {
    private CompteRepository compteRepository;
    private CollaborateurRepository collaborateurRepository;
    private EmailsService emailsService;


    public Compte AjouterCompte(String compteEmail) {
        Optional<Compte> optionalCompte = compteRepository.findCompteByEmail(compteEmail);
        if (optionalCompte.isPresent()) {
            throw new IllegalStateException("Already Exist");
        }
        Compte compte = new Compte(null, compteEmail, generatePassword());
        return compteRepository.save(compte);
    }

    public Compte AccountToCollab(Integer compteId, Integer collaborateurId) throws MessagingException {
        Optional<Compte> optionalCompte = compteRepository.findById(compteId);
        Optional<Collaborateur> optionalCollab = collaborateurRepository.findById(collaborateurId);
        if (!(optionalCompte.isPresent() && optionalCollab.isPresent())) {
            throw new IllegalArgumentException("Both Compte and Collaborateur entities should be present.");
        }
        Compte compte = optionalCompte.get();
        Collaborateur collaborateur = optionalCollab.get();
        compte.setCollaborateur(collaborateur);
        compteRepository.save(compte);
        emailsService.SendEmail(collaborateur.getEmail()
                ,"Hi Dear "+collaborateur.getPrenom() +" " +collaborateur.getNom()
                        +" , To acces to your personnal space in our Application SQLI Here's the authentication information . The Email :"
                        +compte.getEmail()+" And the password : "+compte.getPassword()
                ,"New Account");
        return compte;
    }

    private String generatePassword() {
        String Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String passwordBuilder = "";
        for (int i = 0; i < 13; i++) {
            int index = (int) (Math.random() * Chars.length());
            passwordBuilder += Chars.charAt(index);
        }
        return passwordBuilder;
    }

    public List<Compte> GetComptes(){
        return compteRepository.findAll();
    }
    public Compte ChangePassword(String email,String OldPassword,String newPassword,String confirmedPassword) throws MessagingException {
        Optional<Compte> optionalCompte = compteRepository.findCompteByEmail(email);
        if (!optionalCompte.isPresent()) {
            throw new IllegalArgumentException("You should enter valid email .");
        }
        Compte compte = optionalCompte.get();
        int valideAuthentication=OldPassword.compareTo(compte.getPassword());
        int samePassword=newPassword.compareTo(confirmedPassword);
        Collaborateur collaborateur=compte.getCollaborateur();
        if (valideAuthentication==0 && samePassword==0){
            compte.setPassword(confirmedPassword);
            emailsService.SendEmail(collaborateur.getEmail()
                    ,"Hi Dear "+collaborateur.getPrenom() +" " +collaborateur.getNom()
                            +" , To acces to your personnal space in our Application SQLI , Your new password is : "+compte.getPassword()
                    ,"Password has changed");
            return compteRepository.save(compte);
        }
        else {
            throw new RuntimeException("Password should be valid ");
        }

    }
    public List<Compte> accountWithoutCollab(){
        return compteRepository.findByCollaborateurIsNull();
    }



}



