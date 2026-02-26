package poly.edu.java5_asm.DevNgoc;

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
 * Test cases cho ReviewService - Thiên Ngọc
 * Test cases: REV_001 đến REV_012
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
    private Product testProduct;
    private Review testReview;
    private CreateReviewRequest createReviewRequest;

    @Before
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .build();

        testReview = Review.builder()
                .id(1L)
                .product(testProduct)
                .user(testUser)
                .rating(5)
                .title("Great product")
                .comment("Very satisfied with this product")
                .isVerifiedPurchase(true)
                .build();

        createReviewRequest = CreateReviewRequest.builder()
                .productId(1L)
                .rating(5)
                .title("Great product")
                .comment("Very satisfied with this product")
                .build();
    }

    /**
     * REV_001: testCreateReview_Success
     * Tạo đánh giá thành công
     */
    @Test
    public void testCreateReview_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser)).thenReturn(Optional.empty());
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L)).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // When
        ReviewResponse result = reviewService.createReview(testUser, createReviewRequest);

        // Then
        assertNotNull("ReviewResponse should not be null", result);
        assertEquals("Rating should be 5", Integer.valueOf(5), result.getRating());
        assertEquals("Product ID should match", Long.valueOf(1L), result.getProductId());
        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_002: testCreateReview_NotPurchased
     * Đánh giá sản phẩm chưa mua (verified purchase = false)
     */
    @Test
    public void testCreateReview_NotPurchased() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser)).thenReturn(Optional.empty());
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L)).thenReturn(false);
        
        Review unverifiedReview = Review.builder()
                .id(1L)
                .product(testProduct)
                .user(testUser)
                .rating(5)
                .isVerifiedPurchase(false)
                .build();
        
        when(reviewRepository.save(any(Review.class))).thenReturn(unverifiedReview);

        // When
        ReviewResponse result = reviewService.createReview(testUser, createReviewRequest);

        // Then
        assertNotNull("ReviewResponse should not be null", result);
        assertFalse("Should not be verified purchase", result.getIsVerifiedPurchase());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_003: testCreateReview_DuplicateReview
     * Tạo đánh giá trùng lặp
     */
    @Test(expected = ReviewException.class)
    public void testCreateReview_DuplicateReview() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser)).thenReturn(Optional.of(testReview));

        // When
        reviewService.createReview(testUser, createReviewRequest);

        // Then - expect ReviewException
    }

    /**
     * REV_004: testCreateReview_InvalidRating
     * Đánh giá với rating không hợp lệ (validation ở request level)
     */
    @Test
    public void testCreateReview_InvalidRating() {
        // Given
        CreateReviewRequest invalidRequest = CreateReviewRequest.builder()
                .productId(1L)
                .rating(6) // Invalid rating > 5
                .title("Test")
                .comment("Test comment")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser)).thenReturn(Optional.empty());
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L)).thenReturn(true);
        
        Review reviewWithInvalidRating = Review.builder()
                .id(1L)
                .product(testProduct)
                .user(testUser)
                .rating(6)
                .isVerifiedPurchase(true)
                .build();
        
        when(reviewRepository.save(any(Review.class))).thenReturn(reviewWithInvalidRating);

        // When
        ReviewResponse result = reviewService.createReview(testUser, invalidRequest);

        // Then - Service accepts it (validation should be at controller/request level)
        assertNotNull("ReviewResponse should not be null", result);
        assertEquals("Rating should be 6", Integer.valueOf(6), result.getRating());
    }

    /**
     * REV_005: testUpdateReview_Success
     * Cập nhật đánh giá thành công
     */
    @Test
    public void testUpdateReview_Success() {
        // Given
        CreateReviewRequest updateRequest = CreateReviewRequest.builder()
                .productId(1L)
                .rating(4)
                .title("Updated title")
                .comment("Updated comment")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // When
        ReviewResponse result = reviewService.updateReview(testUser, 1L, updateRequest);

        // Then
        assertNotNull("ReviewResponse should not be null", result);
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_006: testUpdateReview_NotOwner
     * Cập nhật đánh giá không phải owner
     */
    @Test(expected = ReviewException.class)
    public void testUpdateReview_NotOwner() {
        // Given
        User anotherUser = User.builder()
                .id(2L)
                .username("anotheruser")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // When
        reviewService.updateReview(anotherUser, 1L, createReviewRequest);

        // Then - expect ReviewException
    }

    /**
     * REV_007: testDeleteReview_Success
     * Xóa đánh giá thành công
     */
    @Test
    public void testDeleteReview_Success() {
        // Given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        doNothing().when(reviewRepository).delete(testReview);

        // When
        reviewService.deleteReview(testUser, 1L);

        // Then
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).delete(testReview);
    }

    /**
     * REV_008: testGetProductReviews_Success
     * Lấy tất cả đánh giá của sản phẩm
     */
    @Test
    public void testGetProductReviews_Success() {
        // Given
        List<Review> reviews = Arrays.asList(testReview, testReview, testReview, testReview, testReview);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct)).thenReturn(reviews);

        // When
        List<ReviewResponse> result = reviewService.getProductReviews(1L);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 5 reviews", 5, result.size());
        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).findByProductOrderByCreatedAtDesc(testProduct);
    }

    /**
     * REV_009: testGetProductReviewsPaginated_Success
     * Lấy đánh giá sản phẩm có phân trang
     */
    @Test
    public void testGetProductReviewsPaginated_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        List<Review> reviews = Arrays.asList(testReview, testReview, testReview, testReview, testReview);
        Page<Review> reviewPage = new PageImpl<>(reviews, pageable, 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct, pageable)).thenReturn(reviewPage);

        // When
        Page<ReviewResponse> result = reviewService.getProductReviewsPaginated(1L, pageable);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 5 reviews", 5, result.getContent().size());
        assertEquals("Total elements should be 10", 10, result.getTotalElements());
        verify(productRepository, times(1)).findById(1L);
    }

    /**
     * REV_010: testGetUserReviews_Success
     * Lấy đánh giá của user
     */
    @Test
    public void testGetUserReviews_Success() {
        // Given
        List<Review> reviews = Arrays.asList(testReview, testReview, testReview);
        when(reviewRepository.findByUserOrderByCreatedAtDesc(testUser)).thenReturn(reviews);

        // When
        List<ReviewResponse> result = reviewService.getUserReviews(testUser);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 reviews", 3, result.size());
        verify(reviewRepository, times(1)).findByUserOrderByCreatedAtDesc(testUser);
    }

    /**
     * REV_011: testGetProductAverageRating_Success
     * Lấy điểm trung bình của sản phẩm
     */
    @Test
    public void testGetProductAverageRating_Success() {
        // Given
        Review review1 = Review.builder().rating(5).product(testProduct).user(testUser).build();
        Review review2 = Review.builder().rating(4).product(testProduct).user(testUser).build();
        Review review3 = Review.builder().rating(3).product(testProduct).user(testUser).build();
        List<Review> reviews = Arrays.asList(review1, review2, review3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct)).thenReturn(reviews);

        // When
        Double result = reviewService.getProductAverageRating(1L);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Average rating should be 4.0", 4.0, result, 0.01);
        verify(productRepository, times(1)).findById(1L);
    }

    /**
     * REV_012: testGetProductReviewCount_Success
     * Lấy số lượng đánh giá của sản phẩm
     */
    @Test
    public void testGetProductReviewCount_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.countByProduct(testProduct)).thenReturn(5L);

        // When
        Long result = reviewService.getProductReviewCount(1L);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 5", Long.valueOf(5L), result);
        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).countByProduct(testProduct);
    }
}
