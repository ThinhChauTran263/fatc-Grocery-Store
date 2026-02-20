/**
 * Admin Panel Mobile JavaScript
 * Xử lý tương tác cho admin panel trên mobile
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
        initAdminSidebar();
        initMobileToggle();
        initTableScroll();
    }

    /**
     * Initialize Admin Sidebar for Mobile
     */
    function initAdminSidebar() {
        // Create overlay if not exists
        if (!document.querySelector('.admin-sidebar-overlay')) {
            const overlay = document.createElement('div');
            overlay.className = 'admin-sidebar-overlay';
            overlay.id = 'admin-sidebar-overlay';
            document.body.appendChild(overlay);
            
            // Close sidebar when clicking overlay
            overlay.addEventListener('click', closeSidebar);
        }
        
        // Close sidebar when clicking menu items on mobile
        const sidebar = document.querySelector('.admin-sidebar');
        if (sidebar && window.innerWidth < 1024) {
            const menuItems = sidebar.querySelectorAll('a');
            menuItems.forEach(item => {
                item.addEventListener('click', () => {
                    setTimeout(closeSidebar, 100);
                });
            });
        }
    }

    /**
     * Initialize Mobile Toggle Button
     */
    function initMobileToggle() {
        // Create mobile toggle button if not exists
        const adminHeader = document.querySelector('.admin-header');
        if (!adminHeader) return;
        
        if (!document.querySelector('.admin-mobile-toggle')) {
            const toggleBtn = document.createElement('button');
            toggleBtn.className = 'admin-mobile-toggle';
            toggleBtn.innerHTML = '<i class="bi bi-list"></i>';
            toggleBtn.setAttribute('aria-label', 'Toggle Menu');
            
            // Insert at the beginning of header
            adminHeader.insertBefore(toggleBtn, adminHeader.firstChild);
            
            // Add click handler
            toggleBtn.addEventListener('click', toggleSidebar);
        }
    }

    /**
     * Toggle Sidebar
     */
    function toggleSidebar() {
        const sidebar = document.querySelector('.admin-sidebar');
        const overlay = document.querySelector('.admin-sidebar-overlay');
        
        if (!sidebar) return;
        
        const isActive = sidebar.classList.contains('active');
        
        if (isActive) {
            closeSidebar();
        } else {
            openSidebar();
        }
    }

    /**
     * Open Sidebar
     */
    function openSidebar() {
        const sidebar = document.querySelector('.admin-sidebar');
        const overlay = document.querySelector('.admin-sidebar-overlay');
        
        if (sidebar) sidebar.classList.add('active');
        if (overlay) overlay.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    /**
     * Close Sidebar
     */
    function closeSidebar() {
        const sidebar = document.querySelector('.admin-sidebar');
        const overlay = document.querySelector('.admin-sidebar-overlay');
        
        if (sidebar) sidebar.classList.remove('active');
        if (overlay) overlay.classList.remove('active');
        document.body.style.overflow = '';
    }

    /**
     * Initialize Table Scroll Indicator
     */
    function initTableScroll() {
        const tableContainers = document.querySelectorAll('.table-responsive');
        
        tableContainers.forEach(container => {
            // Add scroll indicator
            if (container.scrollWidth > container.clientWidth) {
                container.classList.add('has-scroll');
                
                // Add scroll hint
                if (!container.querySelector('.scroll-hint')) {
                    const hint = document.createElement('div');
                    hint.className = 'scroll-hint';
                    hint.innerHTML = '← Vuốt để xem thêm →';
                    hint.style.cssText = `
                        position: absolute;
                        bottom: 10px;
                        left: 50%;
                        transform: translateX(-50%);
                        background: rgba(6, 182, 212, 0.9);
                        color: white;
                        padding: 6px 12px;
                        border-radius: 20px;
                        font-size: 12px;
                        font-weight: 500;
                        pointer-events: none;
                        opacity: 1;
                        transition: opacity 0.3s ease;
                        z-index: 10;
                    `;
                    container.style.position = 'relative';
                    container.appendChild(hint);
                    
                    // Hide hint after scroll
                    let scrollTimeout;
                    container.addEventListener('scroll', () => {
                        hint.style.opacity = '0';
                        clearTimeout(scrollTimeout);
                        scrollTimeout = setTimeout(() => {
                            hint.remove();
                        }, 300);
                    });
                    
                    // Auto hide after 3 seconds
                    setTimeout(() => {
                        hint.style.opacity = '0';
                        setTimeout(() => hint.remove(), 300);
                    }, 3000);
                }
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
            // Close sidebar on desktop
            if (window.innerWidth >= 1024) {
                closeSidebar();
            }
            
            // Re-check table scroll
            initTableScroll();
        }, 250);
    });

    /**
     * Handle ESC key
     */
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            const sidebar = document.querySelector('.admin-sidebar');
            if (sidebar && sidebar.classList.contains('active')) {
                closeSidebar();
            }
        }
    });

    /**
     * Convert Table to Cards on Small Screens (Optional)
     */
    function convertTableToCards() {
        if (window.innerWidth > 767) return;
        
        const tables = document.querySelectorAll('.admin-table:not(.admin-table--mobile-cards)');
        
        tables.forEach(table => {
            // Check if already converted
            if (table.dataset.converted === 'true') return;
            
            const rows = table.querySelectorAll('tbody tr');
            const headers = Array.from(table.querySelectorAll('thead th')).map(th => th.textContent.trim());
            
            // Create cards container
            const cardsContainer = document.createElement('div');
            cardsContainer.className = 'mobile-cards-view';
            cardsContainer.style.display = 'none';
            
            rows.forEach(row => {
                const cells = row.querySelectorAll('td');
                const card = document.createElement('div');
                card.className = 'mobile-card';
                
                // Build card HTML
                let cardHTML = '<div class="mobile-card__body">';
                cells.forEach((cell, index) => {
                    if (headers[index]) {
                        cardHTML += `
                            <div class="mobile-card__field">
                                <div class="mobile-card__label">${headers[index]}</div>
                                <div class="mobile-card__value">${cell.innerHTML}</div>
                            </div>
                        `;
                    }
                });
                cardHTML += '</div>';
                
                card.innerHTML = cardHTML;
                cardsContainer.appendChild(card);
            });
            
            // Insert cards after table
            table.parentNode.insertBefore(cardsContainer, table.nextSibling);
            table.dataset.converted = 'true';
        });
    }

    // Optional: Enable card view conversion
    // Uncomment to use card view instead of scrollable table
    // convertTableToCards();
    // window.addEventListener('resize', convertTableToCards);

    // Make functions globally available
    window.adminMobile = {
        openSidebar,
        closeSidebar,
        toggleSidebar
    };

})();
