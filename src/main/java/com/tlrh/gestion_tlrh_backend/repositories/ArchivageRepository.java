package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivageRepository extends JpaRepository<Archivage, Integer> {

}
