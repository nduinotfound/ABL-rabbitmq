package com.randu.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderConsumerService {

    private final OrderRepository orderRepository;
    private final KirimEmail kirimEmail;

    public OrderConsumerService(OrderRepository orderRepository, KirimEmail kirimEmail) {
        this.orderRepository = orderRepository;
        this.kirimEmail = kirimEmail;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receiveOrder(@Payload Order order) {
        try {
            System.out.println("Order received from RabbitMQ: " + order);

            // Update status jadi PROCESSING
            order.setStatus(Order.OrderStatus.PROCESSING);
            orderRepository.save(order);

            processOrder(order);

            // Update status jadi COMPLETED
            order.setStatus(Order.OrderStatus.COMPLETED);
            order.setProcessedAt(java.time.LocalDateTime.now());
            orderRepository.save(order);

            System.out.println("Order processed successfully: " + order.getId());

        } catch (Exception e) {
            System.err.println("Error processing order: " + order.getId() + ", Error: " + e.getMessage());

            order.setStatus(Order.OrderStatus.FAILED);
            orderRepository.save(order);

            throw new RuntimeException("Failed to process order", e);
        }
    }

    private void processOrder(Order order) {
        System.out.println("Processing order: " + order.getId());

        kirimEmail.sendOrderConfirmation(
                order.getCustomerEmail(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice()
        );

        System.out.println("Order processing completed: " + order.getId());
    }
}