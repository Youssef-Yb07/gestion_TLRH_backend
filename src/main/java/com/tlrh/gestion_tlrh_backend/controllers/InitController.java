package com.tlrh.gestion_tlrh_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @GetMapping("/hello")
    public String init() {
        return "<h1> Hello World!</h1>";
    }
}
