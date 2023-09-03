package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.repositories.RoleRepositories;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    @Autowired
    private RoleRepositories roleRepositories;

    public List<Role> getAllRoles() {
        List<Role> roles = roleRepositories.findAll();
        if (roles.isEmpty()) {
            throw new IllegalStateException("Roles not found");
        } else {
            return roles;
        }
    }

    public Role getRoleById(Integer id) {
        Optional<Role> role = roleRepositories.findById(id);
        if (role.isPresent()) {
            return role.get();
        }  else {
            throw new IllegalStateException("Role not found");
        }
    }

    public List<Role> getRoles(){
        return roleRepositories.findAll();
    }
    public Role findByRole(String name){
        return roleRepositories.findByRole(name);
    }

}
