package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivageRepository extends JpaRepository<Archivage, Integer> {
     List<Archivage> getArchivageByCollaborateur_Matricule(Integer id);
}
