package com.tlrh.gestion_tlrh_backend.dto;

import lombok.Data;

@Data
public class CollaborateurDto {

    private Integer matricule;
    private String Nom;
    private String Prenom;
    private String sexe;
    private String site;
    private String Bu;
    private String email;

    private Double SalaireActuel;

    private Integer managerRH;

    private String PosteAPP;
}
