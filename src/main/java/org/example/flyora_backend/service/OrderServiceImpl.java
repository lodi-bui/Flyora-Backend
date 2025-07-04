package org.example.flyora_backend.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.flyora_backend.DTOs.CreateOrderDTO;
import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.DTOs.OrderDetailDTO;
import org.example.flyora_backend.DTOs.OrderHistoryDTO;
import org.example.flyora_backend.model.Order;
import org.example.flyora_backend.model.OrderItem;
import org.example.flyora_backend.model.Payment;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.repository.OrderItemRepository;
import org.example.flyora_backend.repository.OrderRepository;
import org.example.flyora_backend.repository.PaymentRepository;
import org.example.flyora_backend.repository.ProductRepository;
import org.example.flyora_backend.utils.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.example.flyora_backend.model.Customer;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    @Override
    public Map<String, Object> createOrder(CreateOrderDTO dto) {
        Integer newId = idGeneratorUtil.generateOrderId();
        if (orderRepository.existsById(newId)) {
            throw new IllegalStateException("Generated Order ID already exists: " + newId);
        }

        Order order = new Order();
        order.setId(newId);

        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        order.setCustomer(customer);
        order.setStatus("PENDING");
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        List<OrderItem> orderItems = new ArrayList<>();
        for (var item : dto.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setId(idGeneratorUtil.generateOrderItemId());
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        // Save Order trước
        Order savedOrder = orderRepository.save(order);

        // Gán lại order đã attach vào từng orderItem
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }

        orderItemRepository.saveAll(orderItems);

        return Map.of("orderId", savedOrder.getId(), "status", savedOrder.getStatus());
    }



    @Override
    public Map<String, Object> createPayment(CreatePaymentDTO dto) {
        Payment payment = new Payment();
        payment.setId(idGeneratorUtil.generatePaymentId());

        Order order = new Order();
        order.setId(dto.getOrderId());
        payment.setOrder(order);

        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        payment.setCustomer(customer);

        payment.setStatus("PAID");
        payment.setPaidAt(Instant.now());

        payment = paymentRepository.save(payment);

        return Map.of("paymentId", payment.getId(), "status", payment.getStatus());
    }

    @Override
    public List<OrderHistoryDTO> getOrdersByCustomer(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        return orders.stream().map(order -> {
            List<OrderDetailDTO> details = order.getOrderDetails().stream().map(detail -> {
                return new OrderDetailDTO(
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getPrice()
                );
            }).toList();

            return new OrderHistoryDTO(
                    order.getId(),
                    order.getCreatedAt(),
                    order.getStatus(),
                    details
            );
        }).toList();
    }
}
