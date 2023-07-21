package com.tlrh.gestion_tlrh_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @OneToOne
    private Collaborateur collaborateur;
    public Compte(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
