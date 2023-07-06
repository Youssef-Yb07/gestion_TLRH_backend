package com.tlrh.gestion_tlrh_backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Date;

@Entity
public class Collaborateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCollaborateur;
    private String email;
    private String password;
    private int matricule;
    private String NomCollaborateur;
    private String PrenomCollaborateur;
    private String AbreviationCollaborateur;
    private String AncienManagerRH;
    private String sexe;
    private String site;
    private String BU;
    private Date Date_Embauche;
    private  String Mois_BAP;
    private Date Date_Depart;
    private boolean Ancien_Collaborateur;
    private boolean SeminaireIntegration;
    private Date DateParticipation;
    private String PosteAPP;
    private String PosteActuel;
    private int SalaireActuel;

}
