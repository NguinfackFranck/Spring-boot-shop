package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/say hello")
    public Message hello() {
        return new Message("Say Hello World");
    }
}
