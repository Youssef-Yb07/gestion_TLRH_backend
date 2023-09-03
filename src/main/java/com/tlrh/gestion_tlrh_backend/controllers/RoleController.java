package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.entity.Role;
import com.tlrh.gestion_tlrh_backend.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@Tag(name = "Role", description = "Gestion des Roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired private RoleService roleService;
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
