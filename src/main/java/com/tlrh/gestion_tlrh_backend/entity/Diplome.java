package com.tlrh.gestion_tlrh_backend.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Diplome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String niveau ;
    @Column(nullable = false)
    private Integer promotion ;
    @ManyToOne
    @JoinColumn(name="ecole_id")
    private Ecole ecole;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="collaborateur_id")
    private Collaborateur collaborateur;

}