package com.example.demo.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    public void sendConfirmationCode(String email, UUID confirmationCode) {
        //for now confirmation code is logged into console
        //TODO: implement email sending mechanism and sebd code via email
        log.info("###### Confirmation code for {}: {} ######", email, confirmationCode);
    }

}
