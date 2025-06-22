package org.example.flyora_backend.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import org.example.flyora_backend.DTOs.CreateOrderDTO;
import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.model.Order;
import org.example.flyora_backend.model.OrderItem;
import org.example.flyora_backend.model.Payment;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.repository.OrderItemRepository;
import org.example.flyora_backend.repository.OrderRepository;
import org.example.flyora_backend.repository.PaymentRepository;
import org.example.flyora_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.example.flyora_backend.model.Customer;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Map<String, Object> createOrder(CreateOrderDTO dto) {
        Order order = new Order();

        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        order.setCustomer(customer);

        order.setStatus(false); // Tạm hiểu false là trạng thái "chờ xử lý"

        order.setOrderDate(new Timestamp(System.currentTimeMillis()));

        order = orderRepository.save(order);

        for (var item : dto.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
        }

        return Map.of("orderId", order.getId(), "status", order.getStatus());
    }

    @Override
    public Map<String, Object> createPayment(CreatePaymentDTO dto) {
        Payment payment = new Payment();

        // Gán Order (chỉ cần ID)
        Order order = new Order();
        order.setId(dto.getOrderId());
        payment.setOrder(order);

        // Gán Customer
        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        payment.setCustomer(customer);

        // Trạng thái thanh toán + thời điểm
        payment.setStatus("PAID");
        payment.setPaidAt(Instant.now());

        // Lưu vào DB
        payment = paymentRepository.save(payment);

        return Map.of(
            "paymentId", payment.getId(),
            "status", payment.getStatus()
        );
    }
}
