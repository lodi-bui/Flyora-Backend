package org.example.flyora_backend.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.flyora_backend.DTOs.*; // Import tất cả DTOs
import org.example.flyora_backend.model.*; // Import tất cả models
import org.example.flyora_backend.repository.*;
import org.example.flyora_backend.utils.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    // --- CÁC REPOSITORY VÀ SERVICE CẦN THIẾT ---
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository; // Cần repo này
    private final DeliveryNoteRepository deliveryNoteRepository; // Cần repo này
    private final ShippingMethodRepository shippingMethodRepository; // Cần repo này
    private final IdGeneratorUtil idGeneratorUtil;
    private final GHNService ghnService; // **QUAN TRỌNG: Inject GHNService**

    @Override
    @Transactional
    public Map<String, Object> createOrder(CreateOrderDTO dto) {
        // 1. Tạo đối tượng Order chính
        Order order = new Order();
        order.setId(idGeneratorUtil.generateOrderId());

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
        order.setCustomer(customer);
        order.setStatus("PENDING");
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // 2. Tạo danh sách OrderItem và thiết lập mối quan hệ hai chiều
        List<OrderItem> orderItems = new ArrayList<>();
        for (var itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDTO.getProductId()));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setId(idGeneratorUtil.generateOrderItemId()); // <-- VẪN CẦN KIỂM TRA LỖI TRÙNG ID Ở ĐÂY
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            // QUAN TRỌNG: Thiết lập mối quan hệ từ OrderItem -> Order
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        // 3. Gán danh sách item cho Order (hoàn thành mối quan hệ hai chiều)
        order.setOrderDetails(orderItems);

        // 4. Chỉ cần lưu Order. JPA sẽ tự động lưu các OrderItem nhờ CascadeType.ALL
        Order savedOrder = orderRepository.save(order);

        return Map.of("orderId", savedOrder.getId(), "status", savedOrder.getStatus());
    }

    @Override
    @Transactional // **QUAN TRỌNG: Toàn bộ quá trình là một giao dịch**
    public Map<String, Object> createPayment(CreatePaymentDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getCustomer().getId().equals(dto.getCustomerId())) {
            throw new RuntimeException("Customer ID không khớp với đơn hàng.");
        }

        // === BƯỚC 1: TẠO ĐƠN VẬN CHUYỂN TRÊN GHN ===
        // Giả sử chỉ tạo đơn vận chuyển cho COD tại bước này
        if (dto.getPaymentMethodId() == 2) { // 2 = COD
            try {
                // 1.1 Chuẩn bị request cho GHN
                // Lưu ý: Cần thêm các trường địa chỉ vào CreatePaymentDTO hoặc lấy từ Customer
                CreateOrderRequestDTO ghnRequest = createGhnRequestFromOrder(order, dto);

                // 1.2 Gọi GHN Service để tạo đơn
                Map<String, Object> ghnResponse = ghnService.createOrder(ghnRequest);

                // 1.3 Lấy mã vận đơn và cập nhật DeliveryNote
                String trackingNumber = (String) ghnResponse.get("order_code");
                ShippingMethod shippingMethod = shippingMethodRepository.findById(1).orElse(null); // Giả sử ID 1 là
                                                                                                   // Giao hàng tiêu
                                                                                                   // chuẩn

                DeliveryNote deliveryNote = new DeliveryNote();
                deliveryNote.setId(idGeneratorUtil.generateDeliveryNoteId());
                deliveryNote.setOrder(order);
                deliveryNote.setTrackingNumber(trackingNumber);
                deliveryNote.setShippingMethod(shippingMethod);
                deliveryNote.setDeliveryPartnerName("GHN");
                deliveryNote.setStatus("ready_to_pick"); // Trạng thái ban đầu của GHN
                deliveryNoteRepository.save(deliveryNote);

                // 1.4 Cập nhật lại trạng thái đơn hàng chính và tồn kho
                order.setStatus("Shipping"); // Đang giao hàng
                for (OrderItem item : order.getOrderDetails()) {
                    Product product = item.getProduct();
                    product.setStock(product.getStock() - item.getQuantity());
                    productRepository.save(product);
                }
                orderRepository.save(order);

            } catch (Exception e) {
                throw new RuntimeException("Tạo đơn vận chuyển thất bại: " + e.getMessage(), e);
            }
        }
        // === KẾT THÚC PHẦN TÍCH HỢP GHN ===

        // === BƯỚC 2: TẠO BẢN GHI THANH TOÁN ===
        Payment payment = new Payment();
        payment.setId(idGeneratorUtil.generatePaymentId());
        payment.setOrder(order);
        payment.setCustomer(order.getCustomer());
        payment.setStatus("PENDING_COD"); // Trạng thái cho COD
        paymentRepository.save(payment);

        return Map.of("paymentId", payment.getId(), "orderStatus", order.getStatus());
    }

    // PHƯƠNG THỨC PHỤ ĐỂ TẠO GHN REQUEST
    private CreateOrderRequestDTO createGhnRequestFromOrder(Order order, CreatePaymentDTO paymentDTO) {
        CreateOrderRequestDTO ghnRequest = new CreateOrderRequestDTO();
        Customer customer = order.getCustomer();

        // LƯU Ý: Bạn cần có thông tin địa chỉ chi tiết.
        // Ở đây giả định chúng được truyền trong CreatePaymentDTO.
        // Bạn cần thêm các trường to_name, to_phone, to_address, to_ward_code,
        // to_district_id vào CreatePaymentDTO
        ghnRequest.setTo_name(customer.getName());
        ghnRequest.setTo_phone(/* paymentDTO.getToPhone() */ "0942921287"); // Dùng SĐT test
        ghnRequest.setTo_address(/* paymentDTO.getToAddress() */ "123 Đường ABC");
        ghnRequest.setTo_ward_code(/* paymentDTO.getToWardCode() */ "20309");
        ghnRequest.setTo_district_id(/* paymentDTO.getToDistrictId() */ 1459);

        BigDecimal codAmount = order.getOrderDetails().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ghnRequest.setCod_amount(codAmount.intValue());
        ghnRequest.setContent("Flyora - Don hang #" + order.getId());

        // ==========================================================
        // SỬA LỖI LOGIC TÍNH TỔNG TRỌNG LƯỢNG Ở ĐÂY
        // ==========================================================
        int totalWeight = order.getOrderDetails().stream()
                .mapToInt(item -> {
                    Product product = item.getProduct();
                    BigDecimal weight = BigDecimal.ZERO;

                    // Kiểm tra loại sản phẩm và lấy weight từ detail tương ứng
                    if (product.getFoodDetail() != null && product.getFoodDetail().getWeight() != null) {
                        weight = product.getFoodDetail().getWeight();
                    } else if (product.getToyDetail() != null && product.getToyDetail().getWeight() != null) {
                        weight = product.getToyDetail().getWeight();
                    } else if (product.getFurnitureDetail() != null
                            && product.getFurnitureDetail().getWeight() != null) {
                        weight = product.getFurnitureDetail().getWeight();
                    }

                    // Chuyển đổi weight sang gram (nếu đơn vị trong DB là kg thì nhân 1000)
                    // Giả sử đơn vị trong DB đã là gram
                    return weight.intValue() * item.getQuantity();
                }).sum();
        // ==========================================================

        ghnRequest.setWeight(totalWeight > 0 ? totalWeight : 200); // Trọng lượng tối thiểu 200g

        ghnRequest.setLength(20);
        ghnRequest.setWidth(20);
        ghnRequest.setHeight(10);
        ghnRequest.setInsurance_value(codAmount.intValue());

        ghnRequest.setService_id(53321); // Mặc định
        ghnRequest.setPayment_type_id(2); // Khách trả phí
        ghnRequest.setNote("Ghi chú từ đơn hàng");
        ghnRequest.setRequired_note("CHOXEMHANGKHONGTHU");

        List<ItemDTO> ghnItems = order.getOrderDetails().stream().map(item -> {
            ItemDTO ghnItem = new ItemDTO();
            ghnItem.setName(item.getProduct().getName());
            ghnItem.setQuantity(item.getQuantity());
            ghnItem.setPrice(item.getPrice().intValue());
            return ghnItem;
        }).toList();
        ghnRequest.setItems(ghnItems);

        return ghnRequest;
    }

    @Override
    public List<OrderHistoryDTO> getOrdersByCustomer(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        return orders.stream().map(order -> {
            List<OrderDetailDTO> details = order.getOrderDetails().stream().map(detail -> new OrderDetailDTO(
                    detail.getProduct().getId(),
                    detail.getProduct().getName(),
                    detail.getQuantity(),
                    detail.getPrice())).toList();

            return new OrderHistoryDTO(
                    order.getId(),
                    order.getCreatedAt(),
                    order.getStatus(),
                    details);
        }).toList();
    }
}