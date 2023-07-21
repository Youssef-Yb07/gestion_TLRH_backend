package com.tlrh.gestion_tlrh_backend;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import com.tlrh.gestion_tlrh_backend.service.EmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Date;

@SpringBootApplication
@EnableScheduling
public class GestionTlrhBackendApplication {
    @Autowired
    private EmailsService emailsService;
    @Autowired
    private CollaborateurRepository collaborateurRepository;
    @Autowired
    private CollaborateurService collaborateurService;
    public static void main(String[] args) {

        SpringApplication.run(GestionTlrhBackendApplication.class, args);
    }

//    Oumnia's test
//    @Bean
//    CommandLineRunner start(CollaborateurService collaborateurService){
//        return args -> {
//            Collaborateur c=new Collaborateur(12345,"kahlaouioumnia@gmail.com","Kahlaoui","Oumnia","Be",
//                    "Amani",null,"Femme","Rabat","", Date.valueOf("2023-4-12"),"6",null,false,true,null,"d","xc",123456789, StatutManagerRH.Active);
//            collaborateurService.createCollaborateur(c);
//        };
//    }

}
