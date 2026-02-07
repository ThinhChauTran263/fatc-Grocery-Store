package poly.edu.java5_asm.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import poly.edu.java5_asm.module.brand.dto.response.BrandResponse;
import poly.edu.java5_asm.module.category.dto.response.CategoryResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.wishlist.repository.WishlistRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper chuyển đổi Entity sang DTO
 * Tránh trả về toàn bộ Entity (gây circular reference và lộ data nhạy cảm)
 */
@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final WishlistRepository wishlistRepository;

    // Chuyển Product Entity → ProductResponse DTO (không có user context)
    public ProductResponse toResponse(Product product) {
        return toResponse(product, null);
    }

    // Chuyển Product Entity → ProductResponse DTO (có user context)
    public ProductResponse toResponse(Product product, Long userId) {
        if (product == null) return null;

        // Check if product is in user's wishlist
        boolean inWishlist = false;
        if (userId != null) {
            inWishlist = wishlistRepository.existsByUserIdAndProductId(userId, product.getId());
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .sku(product.getSku())
                .shortDescription(product.getShortDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .stockQuantity(product.getStockQuantity())
                .averageRating(calculateAverageRating(product))
                .totalReviews(product.getReviews() != null ? product.getReviews().size() : 0)
                .isInStock(product.getStockQuantity() != null && product.getStockQuantity() > 0)
                .isFeatured(product.getIsFeatured())
                .isActive(product.getIsActive())
                .inWishlist(inWishlist)
                .build();
    }

    // Chuyển List<Product> → List<ProductResponse> (không có user context)
    public List<ProductResponse> toResponseList(List<Product> products) {
        return toResponseList(products, null);
    }

    // Chuyển List<Product> → List<ProductResponse> (có user context)
    public List<ProductResponse> toResponseList(List<Product> products, Long userId) {
        if (products == null) return List.of();

        return products.stream()
                .map(product -> toResponse(product, userId))
                .collect(Collectors.toList());
    }

    // Chuyển Page<Product> → ProductListResponse (có phân trang, không có user context)
    public ProductListResponse toProductListResponse(Page<Product> productPage) {
        return toProductListResponse(productPage, null);
    }

    // Chuyển Page<Product> → ProductListResponse (có phân trang và user context)
    public ProductListResponse toProductListResponse(Page<Product> productPage, Long userId) {
        if (productPage == null) return null;

        return ProductListResponse.builder()
                .products(toResponseList(productPage.getContent(), userId))
                .currentPage(productPage.getNumber())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .pageSize(productPage.getSize())
                .build();
    }

    // Chuyển Category Entity → CategoryResponse DTO
    public CategoryResponse toCategoryResponse(Category category, Long productCount) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .iconUrl(category.getIconUrl())
                .productCount(productCount)
                .build();
    }

    // Chuyển Brand Entity → BrandResponse DTO
    public BrandResponse toBrandResponse(Brand brand, Long productCount) {
        if (brand == null) return null;

        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .logoUrl(brand.getLogoUrl())
                .productCount(productCount)
                .build();
    }

    // Tính điểm đánh giá trung bình
    private Double calculateAverageRating(Product product) {
        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            return 0.0;
        }

        return product.getReviews().stream()
                .mapToInt(review -> review.getRating())
                .average()
                .orElse(0.0);
    }
}
