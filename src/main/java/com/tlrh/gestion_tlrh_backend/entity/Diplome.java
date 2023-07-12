package com.tlrh.gestion_tlrh_backend.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diplome {

    @Id
    @GeneratedValue
    private Integer  id;
    @Column(nullable = false)
    private String typeDiplome;
    @Column(nullable = false)
    private Integer niveau ;
    @Column(nullable = false)
    private Integer promotion ;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    @ManyToOne
    @JoinColumn(name="ecole-id")
    private Ecole ecole;

    @ManyToOne
    @JoinColumn(name="col-id")
    private Collaborateur col;
}
