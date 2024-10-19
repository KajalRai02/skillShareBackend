package com.Final.Project.utils;

import com.Final.Project.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceNotificationScheduler {

    private final EmailService emailService;

    public MaintenanceNotificationScheduler(EmailService emailService){
        this.emailService=emailService;
    }

   // @Scheduled(cron = "0 * * * * ?")
    public void notifyUser(){
        System.out.println("Sending mails");
        String recipient="shanti.suman@argusoft.in";
        String subject="Scheduled System Notificaton";
        String body="Dear User,Please be informed that our system will undergo maintenance.";

        emailService.sendErrorNotification(recipient,subject,body);
    }
}
