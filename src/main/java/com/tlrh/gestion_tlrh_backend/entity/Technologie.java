package com.tlrh.gestion_tlrh_backend.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Technologie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private int niveau;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Technologie_Collaborateur",
            joinColumns = @JoinColumn(name = "id_technologie"),
            inverseJoinColumns = @JoinColumn(name = "id_collaborateur")
    )
    private List<Collaborateur> collaborateurs;
    @Override
    public String toString() {
        return "Technologie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", niveau=" + niveau +
                '}';
    }
}
