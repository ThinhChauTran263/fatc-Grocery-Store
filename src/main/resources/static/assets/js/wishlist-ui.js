/**
 * Wishlist UI Service - Cập nhật giao diện wishlist
 * Đồng bộ badge số lượng sản phẩm yêu thích
 */

(function initWishlistUIService() {
    
    /**
     * Cập nhật badge số lượng wishlist
     */
    async function updateWishlistBadge() {
        try {
            const count = await window.WishlistAPI.getWishlistCount();
            const badge = document.getElementById('header-wishlist-count');
            
            if (badge) {
                if (count > 0) {
                    badge.textContent = count;
                    badge.style.display = 'inline-block';
                } else {
                    badge.textContent = '0';
                    badge.style.display = 'none';
                }
            }
        } catch (error) {
            console.error('Không thể cập nhật badge wishlist:', error);
        }
    }

    /**
     * Khởi tạo khi DOM ready
     */
    function init() {
        // Cập nhật badge ngay khi trang load
        if (window.WishlistAPI) {
            updateWishlistBadge();
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // Xuất ra phạm vi toàn cục để sử dụng từ nơi khác
    window.WishlistUI = {
        updateBadge: updateWishlistBadge
    };
})();
