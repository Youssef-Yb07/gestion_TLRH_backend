package com.tlrh.gestion_tlrh_backend.entity;
import com.tlrh.gestion_tlrh_backend.entity.Enum.StatutManagerRH;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matricule;

    private String email;

    private String password;

    private String Nom;

    private String Prenom;

    private String AbreviationCollaborateur;

    private String AncienManagerRH;

    @OneToOne
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

    @OneToMany(mappedBy = "collaborateur")
    private List<Diplome> diplomes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutManagerRH statut = StatutManagerRH.Desactive;
    public Collaborateur(Integer matricule,String email, String password, String nom, String prenom, String abreviationCollaborateur, String ancienManagerRH, Collaborateur managerRH, String sexe, String site, String BU, Date date_Embauche, String mois_BAP, Date date_Depart, boolean ancien_Collaborateur, boolean seminaireIntegration, Date dateParticipation, String posteAPP, String posteActuel, int salaireActuel, StatutManagerRH statut) {
        this.matricule=matricule;
        this.email = email;
        this.password = password;
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


}
