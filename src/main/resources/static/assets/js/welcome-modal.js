/**
 * Welcome Modal - Hiển thị popup chào mừng chỉ trên trang index
 * Hiển thị 1 lần khi mới truy cập và sau 30 phút sẽ hiển thị lại
 */

(function initWelcomeModal() {
    const STORAGE_KEY = 'welcomeModalLastShown';
    const SHOW_INTERVAL = 30 * 60 * 1000; // 30 phút tính bằng milliseconds

    /**
     * Kiểm tra xem có phải trang index không
     */
    function isIndexPage() {
        const currentPath = window.location.pathname;
        return currentPath === '/' || currentPath === '/index' || currentPath === '/index.html';
    }

    /**
     * Kiểm tra xem có nên hiển thị modal không
     */
    function shouldShowModal() {
        try {
            const lastShown = localStorage.getItem(STORAGE_KEY);

            // Nếu chưa từng hiển thị, cho phép hiển thị
            if (!lastShown) {
                return true;
            }

            // Kiểm tra thời gian đã qua
            const lastShownTime = parseInt(lastShown, 10);
            const currentTime = Date.now();
            const timePassed = currentTime - lastShownTime;

            // Nếu đã qua 30 phút, cho phép hiển thị lại
            return timePassed >= SHOW_INTERVAL;
        } catch (error) {
            console.error('[Welcome Modal] Error checking show status:', error);
            return true; // Nếu có lỗi, vẫn hiển thị modal
        }
    }

    /**
     * Lưu thời gian hiển thị modal
     */
    function saveShowTime() {
        try {
            localStorage.setItem(STORAGE_KEY, Date.now().toString());
            console.log('[Welcome Modal] Show time saved');
        } catch (error) {
            console.error('[Welcome Modal] Error saving show time:', error);
        }
    }

    /**
     * Tạo HTML cho modal
     */
    function createModalHTML() {
        return `
            <div id="welcome-modal-overlay" class="welcome-modal-overlay">
                <div class="welcome-modal-container">
                    <!-- Close Button -->
                    <button class="welcome-modal-close" onclick="closeWelcomeModal()">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="18" y1="6" x2="6" y2="18"></line>
                            <line x1="6" y1="6" x2="18" y2="18"></line>
                        </svg>
                    </button>

                    <!-- Header with Icon -->
                    <div class="welcome-modal-header">
                        <div class="welcome-modal-icon">
                            <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
                                <circle cx="32" cy="32" r="30" stroke="#22d3ee" stroke-width="2"/>
                                <path d="M32 16v16m0 8v4" stroke="#22d3ee" stroke-width="2" stroke-linecap="round"/>
                                <circle cx="32" cy="44" r="2" fill="#22d3ee"/>
                                <path d="M20 32h24" stroke="#22d3ee" stroke-width="2" stroke-linecap="round"/>
                                <path d="M24 28l-4 4 4 4" stroke="#22d3ee" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                <path d="M40 28l4 4-4 4" stroke="#22d3ee" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                        </div>
                    </div>

                    <!-- Content -->
                    <div class="welcome-modal-content">
                        <h2 class="welcome-modal-title">Lời chào từ Đội ngũ Phát triển</h2>

                        <p class="welcome-modal-text">
                            Cảm ơn bạn đã ghé thăm <strong>BigC GroceryStore</strong>! Đây là không gian dành riêng cho việc trải nghiệm thiết kế và tính năng người dùng. Vì vậy, các giao dịch mua hàng tại đây chỉ mang tính chất dùng thử.
                        </p>

                        <p class="welcome-modal-text welcome-modal-text--highlight">
                            Chúc bạn có những giây phút trải nghiệm thú vị! 🎉
                        </p>

                        <!-- Features List -->
                        <div class="welcome-modal-features">
                            <div class="feature-item" onclick="navigateToPage('/')">
                                <span class="feature-icon">🛒</span>
                                <span class="feature-text">Khám phá sản phẩm</span>
                            </div>
                            <div class="feature-item" onclick="navigateToPage('/product/1')">
                                <span class="feature-icon">⭐</span>
                                <span class="feature-text">Đánh giá sản phẩm</span>
                            </div>
                            <div class="feature-item" onclick="navigateToPage('/favourite')">
                                <span class="feature-icon">❤️</span>
                                <span class="feature-text">Lưu yêu thích</span>
                            </div>
                            <div class="feature-item" onclick="navigateToPage('/cc-doctor')">
                                <span class="feature-icon">☕</span>
                                <span class="feature-text">Tính toán Caffeine</span>
                            </div>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div class="welcome-modal-footer">
                        <button class="welcome-modal-btn welcome-modal-btn--primary" onclick="closeWelcomeModal()">
                            Bắt đầu khám phá
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Hiển thị modal
     */
    function showWelcomeModal() {
        // Chỉ hiển thị trên trang index
        if (!isIndexPage()) {
            console.log('[Welcome Modal] Not on index page - modal not shown');
            return;
        }

        // Kiểm tra xem có nên hiển thị modal không
        if (!shouldShowModal()) {
            console.log('[Welcome Modal] Modal already shown recently - skipping');
            return;
        }

        // Kiểm tra xem modal đã tồn tại chưa
        if (document.getElementById('welcome-modal-overlay')) {
            return;
        }

        // Tạo modal HTML
        const modalHTML = createModalHTML();
        document.body.insertAdjacentHTML('beforeend', modalHTML);

        // Thêm animation
        const overlay = document.getElementById('welcome-modal-overlay');
        setTimeout(() => {
            overlay.classList.add('show');
        }, 100);

        // Lưu thời gian hiển thị
        saveShowTime();

        // Log
        console.log('✓ Welcome modal shown on index page');
    }

    /**
     * Đóng modal
     */
    window.closeWelcomeModal = function() {
        const overlay = document.getElementById('welcome-modal-overlay');
        if (overlay) {
            overlay.classList.remove('show');
            setTimeout(() => {
                overlay.remove();
            }, 300);
            console.log('✓ Welcome modal closed');
        }
    };

    /**
     * Navigate to page and close modal
     */
    window.navigateToPage = function(path) {
        console.log(`[Welcome Modal] Navigating to: ${path}`);
        window.closeWelcomeModal();
        setTimeout(() => {
            window.location.href = path;
        }, 300); // Wait for modal close animation
    };

    /**
     * Khởi tạo khi DOM ready
     */
    document.addEventListener('DOMContentLoaded', () => {
        showWelcomeModal();
    });

    /**
     * Expose function để gọi từ nơi khác
     */
    window.showWelcomeModal = showWelcomeModal;

    /**
     * Reset timer - xóa thời gian đã lưu để modal hiển thị lại ngay lập tức
     */
    window.resetWelcomeModalTimer = function() {
        try {
            localStorage.removeItem(STORAGE_KEY);
            console.log('[Welcome Modal] Timer reset - modal will show on next page load');
        } catch (error) {
            console.error('[Welcome Modal] Error resetting timer:', error);
        }
    };

    /**
     * Lấy thông tin về modal status
     */
    window.getWelcomeModalStatus = function() {
        const isIndex = isIndexPage();
        const shouldShow = shouldShowModal();

        let nextShowTime = null;
        try {
            const lastShown = localStorage.getItem(STORAGE_KEY);
            if (lastShown) {
                const lastShownTime = parseInt(lastShown, 10);
                nextShowTime = new Date(lastShownTime + SHOW_INTERVAL);
            }
        } catch (error) {
            console.error('[Welcome Modal] Error getting status:', error);
        }

        return {
            page: window.location.pathname,
            isIndexPage: isIndex,
            shouldShow: shouldShow,
            showInterval: '30 minutes',
            nextShowTime: nextShowTime ? nextShowTime.toLocaleString('vi-VN') : 'Not set',
            status: isIndex && shouldShow ? 'will_show' : 'not_shown',
            message: !isIndex ? 'Modal only shows on index page' :
                     !shouldShow ? 'Modal shown recently, will show again after 30 minutes' :
                     'Modal will show'
        };
    };

    console.log('✅ Welcome Modal Service initialized');
    console.log('📍 Current page:', window.location.pathname);
    console.log('⏱️ Show interval: 30 minutes');
    console.log('🔄 Behavior: Show once, then every 30 minutes');
})();
