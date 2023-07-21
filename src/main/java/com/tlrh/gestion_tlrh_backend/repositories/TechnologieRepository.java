package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Technologie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologieRepository extends JpaRepository<Technologie,Integer> {
    Technologie findTechnologieByNom(String nom);
}
