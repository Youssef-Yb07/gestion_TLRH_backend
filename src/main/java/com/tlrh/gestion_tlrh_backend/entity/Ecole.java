package com.tlrh.gestion_tlrh_backend.entity;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ecole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy ="ecole" )
    private List<Diplome> diplomes;

}
