package com.tlrh.gestion_tlrh_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Certificat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    private Date date;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Certificat_Collaborateur",
            joinColumns = @JoinColumn(name = "id_certificat"),
            inverseJoinColumns = @JoinColumn(name = "id_collaborateur")
    )
    private List<Collaborateur> collaborateurs;

}