/**
 * Cart Mobile Enhancements
 * Xử lý tương tác cho giỏ hàng trên mobile
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
        setupCollapsibleSummary();
    }

    /**
     * Setup Collapsible Cart Summary
     */
    function setupCollapsibleSummary() {
        // Only on mobile
        if (window.innerWidth >= 1024) return;

        const summary = document.querySelector('.cart-summary');
        if (!summary) return;
        
        // Check if already initialized
        if (summary.querySelector('.cart-summary__header')) return;

        // Find elements - using actual class names from HTML
        const promoSection = summary.querySelector('.promo-code');
        const cartActions = summary.querySelector('.cart-actions');
        
        // Get all 3 buttons from cart-actions (keep all outside collapsible)
        let checkoutBtn = null;
        let continueBtn = null;
        let clearBtn = null;
        
        if (cartActions) {
            const buttons = cartActions.querySelectorAll('.cart-btn');
            buttons.forEach(btn => {
                if (btn.classList.contains('cart-btn--primary') || btn.textContent.includes('thanh toán')) {
                    checkoutBtn = btn;
                } else if (btn.classList.contains('cart-btn--secondary') || btn.textContent.includes('Tiếp tục')) {
                    continueBtn = btn;
                } else if (btn.classList.contains('cart-btn--danger') || btn.textContent.includes('Xóa')) {
                    clearBtn = btn;
                }
            });
        }
        
        // Create header structure
        const header = document.createElement('div');
        header.className = 'cart-summary__header';
        
        const headerLeft = document.createElement('div');
        headerLeft.className = 'cart-summary__header-left';
        
        const headerTitle = document.createElement('div');
        headerTitle.className = 'cart-summary__header-title';
        headerTitle.textContent = 'Tổng đơn hàng';
        
        const headerTotal = document.createElement('div');
        headerTotal.className = 'cart-summary__header-total';
        headerTotal.id = 'mobile-total-display';
        
        // Get total from existing summary
        const totalElement = summary.querySelector('.cart-summary__row--total span:last-child, #total');
        if (totalElement) {
            headerTotal.textContent = totalElement.textContent;
        }
        
        headerLeft.appendChild(headerTitle);
        headerLeft.appendChild(headerTotal);
        
        const toggleBtn = document.createElement('button');
        toggleBtn.className = 'cart-summary__toggle';
        toggleBtn.innerHTML = '▼';
        toggleBtn.setAttribute('aria-label', 'Toggle summary');
        toggleBtn.setAttribute('type', 'button');
        
        header.appendChild(headerLeft);
        header.appendChild(toggleBtn);
        
        // Create body for collapsible content (details only)
        const body = document.createElement('div');
        body.className = 'cart-summary__body';
        
        const content = document.createElement('div');
        content.className = 'cart-summary__content';
        
        // Create buttons wrapper
        const buttonsWrapper = document.createElement('div');
        buttonsWrapper.className = 'cart-summary__buttons';
        
        // Move children to appropriate sections
        const children = Array.from(summary.children);
        children.forEach(child => {
            // Skip only promo (cart-actions will go into collapsible)
            if (child === promoSection) {
                return;
            }
            // Move everything else to collapsible content (including cart-actions)
            content.appendChild(child);
        });
        
        body.appendChild(content);
        
        // Clear summary and rebuild structure
        summary.innerHTML = '';
        
        // Add in order: promo, header, body, buttons
        if (promoSection) {
            promoSection.classList.add('promo-code-section');
            summary.appendChild(promoSection);
        }
        summary.appendChild(header);
        summary.appendChild(body);
        summary.appendChild(buttonsWrapper);
        
        // Add all 3 buttons to wrapper (outside collapsible)
        if (checkoutBtn) {
            checkoutBtn.classList.add('cart-summary__checkout');
            buttonsWrapper.appendChild(checkoutBtn);
        }
        if (continueBtn) {
            continueBtn.classList.add('continue-shopping');
            buttonsWrapper.appendChild(continueBtn);
        }
        if (clearBtn) {
            clearBtn.classList.add('clear-cart-btn');
            buttonsWrapper.appendChild(clearBtn);
        }
        
        // Toggle functionality
        let isExpanded = false;
        
        header.addEventListener('click', () => {
            isExpanded = !isExpanded;
            
            if (isExpanded) {
                body.classList.add('expanded');
                toggleBtn.classList.add('expanded');
                toggleBtn.innerHTML = '▲';
            } else {
                body.classList.remove('expanded');
                toggleBtn.classList.remove('expanded');
                toggleBtn.innerHTML = '▼';
            }
        });
        
        // Update total when cart changes
        const observer = new MutationObserver(() => {
            const totalElement = content.querySelector('.cart-summary__row--total span:last-child, #total');
            if (totalElement && headerTotal) {
                headerTotal.textContent = totalElement.textContent;
            }
        });
        
        observer.observe(content, {
            childList: true,
            subtree: true,
            characterData: true
        });
    }

    /**
     * Handle Window Resize
     */
    let resizeTimer;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
            // Reinit on resize
            if (window.innerWidth < 1024) {
                setupCollapsibleSummary();
            }
        }, 250);
    });

})();
