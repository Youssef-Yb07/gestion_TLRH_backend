package com.tlrh.gestion_tlrh_backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name="name ")
@AllArgsConstructor
@NoArgsConstructor
public class Ecole {

    @Id
    @GeneratedValue
    private Integer  id;

    @Column(nullable = false)
    private String NomeEcole;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    @OneToMany(mappedBy ="id" )
    private List<Diplome> diplomes;


}
