package com.tlrh.gestion_tlrh_backend;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import com.tlrh.gestion_tlrh_backend.service.EmailsService;
import org.modelmapper.ModelMapper;
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

//    @Bean
//    public ModelMapper modelMapper(){
//        return new ModelMapper();
//    }
    public static void main(String[] args) {

        SpringApplication.run(GestionTlrhBackendApplication.class, args);

    }
//    Oumnia's test
//    @Bean
//
//    CommandLineRunner start(){
//        return args -> {
//            Collaborateur c=collaborateurRepository.save(new Collaborateur(1,"kahlaouioumnia@gmail.com","hi","Kahlaoui","Oumnia","Be",
//                    "Abdelhaj",null,"Femme","Rabat","", Date.valueOf("2023-4-12"),"6",new Date(0),false,true,new Date(0),"d","xc",123456789, StatutManagerRH.Active));
//            collaborateurService.SendMails(c);
//        };
//    }

}
