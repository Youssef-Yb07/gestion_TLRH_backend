package com.tlrh.gestion_tlrh_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte,Integer> {
    Optional<Compte> findCompteByEmail(String email);
}
