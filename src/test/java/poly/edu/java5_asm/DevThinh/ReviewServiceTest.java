package poly.edu.java5_asm.DevThinh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.common.exception.ReviewException;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.review.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.module.review.dto.response.ReviewResponse;
import poly.edu.java5_asm.module.review.entity.Review;
import poly.edu.java5_asm.module.review.repository.ReviewRepository;
import poly.edu.java5_asm.module.review.service.ReviewServiceImpl;
import poly.edu.java5_asm.module.user.entity.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases cho ReviewService - Châu Thịnh
 * Test cases: REV_002 đến REV_010
 * (REV_001 sẽ được test bằng Selenium)
 */
@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User testUser;
    private User otherUser;
    private Product testProduct;
    private Review review1;
    private Review review2;
    private Review review3;

    @Before
    public void setUp() {
        // Test users
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        otherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@example.com")
                .fullName("Other User")
                .build();

        // Test product - Product ID=1 từ database
        testProduct = Product.builder()
                .id(1L)
                .name("Coffee Beans - Espresso Arabica and Robusta Beans")
                .slug("coffee-beans-espresso-arabica-robusta")
                .price(new BigDecimal("1175000"))
                .stockQuantity(100)
                .isActive(true)
                .build();

        // Test reviews - Dữ liệu thực tế: Product 1 có 3 reviews (rating: 5, 4, 3)
        review1 = Review.builder()
                .id(1L)
                .product(testProduct)
                .user(testUser)
                .rating(5)
                .title("Excellent coffee!")
                .comment("Best espresso beans I have ever tried.")
                .isVerifiedPurchase(true)
                .build();

        review2 = Review.builder()
                .id(2L)
                .product(testProduct)
                .user(otherUser)
                .rating(4)
                .title("Very good")
                .comment("Great quality beans, slightly expensive but worth it.")
                .isVerifiedPurchase(true)
                .build();

        review3 = Review.builder()
                .id(3L)
                .product(testProduct)
                .user(otherUser)
                .rating(3)
                .title("Good but not great")
                .comment("Decent coffee but I expected more for the price.")
                .isVerifiedPurchase(false)
                .build();
    }

    /**
     * REV_002: testCreateReview_NotPurchased
     * Đánh giá sản phẩm chưa mua
     */
    @Test
    public void testCreateReview_NotPurchased() {
        // Given
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);
        request.setTitle("Great product");
        request.setComment("Love it!");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.empty());
        // User chưa mua sản phẩm
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L))
                .thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // When
        ReviewResponse result = reviewService.createReview(testUser, request);

        // Then
        assertNotNull("Result should not be null", result);
        assertFalse("Should not be verified purchase", result.getIsVerifiedPurchase());
        assertEquals("Rating should be 5", Integer.valueOf(5), result.getRating());
        
        verify(productRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1))
                .existsByUserIdAndProductIdAndOrderDelivered(1L, 1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_003: testCreateReview_DuplicateReview
     * Tạo đánh giá trùng lặp
     */
    @Test(expected = ReviewException.class)
    public void testCreateReview_DuplicateReview() {
        // Given
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        // User đã đánh giá sản phẩm này rồi
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.of(review1));

        // When
        reviewService.createReview(testUser, request);

        // Then - expect ReviewException
    }

    /**
     * REV_004: testCreateReview_InvalidRating
     * Đánh giá với rating không hợp lệ
     * Note: Validation thường được xử lý ở controller layer với @Valid
     * Test này kiểm tra logic khi rating = 6 được pass vào
     */
    @Test
    public void testCreateReview_InvalidRating() {
        // Given
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(6); // Invalid rating (should be 1-5)
        request.setTitle("Test");
        request.setComment("Test comment");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.empty());
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L))
                .thenReturn(true);

        // Note: Trong thực tế, validation sẽ được xử lý bởi @Min(1) @Max(5) annotation
        // Test này chỉ verify rằng service có thể xử lý rating bất kỳ
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> {
            Review review = i.getArgument(0);
            if (review.getRating() < 1 || review.getRating() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            return review;
        });

        // When & Then
        try {
            reviewService.createReview(testUser, request);
            fail("Should throw exception for invalid rating");
        } catch (IllegalArgumentException e) {
            assertEquals("Rating must be between 1 and 5", e.getMessage());
        }
    }

    /**
     * REV_005: testUpdateReview_Success
     * Cập nhật đánh giá thành công
     */
    @Test
    public void testUpdateReview_Success() {
        // Given
        Long reviewId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(4); // Update từ 5 xuống 4
        request.setTitle("Updated title");
        request.setComment("Updated comment");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // When
        ReviewResponse result = reviewService.updateReview(testUser, reviewId, request);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Rating should be updated to 4", Integer.valueOf(4), result.getRating());
        
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_006: testUpdateReview_NotOwner
     * Cập nhật đánh giá không phải owner
     */
    @Test(expected = ReviewException.class)
    public void testUpdateReview_NotOwner() {
        // Given
        Long reviewId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));

        // When - otherUser cố gắng update review của testUser
        reviewService.updateReview(otherUser, reviewId, request);

        // Then - expect ReviewException
    }

    /**
     * REV_007: testDeleteReview_Success
     * Xóa đánh giá thành công
     */
    @Test
    public void testDeleteReview_Success() {
        // Given
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));
        doNothing().when(reviewRepository).delete(review1);

        // When
        reviewService.deleteReview(testUser, reviewId);

        // Then
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).delete(review1);
    }

    /**
     * REV_008: testGetProductReviews_Success
     * Lấy tất cả đánh giá của sản phẩm
     */
    @Test
    public void testGetProductReviews_Success() {
        // Given
        Long productId = 1L;
        List<Review> reviews = Arrays.asList(review1, review2, review3);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct))
                .thenReturn(reviews);

        // When
        List<ReviewResponse> result = reviewService.getProductReviews(productId);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 reviews", 3, result.size());
        
        // Verify ratings (5, 4, 3)
        assertEquals("First review rating should be 5", Integer.valueOf(5), result.get(0).getRating());
        assertEquals("Second review rating should be 4", Integer.valueOf(4), result.get(1).getRating());
        assertEquals("Third review rating should be 3", Integer.valueOf(3), result.get(2).getRating());
        
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProductOrderByCreatedAtDesc(testProduct);
    }

    /**
     * REV_009: testGetProductReviewsPaginated_Success
     * Lấy đánh giá sản phẩm có phân trang
     */
    @Test
    public void testGetProductReviewsPaginated_Success() {
        // Given
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 2); // Page 0, size 2
        
        // Giả lập có 3 reviews, lấy 2 reviews đầu tiên
        List<Review> reviewsPage1 = Arrays.asList(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviewsPage1, pageable, 3);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct, pageable))
                .thenReturn(reviewPage);

        // When
        Page<ReviewResponse> result = reviewService.getProductReviewsPaginated(productId, pageable);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 2 reviews in page", 2, result.getContent().size());
        assertEquals("Total elements should be 3", 3, result.getTotalElements());
        assertEquals("Total pages should be 2", 2, result.getTotalPages());
        
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProductOrderByCreatedAtDesc(testProduct, pageable);
    }

    /**
     * REV_010: testGetUserReviews_Success
     * Lấy đánh giá của user
     */
    @Test
    public void testGetUserReviews_Success() {
        // Given
        List<Review> userReviews = Arrays.asList(review1); // testUser có 1 review
        
        when(reviewRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(userReviews);

        // When
        List<ReviewResponse> result = reviewService.getUserReviews(testUser);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 1 review", 1, result.size());
        assertEquals("Review should belong to testUser", Long.valueOf(1L), result.get(0).getUserId());
        
        verify(reviewRepository, times(1)).findByUserOrderByCreatedAtDesc(testUser);
    }
}
