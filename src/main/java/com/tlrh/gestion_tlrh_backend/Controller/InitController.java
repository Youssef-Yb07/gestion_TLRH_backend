package com.tlrh.gestion_tlrh_backend.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @RequestMapping("/hello")
    public String init() {
        return "<h1> Hello World!</h1>";
    }
}
