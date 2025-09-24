package com.codewithmosh.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminControllers {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World";
    }
}
