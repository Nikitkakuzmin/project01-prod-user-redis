package kz.nik.project01produserredis.api;

import io.swagger.v3.oas.annotations.Operation;
import kz.nik.project01produserredis.model.Product;
import kz.nik.project01produserredis.service.ProductService;
import kz.nik.project01produserredis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @Operation(summary = "Create product")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam String name,
                                                 @RequestParam double price,
                                                 @RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.createProduct(name, price, categoryId));
    }

    @Operation(summary = "All proucts")
    @GetMapping(value = "/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @Operation(summary = "Products by category")
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @Operation(summary = "Get product")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestParam String name,
                                                 @RequestParam double price,
                                                 @RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.updateProduct(id, name, price, categoryId));
    }
}