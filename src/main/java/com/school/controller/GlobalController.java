package com.school.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "http://localhost:3000",
        methods = {GET, POST, PUT, DELETE},
        allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization"},
        allowCredentials = "true", maxAge = 3600)
public interface GlobalController {
}
