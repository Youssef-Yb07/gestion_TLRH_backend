package com.tlrh.gestion_tlrh_backend.repositories;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface CollaborateurRepository extends JpaRepository<Collaborateur,Integer> {
    @EntityGraph(attributePaths = "roles")
    List<Collaborateur> findAll();
    List<Collaborateur> findCollaborateursByCompteIsNull();
    List<Collaborateur> findCollaborateursByRolesNotLike(String role);
    List<Collaborateur> findCollaborateursByRolesNotContaining(Role role);
}
