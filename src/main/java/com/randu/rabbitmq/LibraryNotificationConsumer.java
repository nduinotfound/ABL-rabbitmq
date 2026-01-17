package com.randu.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class LibraryNotificationConsumer {

    private final KirimEmail kirimEmail;

    public LibraryNotificationConsumer(KirimEmail kirimEmail) {
        this.kirimEmail = kirimEmail;
    }

    @RabbitListener(queues = "${app.library.queue}")
    public void receiveNotification(@Payload String message) {
        try {
            System.out.println("Notification received from library: " + message);
            
            // For now, just logging it and "sending email" via console
            System.out.println("[ACTION] Sending notification email with content: " + message);
            
            // In real case, you might want to parse the message and send a real email
            // kirimEmail.sendSimpleMessage("target@gmail.com", "Library Notification", message);
            
        } catch (Exception e) {
            System.err.println("Error processing library notification: " + e.getMessage());
        }
    }
}
