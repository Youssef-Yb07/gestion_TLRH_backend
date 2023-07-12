package com.tlrh.gestion_tlrh_backend.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String typeDiplome;

    @Column(nullable = false)
    private Integer niveau ;

    @Column(nullable = false)
    private Integer promotion ;

    @ManyToOne
    @JoinColumn(name = "ecole_id")
    private Ecole ecole;

    @ManyToOne
    @JoinTable(
            name = "collaborateur_diplome",
            joinColumns = @JoinColumn(name = "id_diplome"),
            inverseJoinColumns = @JoinColumn(name = "id_collaborateur"))
    private Collaborateur col;
}
