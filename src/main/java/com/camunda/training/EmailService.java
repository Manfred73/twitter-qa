package com.camunda.training;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    public String sendEmail() {
        return UUID.randomUUID().toString();
    }
}
