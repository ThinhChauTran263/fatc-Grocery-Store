package poly.edu.java5_asm.module.email.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.util.List;

/**
 * Resend Email Service Implementation
 * Uses Resend API instead of SMTP for better deliverability on cloud platforms
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "app.email.provider", havingValue = "resend", matchIfMissing = false)
public class ResendEmailServiceImpl implements EmailService {

    private final Resend resend;
    private final TemplateEngine templateEngine;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Value("${spring.mail.from:onboarding@resend.dev}")
    private String fromEmail;

    public ResendEmailServiceImpl(
            @Value("${resend.api-key}") String apiKey,
            TemplateEngine templateEngine,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository) {
        this.resend = new Resend(apiKey);
        this.templateEngine = templateEngine;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        log.info("Resend Email Service initialized with API key");
    }

    @Override
    @Async
    public void sendOrderConfirmation(Long orderId, Long userId) {
        log.info("=== START sendOrderConfirmation (Resend) === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            log.info("Found order {} with {} items for user {}", 
                    order.getOrderNumber(), orderItems.size(), user.getEmail());

            String subject = "Xác nhận đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderConfirmationEmail(order, user, orderItems);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderConfirmation (Resend) === email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderConfirmation (Resend) === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendOrderStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendOrderStatusUpdate (Resend) === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            String subject = "Cập nhật đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderStatusUpdateEmail(order, user, orderItems);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderStatusUpdate (Resend) === email sent successfully");
        } catch (Exception e) {
            log.error("=== ERROR sendOrderStatusUpdate (Resend) === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendPaymentStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendPaymentStatusUpdate (Resend) === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            String subject = getPaymentEmailSubject(order);
            String htmlContent = buildPaymentStatusUpdateEmail(order, user, orderItems);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendPaymentStatusUpdate (Resend) === email sent successfully");
        } catch (Exception e) {
            log.error("=== ERROR sendPaymentStatusUpdate (Resend) === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendOrderCancellationApology(Long orderId, Long userId) {
        log.info("=== START sendOrderCancellationApology (Resend) === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            String subject = "Xin lỗi về việc hủy đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderCancellationApologyEmail(order, user);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderCancellationApology (Resend) === email sent successfully");
        } catch (Exception e) {
            log.error("=== ERROR sendOrderCancellationApology (Resend) === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) throws ResendException {
        SendEmailRequest request = SendEmailRequest.builder()
                .from(fromEmail)
                .to(to)
                .subject(subject)
                .html(htmlContent)
                .build();

        SendEmailResponse response = resend.emails().send(request);
        log.info("Resend email sent successfully. ID: {}", response.getId());
    }

    private String buildOrderConfirmationEmail(Order order, User user, List<OrderItem> orderItems) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        return templateEngine.process("shared/email/order-confirmation-email", context);
    }

    private String buildOrderStatusUpdateEmail(Order order, User user, List<OrderItem> orderItems) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        context.setVariable("statusMessage", getStatusMessage(order.getStatus()));
        return templateEngine.process("shared/email/order-status-update-email", context);
    }

    private String buildPaymentStatusUpdateEmail(Order order, User user, List<OrderItem> orderItems) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        context.setVariable("paymentStatusMessage", getPaymentStatusMessage(order.getPaymentStatus()));
        context.setVariable("isPaymentSuccess", order.getPaymentStatus() == Order.PaymentStatus.PAID);
        return templateEngine.process("shared/email/payment-status-email", context);
    }

    private String buildOrderCancellationApologyEmail(Order order, User user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        return templateEngine.process("shared/email/order-cancellation-apology-email", context);
    }

    private String getPaymentEmailSubject(Order order) {
        return switch (order.getPaymentStatus()) {
            case PAID -> "Thanh toán thành công - Đơn hàng #" + order.getOrderNumber();
            case FAILED -> "Thanh toán thất bại - Đơn hàng #" + order.getOrderNumber();
            case REFUNDED -> "Hoàn tiền thành công - Đơn hàng #" + order.getOrderNumber();
            case PENDING -> "Chờ thanh toán - Đơn hàng #" + order.getOrderNumber();
        };
    }

    private String getPaymentStatusMessage(Order.PaymentStatus status) {
        return switch (status) {
            case PENDING -> "Đơn hàng của bạn đang chờ thanh toán";
            case PAID -> "Thanh toán đã được xác nhận thành công";
            case FAILED -> "Thanh toán không thành công. Vui lòng thử lại hoặc chọn phương thức thanh toán khác";
            case REFUNDED -> "Số tiền đã được hoàn lại vào tài khoản của bạn";
        };
    }

    private String getStatusMessage(Order.OrderStatus status) {
        return switch (status) {
            case PENDING -> "Đơn hàng của bạn đang chờ xác nhận";
            case CONFIRMED -> "Đơn hàng của bạn đã được xác nhận";
            case PROCESSING -> "Đơn hàng của bạn đang được xử lý";
            case SHIPPED -> "Đơn hàng của bạn đã được giao cho đơn vị vận chuyển";
            case DELIVERED -> "Đơn hàng của bạn đã được giao thành công";
            case CANCELLED -> "Đơn hàng của bạn đã bị hủy";
        };
    }
}
