package com.tlrh.gestion_tlrh_backend.controllers;

import com.tlrh.gestion_tlrh_backend.service.CompteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compte")
@AllArgsConstructor
public class CompteController {

    @Autowired
    private CompteService compteService;


}
