package com.tlrh.gestion_tlrh_backend.controller;

import com.tlrh.gestion_tlrh_backend.service.CompteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compte")
@AllArgsConstructor
public class CompteController {
    private CompteService compteService;

}
