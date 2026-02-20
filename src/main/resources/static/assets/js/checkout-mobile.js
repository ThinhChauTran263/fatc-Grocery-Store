/**
 * Checkout Mobile Enhancements
 * Tối ưu trang checkout cho mobile
 */

(function() {
    'use strict';

    // Wait for DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    function init() {
        // Only run on mobile
        if (window.innerWidth >= 1024) return;
        
        setupPaymentMethodsToggle();
        // Disabled collapsible cards for checkout page
        // setupCollapsibleCards();
    }

    /**
     * Setup Collapsible Checkout Cards
     */
    function setupCollapsibleCards() {
        const checkoutCards = document.querySelectorAll('.checkout-card');
        if (checkoutCards.length === 0) return;
        
        checkoutCards.forEach((card, index) => {
            const header = card.querySelector('.checkout-card__header');
            const body = card.querySelector('.checkout-card__body');
            
            if (!header || !body) return;
            
            // Skip payment card (keep it always expanded)
            if (card.classList.contains('payment-card') || header.textContent.includes('Phương thức thanh toán')) {
                card.classList.add('payment-card');
                return;
            }
            
            // Check if already initialized
            if (header.querySelector('.checkout-card__toggle')) return;
            
            // Wrap existing content in header-left
            const headerContent = Array.from(header.children);
            const headerLeft = document.createElement('div');
            headerLeft.className = 'checkout-card__header-left';
            headerContent.forEach(child => headerLeft.appendChild(child));
            header.appendChild(headerLeft);
            
            // Add toggle icon
            const toggleIcon = document.createElement('span');
            toggleIcon.className = 'checkout-card__toggle';
            toggleIcon.innerHTML = '▼';
            header.appendChild(toggleIcon);
            
            // Set initial state based on card type
            let isExpanded = false;
            
            // Delivery info (Thông tin giao hàng) - expanded by default
            if (header.textContent.includes('Thông tin giao hàng') || header.textContent.includes('Địa chỉ')) {
                isExpanded = true;
            }
            // Order summary (Tổng đơn hàng) - collapsed by default
            else if (header.textContent.includes('Tổng đơn hàng') || header.textContent.includes('đơn hàng')) {
                isExpanded = false;
            }
            // Products (Sản phẩm) - collapsed by default
            else if (header.textContent.includes('Sản phẩm')) {
                isExpanded = false;
            }
            
            if (isExpanded) {
                body.classList.add('expanded');
                toggleIcon.classList.add('expanded');
            }
            
            // Toggle functionality
            header.addEventListener('click', () => {
                isExpanded = !isExpanded;
                
                if (isExpanded) {
                    body.classList.add('expanded');
                    toggleIcon.classList.add('expanded');
                } else {
                    body.classList.remove('expanded');
                    toggleIcon.classList.remove('expanded');
                }
            });
        });
    }

    /**
     * Setup Payment Methods Toggle for Mobile
     */
    function setupPaymentMethodsToggle() {
        const paymentMethodsContainer = document.querySelector('.payment-methods');
        if (!paymentMethodsContainer) return;
        
        // Check if already initialized
        if (paymentMethodsContainer.querySelector('.payment-methods-toggle')) return;
        
        // Find all payment methods
        const paymentMethods = paymentMethodsContainer.querySelectorAll('.payment-method');
        if (paymentMethods.length === 0) return;
        
        // Mark the selected payment method
        paymentMethods.forEach(method => {
            const radio = method.querySelector('input[type="radio"]');
            if (radio && radio.checked) {
                method.classList.add('payment-method--selected');
            }
            
            // Update selected class when radio changes
            if (radio) {
                radio.addEventListener('change', function() {
                    paymentMethods.forEach(m => m.classList.remove('payment-method--selected'));
                    if (this.checked) {
                        method.classList.add('payment-method--selected');
                    }
                });
            }
        });
        
        // Create toggle button
        const toggleBtn = document.createElement('button');
        toggleBtn.type = 'button';
        toggleBtn.className = 'payment-methods-toggle';
        toggleBtn.innerHTML = `
            <span>Xem thêm phương thức thanh toán</span>
            <span class="payment-methods-toggle-icon">▼</span>
        `;
        
        // Add toggle button after payment methods
        paymentMethodsContainer.appendChild(toggleBtn);
        
        // Toggle functionality
        let isExpanded = false;
        
        toggleBtn.addEventListener('click', () => {
            isExpanded = !isExpanded;
            
            if (isExpanded) {
                paymentMethodsContainer.classList.add('expanded');
                toggleBtn.classList.add('expanded');
                toggleBtn.querySelector('span:first-child').textContent = 'Thu gọn';
            } else {
                paymentMethodsContainer.classList.remove('expanded');
                toggleBtn.classList.remove('expanded');
                toggleBtn.querySelector('span:first-child').textContent = 'Xem thêm phương thức thanh toán';
            }
        });
    }

    /**
     * Handle Window Resize
     */
    let resizeTimer;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
            // Reinit on resize to mobile
            if (window.innerWidth < 1024) {
                setupPaymentMethodsToggle();
            }
        }, 250);
    });

})();
