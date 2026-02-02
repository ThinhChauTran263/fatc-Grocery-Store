package poly.edu.java5_asm.module.email.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Service("sendGridEmailService")
@Slf4j
public class SendGridEmailService implements EmailService {

    private final SendGrid sendGridClient;
    private final TemplateEngine templateEngine;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Value("${spring.mail.from:Fat C Grocery Store <noreply@grocerystore.com>}")
    private String fromEmail;

    public SendGridEmailService(
            @Value("${sendgrid.api-key:}") String apiKey,
            TemplateEngine templateEngine,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository) {
        
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("SendGrid API key not configured. Email service will not work.");
            this.sendGridClient = null;
        } else {
            this.sendGridClient = new SendGrid(apiKey);
        }
        
        this.templateEngine = templateEngine;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderConfirmation(Long orderId, Long userId) {
        log.info("=== START sendOrderConfirmation (SendGrid) === orderId={}, userId={}", orderId, userId);

        try {
            // Add small delay to ensure transaction is committed
            Thread.sleep(500);
            
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
            log.info("=== END sendOrderConfirmation === email sent successfully for order {}", order.getOrderNumber());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("=== ERROR sendOrderConfirmation === Thread interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderConfirmation === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendOrderStatusUpdate (SendGrid) === orderId={}, userId={}", orderId, userId);

        try {
            Thread.sleep(500);
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            String subject = "Cập nhật đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderStatusUpdateEmail(order, user, orderItems);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderStatusUpdate === email sent successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("=== ERROR sendOrderStatusUpdate === Thread interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderStatusUpdate === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPaymentStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendPaymentStatusUpdate (SendGrid) === orderId={}, userId={}", orderId, userId);

        try {
            Thread.sleep(500);
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            String subject = getPaymentEmailSubject(order);
            String htmlContent = buildPaymentStatusUpdateEmail(order, user, orderItems);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendPaymentStatusUpdate === email sent successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("=== ERROR sendPaymentStatusUpdate === Thread interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("=== ERROR sendPaymentStatusUpdate === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderCancellationApology(Long orderId, Long userId) {
        log.info("=== START sendOrderCancellationApology (SendGrid) === orderId={}, userId={}", orderId, userId);

        try {
            Thread.sleep(500);
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            String subject = "Xin lỗi về việc hủy đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderCancellationApologyEmail(order, user);

            sendEmail(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderCancellationApology === email sent successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("=== ERROR sendOrderCancellationApology === Thread interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderCancellationApology === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) throws IOException {
        if (sendGridClient == null) {
            log.warn("SendGrid client not initialized. Skipping email send.");
            return;
        }
        
        log.info("Sending email via SendGrid API - To: {}, Subject: {}", to, subject);

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGridClient.api(request);
        
        log.info("SendGrid response - Status: {}, Body: {}", response.getStatusCode(), response.getBody());
        
        if (response.getStatusCode() >= 400) {
            throw new IOException("SendGrid API error: " + response.getStatusCode() + " - " + response.getBody());
        }
    }

    // Template building methods (same as EmailServiceImpl)
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
