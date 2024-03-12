package com.example.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ShippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingApplication.class, args);
    }

}
@Service
@RequiredConstructor
class OrderService {

    private final KafkaTemplate kafkaTemplate;
    private final ShippingRepository shippingRepository;

    @KafkaListener(topics = "prod.orders.placed", groupId = "shipping_group")
    public void handleOrderPlacedEvent (OrderPlacedEvent event) {

        Shipping shipping = new Shipping();
        shipping.setOrderId(event.getOrderID());
        this.shippingRepository.save(shipping);
        this.kafkaTemplate.send("prod.orders.shipped",shipping.getOrderId() ,String.valueOf(shipping.getOrderId()));
    }
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class OrderPlacedEvent {
    private String product;
    private double price;
}
