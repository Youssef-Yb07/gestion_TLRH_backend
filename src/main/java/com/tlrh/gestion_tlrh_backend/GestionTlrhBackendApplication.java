package com.tlrh.gestion_tlrh_backend;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.DiplomeRepository;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;
import com.tlrh.gestion_tlrh_backend.service.DiplomeService;
import com.tlrh.gestion_tlrh_backend.service.EmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class GestionTlrhBackendApplication {
    @Autowired
    private EmailsService emailsService;
    @Autowired
    private CollaborateurRepository collaborateurRepository;
    @Autowired
    private CollaborateurService collaborateurService;

    @Autowired
    private DiplomeService diplomeService;
    @Autowired
    private DiplomeRepository diplomeRepository;
    //    @Bean
//    public ModelMapper modelMapper(){
//        return new ModelMapper();
//    }
    public static void main(String[] args) {

        SpringApplication.run(GestionTlrhBackendApplication.class, args);

    }
//    Oumnia's test
    //@Bean
    //CommandLineRunner start(CollaborateurService collaborateurService){
    //    return args -> {
    // Collaborateur c=new Collaborateur(12345,"kahlaouioumnia@gmail.com","hidxdd","Kahlaoui","Oumnia","Be",
    //         "Amani",null,"Femme","Rabat","", Date.valueOf("2023-4-12"),"6",null,false,true,null,"d","xc",123456789, StatutManagerRH.Active);
    //  collaborateurService.createCollaborateur(c);
    //  };
    //}
/*//    Oumnia's test
    @Bean
    CommandLineRunner start(CollaborateurService collaborateurService){
        return args -> {
            Collaborateur c=new Collaborateur(12345,"kahlaouioumnia@gmail.com","hidxdd","Kahlaoui","Oumnia","Be",
                    "Amani",null,"Femme","Rabat","", Date.valueOf("2023-4-12"),"6",null,false,true,null,"d","xc",123456789, StatutManagerRH.Active);
            collaborateurService.createCollaborateur(c);
        };
    }*/

    //test1
//    @Bean
//    CommandLineRunner loadData() {
//        return args -> {
//            // Adding test data to the database when the application starts
//            Diplome diplome1 = new Diplome();
//            diplome1.setType("Bachelor");
//            diplome1.setNiveau(3);
//            diplome1.setPromotion(2022);
//
//            Diplome diplome2 = new Diplome();
//            diplome2.setType("Master");
//            diplome2.setNiveau(2);
//            diplome2.setPromotion(2021);
//
//            Diplome diplome3= new Diplome();
//            diplome3.setId(1);
//            diplome3.setType("doctora");
//            diplome3.setNiveau(3);
//            diplome3.setPromotion(2023);
////            diplomeRepository.save(diplome3);
//            diplomeRepository.save(diplome1);
//            diplomeRepository.save(diplome2);
//            diplomeService.createDiplome(diplome3);
//            diplomeService.AffectDiplomeToACollaborateur(12,1);
//
//
//
//        };
//    }
}



