package poly.edu.java5_asm.module.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller cho các trang Product (MVC)
 */
@Controller
public class ProductController {

    /**
     * Trang danh sách sản phẩm với filter và search
     * 
     * @param search Từ khóa tìm kiếm (optional)
     * @param model Model để truyền dữ liệu sang view
     * @return Trang products
     */
    @GetMapping("/products")
    public String productsPage(
            @RequestParam(required = false) String search,
            Model model) {
        
        // Truyền từ khóa tìm kiếm sang view để hiển thị
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("searchKeyword", search.trim());
        }
        
        return "module/product/products";
    }
}
