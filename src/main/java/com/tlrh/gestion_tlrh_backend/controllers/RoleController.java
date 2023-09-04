package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/role")
@CrossOrigin(origins = "*")
public class RoleController {
    private RoleService roleService;
    @GetMapping("/getRoles")
    public ResponseEntity<List<Role>> getRoles(){
        try {
            List<Role> roles = roleService.getRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        try{
            return  new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/By/Id")
    public ResponseEntity<Role> getRoleByID(@RequestParam Integer idRole){
        try{
            return  new ResponseEntity<>(roleService.getRoleById(idRole), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
