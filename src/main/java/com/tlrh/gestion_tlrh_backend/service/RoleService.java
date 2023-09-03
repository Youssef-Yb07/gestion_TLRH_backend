package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.repositories.RoleRepositories;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepositories roleRepositories;
    public List<Role> getRoles(){
        return roleRepositories.findAll();
    }
    public Role findByRole(String name){
        return roleRepositories.findByRole(name);
    }

}
