package com.tlrh.gestion_tlrh_backend.Entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TechnologieCollaborateur",
            joinColumns = @JoinColumn(name = "id_technologie"),
            inverseJoinColumns = @JoinColumn(name = "id_collaborateur")
    )
    private List<Collaborateur> collaborateurs;
}
