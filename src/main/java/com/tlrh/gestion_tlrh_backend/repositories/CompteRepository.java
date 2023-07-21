package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte,Integer> {
    Optional<Compte> findCompteByEmail(String email);
    List<Compte> findByCollaborateurIsNull();
}
