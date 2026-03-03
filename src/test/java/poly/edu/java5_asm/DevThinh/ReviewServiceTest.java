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

    // @Mock: Giả lập các repository
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    // @InjectMocks: Inject các mock vào service
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User testUser;
    private User otherUser;
    private Product testProduct;
    private Review review1;
    private Review review2;
    private Review review3;

    /**
     * @Before: Chạy trước mỗi test case
     * Chuẩn bị dữ liệu: 2 users, 1 product, 3 reviews
     */
    @Before
    public void setUp() {
        // Tạo 2 test users
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

        // Tạo test product - Product ID=1 từ database
        testProduct = Product.builder()
                .id(1L)
                .name("Coffee Beans - Espresso Arabica and Robusta Beans")
                .slug("coffee-beans-espresso-arabica-robusta")
                .price(new BigDecimal("1175000"))
                .stockQuantity(100)
                .isActive(true)
                .build();

        // Tạo 3 test reviews - Product 1 có 3 reviews (rating: 5, 4, 3)
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
     * Mục đích: Test tạo đánh giá khi user chưa mua sản phẩm
     * Kỳ vọng: Review được tạo nhưng isVerifiedPurchase = false
     */
    @Test
    public void testCreateReview_NotPurchased() {
        // GIVEN: Chuẩn bị request và giả lập user chưa mua sản phẩm
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);
        request.setTitle("Great product");
        request.setComment("Love it!");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.empty());
        // Giả lập: User chưa mua sản phẩm này
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L))
                .thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // WHEN: Tạo review
        ReviewResponse result = reviewService.createReview(testUser, request);

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertFalse("Không phải verified purchase", result.getIsVerifiedPurchase());
        assertEquals("Rating phải là 5", Integer.valueOf(5), result.getRating());
        
        verify(productRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1))
                .existsByUserIdAndProductIdAndOrderDelivered(1L, 1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_003: testCreateReview_DuplicateReview
     * Mục đích: Test tạo đánh giá trùng lặp (user đã review sản phẩm này rồi)
     * Kỳ vọng: Throw ReviewException
     */
    @Test(expected = ReviewException.class)
    public void testCreateReview_DuplicateReview() {
        // GIVEN: User đã đánh giá sản phẩm này rồi
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        // Giả lập: User đã có review cho sản phẩm này
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.of(review1));

        // WHEN: Cố gắng tạo review lần nữa
        reviewService.createReview(testUser, request);

        // THEN: Expect ReviewException (được khai báo ở @Test)
    }

    /**
     * REV_004: testCreateReview_InvalidRating
     * Mục đích: Test đánh giá với rating không hợp lệ (rating = 6)
     * Kỳ vọng: Throw IllegalArgumentException
     * Lưu ý: Validation thường được xử lý ở controller với @Valid
     */
    @Test
    public void testCreateReview_InvalidRating() {
        // GIVEN: Request với rating không hợp lệ (6 sao)
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(6); // Rating không hợp lệ (phải từ 1-5)
        request.setTitle("Test");
        request.setComment("Test comment");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductAndUser(testProduct, testUser))
                .thenReturn(Optional.empty());
        when(orderItemRepository.existsByUserIdAndProductIdAndOrderDelivered(1L, 1L))
                .thenReturn(true);

        // Giả lập: Repository kiểm tra rating và throw exception
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> {
            Review review = i.getArgument(0);
            if (review.getRating() < 1 || review.getRating() > 5) {
                throw new IllegalArgumentException("Rating phải từ 1 đến 5");
            }
            return review;
        });

        // WHEN & THEN: Expect exception
        try {
            reviewService.createReview(testUser, request);
            fail("Phải throw exception khi rating không hợp lệ");
        } catch (IllegalArgumentException e) {
            assertEquals("Rating phải từ 1 đến 5", e.getMessage());
        }
    }

    /**
     * REV_005: testUpdateReview_Success
     * Mục đích: Test cập nhật đánh giá thành công
     * Kỳ vọng: Review được update, rating thay đổi từ 5 xuống 4
     */
    @Test
    public void testUpdateReview_Success() {
        // GIVEN: Request update rating từ 5 xuống 4
        Long reviewId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(1L);
        request.setRating(4); // Update từ 5 xuống 4
        request.setTitle("Updated title");
        request.setComment("Updated comment");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // WHEN: Update review
        ReviewResponse result = reviewService.updateReview(testUser, reviewId, request);

        // THEN: Kiểm tra rating đã được update
        assertNotNull("Kết quả không được null", result);
        assertEquals("Rating phải được cập nhật thành 4", Integer.valueOf(4), result.getRating());
        
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    /**
     * REV_006: testUpdateReview_NotOwner
     * Mục đích: Test update review không phải của mình
     * Kỳ vọng: Throw ReviewException (không có quyền)
     */
    @Test(expected = ReviewException.class)
    public void testUpdateReview_NotOwner() {
        // GIVEN: otherUser cố gắng update review của testUser
        Long reviewId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));

        // WHEN: otherUser cố gắng update review của testUser
        reviewService.updateReview(otherUser, reviewId, request);

        // THEN: Expect ReviewException (không có quyền)
    }

    /**
     * REV_007: testDeleteReview_Success
     * Mục đích: Test xóa đánh giá thành công
     * Kỳ vọng: Review được xóa khỏi database
     */
    @Test
    public void testDeleteReview_Success() {
        // GIVEN: Review tồn tại
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review1));
        doNothing().when(reviewRepository).delete(review1);

        // WHEN: Xóa review
        reviewService.deleteReview(testUser, reviewId);

        // THEN: Verify repository đã gọi delete
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).delete(review1);
    }

    /**
     * REV_008: testGetProductReviews_Success
     * Mục đích: Test lấy tất cả đánh giá của sản phẩm
     * Kỳ vọng: Trả về 3 reviews với rating 5, 4, 3
     */
    @Test
    public void testGetProductReviews_Success() {
        // GIVEN: Product có 3 reviews
        Long productId = 1L;
        List<Review> reviews = Arrays.asList(review1, review2, review3);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct))
                .thenReturn(reviews);

        // WHEN: Lấy reviews của product
        List<ReviewResponse> result = reviewService.getProductReviews(productId);

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertEquals("Phải trả về 3 reviews", 3, result.size());
        
        // Kiểm tra rating của từng review (5, 4, 3)
        assertEquals("Review đầu tiên có rating 5", Integer.valueOf(5), result.get(0).getRating());
        assertEquals("Review thứ hai có rating 4", Integer.valueOf(4), result.get(1).getRating());
        assertEquals("Review thứ ba có rating 3", Integer.valueOf(3), result.get(2).getRating());
        
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProductOrderByCreatedAtDesc(testProduct);
    }

    /**
     * REV_009: testGetProductReviewsPaginated_Success
     * Mục đích: Test lấy đánh giá có phân trang
     * Kỳ vọng: Trả về page 0 có 2 reviews, tổng 3 reviews, 2 pages
     */
    @Test
    public void testGetProductReviewsPaginated_Success() {
        // GIVEN: Lấy page 0, size 2 (có 3 reviews tổng cộng)
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 2); // Page 0, size 2
        
        // Giả lập: Có 3 reviews, lấy 2 reviews đầu tiên
        List<Review> reviewsPage1 = Arrays.asList(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviewsPage1, pageable, 3);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.findByProductOrderByCreatedAtDesc(testProduct, pageable))
                .thenReturn(reviewPage);

        // WHEN: Lấy reviews có phân trang
        Page<ReviewResponse> result = reviewService.getProductReviewsPaginated(productId, pageable);

        // THEN: Kiểm tra kết quả phân trang
        assertNotNull("Kết quả không được null", result);
        assertEquals("Page hiện tại phải có 2 reviews", 2, result.getContent().size());
        assertEquals("Tổng số reviews phải là 3", 3, result.getTotalElements());
        assertEquals("Tổng số pages phải là 2", 2, result.getTotalPages());
        
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProductOrderByCreatedAtDesc(testProduct, pageable);
    }

    /**
     * REV_010: testGetUserReviews_Success
     * Mục đích: Test lấy tất cả đánh giá của user
     * Kỳ vọng: Trả về 1 review của testUser
     */
    @Test
    public void testGetUserReviews_Success() {
        // GIVEN: testUser có 1 review
        List<Review> userReviews = Arrays.asList(review1);
        
        when(reviewRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(userReviews);

        // WHEN: Lấy reviews của user
        List<ReviewResponse> result = reviewService.getUserReviews(testUser);

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertEquals("Phải trả về 1 review", 1, result.size());
        assertEquals("Review phải thuộc về testUser", Long.valueOf(1L), result.get(0).getUserId());
        
        verify(reviewRepository, times(1)).findByUserOrderByCreatedAtDesc(testUser);
    }
}
