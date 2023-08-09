package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Ecole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole,Integer> {

}
