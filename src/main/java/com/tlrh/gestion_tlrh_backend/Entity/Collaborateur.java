package com.tlrh.gestion_tlrh_backend.Entity;

import com.tlrh.gestion_tlrh_backend.Entity.Archive.Archivage;
import lombok.Data;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Collaborateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @OneToMany
    @JoinTable(name = "collaborateur_archivage",
            joinColumns = @JoinColumn(name = "id_collaborateur"),
            inverseJoinColumns = @JoinColumn(name = "id_archivage"))
    private List<Archivage> archivages;


}
