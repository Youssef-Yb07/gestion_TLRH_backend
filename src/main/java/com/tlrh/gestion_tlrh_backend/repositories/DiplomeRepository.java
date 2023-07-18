package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiplomeRepository extends JpaRepository<Diplome,Integer> {
}
