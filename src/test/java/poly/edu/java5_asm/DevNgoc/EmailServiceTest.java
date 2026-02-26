package poly.edu.java5_asm.DevNgoc;

import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import poly.edu.java5_asm.module.address.entity.Address;
import poly.edu.java5_asm.module.email.service.EmailServiceImpl;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test cases cho EmailService - Thiên Ngọc
 * Test cases: EMAIL_001, EMAIL_002
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    private User testUser;
    private Order testOrder;
    private OrderItem testOrderItem;
    private List<OrderItem> orderItems;

    @Before
    public void setUp() {
        // Set fromEmail using reflection
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@example.com")
                .fullName("Test User")
                .build();

        Address testAddress = Address.builder()
                .id(1L)
                .user(testUser)
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("123 Main St")
                .city("Hanoi")
                .isDefault(true)
                .build();

        Product testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .build();

        testOrder = Order.builder()
                .id(1L)
                .orderNumber("ORD-123")
                .user(testUser)
                .shippingAddress(testAddress)
                .subtotal(new BigDecimal("200.00"))
                .shippingFee(new BigDecimal("20.00"))
                .tax(new BigDecimal("20.00"))
                .totalAmount(new BigDecimal("240.00"))
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .paymentMethod("COD")
                .orderedAt(LocalDateTime.now())
                .build();

        testOrderItem = OrderItem.builder()
                .id(1L)
                .order(testOrder)
                .product(testProduct)
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("100.00"))
                .subtotal(new BigDecimal("200.00"))
                .build();

        orderItems = Arrays.asList(testOrderItem);
    }

    /**
     * EMAIL_001: testSendOrderConfirmation_Success
     * Gửi email xác nhận đơn hàng thành công
     */
    @Test
    public void testSendOrderConfirmation_Success() throws Exception {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(orderItems);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email content</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // When
        emailService.sendOrderConfirmation(1L, 1L);

        // Wait for async execution
        Thread.sleep(1000);

        // Then
        verify(orderRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1)).findByOrderId(1L);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    /**
     * EMAIL_002: testSendOrderStatusUpdate_Success
     * Gửi email cập nhật trạng thái đơn hàng
     */
    @Test
    public void testSendOrderStatusUpdate_Success() throws Exception {
        // Given
        testOrder.setStatus(Order.OrderStatus.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(orderItems);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email content</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // When
        emailService.sendOrderStatusUpdate(1L, 1L);

        // Wait for async execution
        Thread.sleep(1000);

        // Then
        verify(orderRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1)).findByOrderId(1L);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }
}
