package kz.nik.project01produserredis.service;

import kz.nik.project01produserredis.mapper.UserMapper;
import kz.nik.project01produserredis.model.Cart;
import kz.nik.project01produserredis.model.Product;
import kz.nik.project01produserredis.repository.CartRepository;
import kz.nik.project01produserredis.repository.ProductRepository;
import kz.nik.project01produserredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;



    public Cart addProductToCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        cart.getProducts().add(product);
        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());
        return cartRepository.save(cart);
    }

    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    public List<Product> getCartProducts(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cart.getProducts();
    }
}
