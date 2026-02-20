/**
 * Mobile Layout JavaScript
 * Xử lý tương tác cho Mobile-First Layout
 */

(function() {
    'use strict';

    // Wait for DOM to be ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    function init() {
        initHamburgerMenu();
        initSearchToggle();
        initSidebarClose();
        updateCartCount();
        updateWishlistCount();
    }

    /**
     * Initialize Hamburger Menu
     */
    function initHamburgerMenu() {
        const hamburgerBtn = document.getElementById('hamburger-btn');
        const sidebar = document.getElementById('mobile-sidebar');
        const body = document.body;

        if (!hamburgerBtn || !sidebar) return;

        hamburgerBtn.addEventListener('click', function() {
            const isActive = sidebar.classList.contains('active');
            
            if (isActive) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        function openSidebar() {
            sidebar.classList.add('active');
            hamburgerBtn.classList.add('active');
            body.classList.add('mobile-sidebar-open');
            
            // Đóng search bar khi mở sidebar (tùy chọn)
            // const mobileSearch = document.getElementById('mobile-search');
            // if (mobileSearch && mobileSearch.classList.contains('active')) {
            //     mobileSearch.classList.remove('active');
            // }
        }

        function closeSidebar() {
            sidebar.classList.remove('active');
            hamburgerBtn.classList.remove('active');
            body.classList.remove('mobile-sidebar-open');
        }

        // Make functions available globally
        window.openMobileSidebar = openSidebar;
        window.closeMobileSidebar = closeSidebar;
    }

    /**
     * Initialize Search Toggle
     */
    function initSearchToggle() {
        const searchToggleBtn = document.getElementById('search-toggle-btn');
        const mobileSearch = document.getElementById('mobile-search');

        if (!searchToggleBtn || !mobileSearch) return;

        searchToggleBtn.addEventListener('click', function(e) {
            e.stopPropagation(); // Prevent event bubbling
            
            const isActive = mobileSearch.classList.contains('active');
            
            if (isActive) {
                closeSearch();
            } else {
                openSearch();
            }
        });

        function openSearch() {
            mobileSearch.classList.add('active');
            searchToggleBtn.classList.add('active');
            
            // Focus on search input with delay for animation
            const searchInput = mobileSearch.querySelector('.mobile-search__input');
            if (searchInput) {
                setTimeout(() => {
                    searchInput.focus();
                }, 350);
            }
        }

        function closeSearch() {
            mobileSearch.classList.remove('active');
            searchToggleBtn.classList.remove('active');
            
            // Clear focus
            const searchInput = mobileSearch.querySelector('.mobile-search__input');
            if (searchInput) {
                searchInput.blur();
            }
        }

        // Close search when clicking outside
        document.addEventListener('click', function(e) {
            if (mobileSearch.classList.contains('active')) {
                if (!mobileSearch.contains(e.target) && !searchToggleBtn.contains(e.target)) {
                    closeSearch();
                }
            }
        });

        // Make functions available globally
        window.openMobileSearch = openSearch;
        window.closeMobileSearch = closeSearch;
    }

    /**
     * Initialize Sidebar Close Handlers
     */
    function initSidebarClose() {
        const sidebar = document.getElementById('mobile-sidebar');
        const overlay = document.getElementById('sidebar-overlay');
        const closeBtn = document.getElementById('sidebar-close-btn');

        if (!sidebar) return;

        // Close on overlay click
        if (overlay) {
            overlay.addEventListener('click', function() {
                if (window.closeMobileSidebar) {
                    window.closeMobileSidebar();
                }
            });
        }

        // Close on close button click
        if (closeBtn) {
            closeBtn.addEventListener('click', function() {
                if (window.closeMobileSidebar) {
                    window.closeMobileSidebar();
                }
            });
        }

        // Close on ESC key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && sidebar.classList.contains('active')) {
                if (window.closeMobileSidebar) {
                    window.closeMobileSidebar();
                }
            }
        });

        // Close sidebar when clicking on a link (except for logout form)
        const sidebarLinks = sidebar.querySelectorAll('.mobile-sidebar__link:not([type="submit"])');
        sidebarLinks.forEach(link => {
            link.addEventListener('click', function() {
                // Small delay to allow navigation to start
                setTimeout(() => {
                    if (window.closeMobileSidebar) {
                        window.closeMobileSidebar();
                    }
                }, 100);
            });
        });
    }

    /**
     * Update Cart Count
     */
    function updateCartCount() {
        const cartBadge = document.getElementById('mobile-cart-count');
        if (!cartBadge) return;

        // Try to get cart count from API or localStorage
        if (typeof CartAPI !== 'undefined' && CartAPI.getCartCount) {
            CartAPI.getCartCount()
                .then(count => {
                    updateBadge(cartBadge, count);
                })
                .catch(err => {
                    console.error('Error getting cart count:', err);
                });
        } else {
            // Fallback: try to get from existing header cart count
            const headerCartCount = document.getElementById('header-cart-count');
            if (headerCartCount) {
                const count = parseInt(headerCartCount.textContent) || 0;
                updateBadge(cartBadge, count);
            }
        }

        // Listen for cart updates
        document.addEventListener('cartUpdated', function(e) {
            if (e.detail && typeof e.detail.count !== 'undefined') {
                updateBadge(cartBadge, e.detail.count);
            }
        });
    }

    /**
     * Update Wishlist Count
     */
    function updateWishlistCount() {
        const wishlistBadge = document.getElementById('sidebar-wishlist-count');
        if (!wishlistBadge) return;

        // Try to get wishlist count from API or localStorage
        if (typeof WishlistAPI !== 'undefined' && WishlistAPI.getWishlistCount) {
            WishlistAPI.getWishlistCount()
                .then(count => {
                    updateBadge(wishlistBadge, count);
                })
                .catch(err => {
                    console.error('Error getting wishlist count:', err);
                });
        } else {
            // Fallback: try to get from existing header wishlist count
            const headerWishlistCount = document.getElementById('header-wishlist-count');
            if (headerWishlistCount) {
                const count = parseInt(headerWishlistCount.textContent) || 0;
                updateBadge(wishlistBadge, count);
            }
        }

        // Listen for wishlist updates
        document.addEventListener('wishlistUpdated', function(e) {
            if (e.detail && typeof e.detail.count !== 'undefined') {
                updateBadge(wishlistBadge, e.detail.count);
            }
        });
    }

    /**
     * Update Badge Display
     */
    function updateBadge(badge, count) {
        if (!badge) return;
        
        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : count;
            badge.style.display = 'flex';
        } else {
            badge.style.display = 'none';
        }
    }

    /**
     * Handle window resize
     */
    let resizeTimer;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            // Close sidebar on desktop breakpoint
            if (window.innerWidth >= 1024) {
                if (window.closeMobileSidebar) {
                    window.closeMobileSidebar();
                }
            }
        }, 250);
    });

    /**
     * Prevent scroll on body when sidebar is open (iOS fix)
     */
    let scrollPosition = 0;
    
    // Only setup observer if body exists
    if (document.body) {
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.attributeName === 'class') {
                    if (document.body.classList.contains('mobile-sidebar-open')) {
                        scrollPosition = window.pageYOffset;
                        document.body.style.overflow = 'hidden';
                        document.body.style.position = 'fixed';
                        document.body.style.top = `-${scrollPosition}px`;
                        document.body.style.width = '100%';
                    } else {
                        document.body.style.removeProperty('overflow');
                        document.body.style.removeProperty('position');
                        document.body.style.removeProperty('top');
                        document.body.style.removeProperty('width');
                        window.scrollTo(0, scrollPosition);
                    }
                }
            });
        });

        observer.observe(document.body, {
            attributes: true
        });
    }

})();
