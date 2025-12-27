package com.example.SmartConsult.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/patient")
public class PatientTestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Patient!";
    }
}
