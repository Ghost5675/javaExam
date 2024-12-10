package com.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailSender {
    public static String getCode() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            result.append(random.nextInt(10));  
        }

        return result.toString();
    }

    public static void sender(String username, String email, String code) {
        Properties properties = new Properties();
        String host = "smtp.mail.ru";
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                String appPassword = "aNQ89L8bsvuVmL2GueYy"; 
                return new PasswordAuthentication("rgaliev751@mail.ru", appPassword); 
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("rgaliev751@mail.ru");
            msg.setRecipients(Message.RecipientType.TO, email);  
            msg.setSubject("Confirmation of password change");

            msg.setText("Hello, " + username + "!\n"
                    + "We have received a request to change the password for your account. If you really want to change your password, please use the confirmation code provided below:\n"
                    + "[" + code + "]\n" 
                    + "Enter this code in the application to confirm the password change.\n" 
                    + "If you didn't send this request, just ignore this email. Your current password will remain unchanged.\n\n"
                    + "This code is valid for 24 hours.\n"
                    + "If you have any questions, feel free to contact support.");

            Transport.send(msg);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
