package com.Internship.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ========================================
    // OTP EMAIL
    // ========================================
    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("E-Waste Management - Email Verification");

        message.setText(
                "Your OTP is: " + otp +
                        "\n\nThis OTP is valid for 5 minutes." +
                        "\n\nDo not share this OTP with anyone."
        );

        mailSender.send(message);
    }

    // ========================================
    // REQUEST APPROVED EMAIL
    // ========================================
    public void sendApprovalEmail(String toEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("E-Waste Pickup Request Approved");

        message.setText(
                "Good news!\n\n" +
                        "Your e-waste pickup request has been approved.\n\n" +
                        "Our team will propose a pickup time shortly.\n\n" +
                        "EcoSync Team"
        );

        mailSender.send(message);
    }

    // ========================================
    // REQUEST REJECTED EMAIL
    // ========================================
    public void sendRejectionEmail(String toEmail, String reason) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("E-Waste Pickup Request Update");

        message.setText(
                "Your e-waste pickup request was rejected.\n\n" +
                        "Reason: " + reason + "\n\n" +
                        "You may submit a new request if necessary.\n\n" +
                        "EcoSync Team"
        );

        mailSender.send(message);
    }

    // ========================================
    // PICKUP SLOT PROPOSAL EMAIL
    // ========================================
    public void sendPickupProposalEmail(String toEmail, String date, String slot) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Pickup Time Proposal");

        message.setText(
                "Our team has proposed a pickup time for your e-waste request.\n\n" +
                        "Date: " + date + "\n" +
                        "Time Slot: " + slot + "\n\n" +
                        "Please login to confirm this slot or request another time.\n\n" +
                        "EcoSync Team"
        );

        mailSender.send(message);
    }

    // ========================================
    // PICKUP CONFIRMED EMAIL
    // ========================================
    public void sendPickupScheduledEmail(String toEmail, String dateTime) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("E-Waste Pickup Scheduled");

        message.setText(
                "Your pickup has been scheduled.\n\n" +
                        "Pickup Time: " + dateTime + "\n\n" +
                        "Our staff will arrive during this time slot.\n\n" +
                        "EcoSync Team"
        );

        mailSender.send(message);
    }

    // ========================================
    // PICKUP COMPLETED EMAIL
    // ========================================
    public void sendPickupCompletedEmail(String toEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Pickup Completed");

        message.setText(
                "Your e-waste pickup has been completed successfully.\n\n" +
                        "Thank you for contributing to responsible recycling.\n\n" +
                        "EcoSync Team"
        );

        mailSender.send(message);
    }

}