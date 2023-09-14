package com.tlrh.gestion_tlrh_backend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Collaborateur {
    @Id
    private Integer matricule;
    private String email;
    private String Nom;

    private String Prenom;

    private String AbreviationCollaborateur;

    private String AncienManagerRH;

    @OneToOne
    @JsonBackReference
    private Collaborateur managerRH;

    //F or M : Femme ou Homme
    private String sexe;

    private String site;

    private String BU;

    private Date Date_Embauche;

    private  String Mois_BAP;

    private Date Date_Depart=null;

    private boolean Ancien_Collaborateur=false;

    private boolean SeminaireIntegration=false;

    private Date DateParticipation;

    private String PosteAPP;

    private String PosteActuel;

    private Double SalaireActuel;


    @ManyToMany
    @JoinTable(
            name = "Technologie_Collaborateur",
            joinColumns = @JoinColumn(name = "id_collaborateur"),
            inverseJoinColumns = @JoinColumn(name = "id_technologie")
    )
    private List<Technologie> technologies;
    
    @ManyToMany
    @JoinTable(
            name = "Role_Collaborateur",
            joinColumns = @JoinColumn(name = "id_collaborateur"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "collaborateur")
    private List<Archivage> archivages;

    @OneToMany(mappedBy = "collaborateur")
    private List<Diplome> diplomes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutManagerRH statut = StatutManagerRH.Desactive;

    @OneToOne(mappedBy = "collaborateur",fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Compte compte;
    @ManyToMany
    @JoinTable(
            name = "certificat_Collaborateur",
            joinColumns = @JoinColumn(name = "id_collaborateur"),
            inverseJoinColumns = @JoinColumn(name = "id_certificat")
    )
    private List<Certificat> certificats;
    public Collaborateur(Integer matricule,String email, String nom, String prenom, String abreviationCollaborateur, String ancienManagerRH, Collaborateur managerRH, String sexe, String site, String BU, Date date_Embauche, String mois_BAP, Date date_Depart, boolean ancien_Collaborateur, boolean seminaireIntegration, Date dateParticipation, String posteAPP, String posteActuel, Double salaireActuel, StatutManagerRH statut) {
        this.matricule=matricule;
        this.email = email;
        this.Nom = nom;
        this.Prenom = prenom;
        this.AbreviationCollaborateur = abreviationCollaborateur;
        this.AncienManagerRH = ancienManagerRH;
        this.sexe = sexe;
        this.site = site;
        this.BU = BU;
        Date_Embauche = date_Embauche;
        Mois_BAP = mois_BAP;
        Date_Depart = date_Depart;
        Ancien_Collaborateur = ancien_Collaborateur;
        SeminaireIntegration = seminaireIntegration;
        DateParticipation = dateParticipation;
        PosteAPP = posteAPP;
        PosteActuel = posteActuel;
        SalaireActuel = salaireActuel;
        this.statut = statut;
    }
    @Override
    public String toString() {
        return "Collaborateur{" +
                "matricule=" + matricule +
                ", email='" + email + '\'' +
                ", Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                ", AbreviationCollaborateur='" + AbreviationCollaborateur + '\'' +
                ", AncienManagerRH='" + AncienManagerRH + '\'' +
                ", managerRH=" + managerRH +
                ", sexe='" + sexe + '\'' +
                ", site='" + site + '\'' +
                ", BU='" + BU + '\'' +
                ", Date_Embauche=" + Date_Embauche +
                ", Mois_BAP='" + Mois_BAP + '\'' +
                ", Date_Depart=" + Date_Depart +
                ", Ancien_Collaborateur=" + Ancien_Collaborateur +
                ", SeminaireIntegration=" + SeminaireIntegration +
                ", DateParticipation=" + DateParticipation +
                ", PosteAPP='" + PosteAPP + '\'' +
                ", PosteActuel='" + PosteActuel + '\'' +
                ", SalaireActuel=" + SalaireActuel +
                ", technologies=" + technologies +
                ", roles=" + roles +
                ", archivages=" + archivages +
                ", diplomes=" + diplomes +
                ", statut=" + statut +
                ", compte=" + compte +
                ", certificats=" + certificats +
                '}';
    }

}
