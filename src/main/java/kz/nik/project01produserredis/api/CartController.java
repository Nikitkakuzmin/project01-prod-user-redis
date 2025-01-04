package kz.nik.project01produserredis.api;

import io.swagger.v3.oas.annotations.Operation;
import kz.nik.project01produserredis.model.Cart;
import kz.nik.project01produserredis.model.Product;
import kz.nik.project01produserredis.service.CartService;
import kz.nik.project01produserredis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Create cart")
    @PostMapping
    public ResponseEntity<Cart> createCart() {
        return ResponseEntity.ok(cartService.createCart());
    }

    @Operation(summary = "Add product")
    @PostMapping("/{cartId}/add-product")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.addProductToCart(cartId, productId));
    }

    @Operation(summary = "Products by cart")
    @GetMapping("/{cartId}/products")
    public ResponseEntity<List<Product>> getCartProducts(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartProducts(cartId));
    }
}
