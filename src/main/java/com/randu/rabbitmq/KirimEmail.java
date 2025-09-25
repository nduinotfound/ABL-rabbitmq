package com.randu.rabbitmq;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class KirimEmail {

    private final JavaMailSender mailSender;

    public KirimEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String to, String productName, int quantity, double price) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
        message.setTo("asyrafilauliarasyid@gmail.com");
        message.setSubject("Order Confirmation");
        message.setText("Thank you for your order!\n\n" +
                "Product: " + productName +
                "\nQuantity: " + quantity +
                "\nTotal: Rp" + price +
                "\n\nWe will process your order shortly.");
        mailSender.send(message);
        }
        catch (Exception e) {
            System.err.println("Gagal mengirim email: " + e.getMessage());
        }
    }
}