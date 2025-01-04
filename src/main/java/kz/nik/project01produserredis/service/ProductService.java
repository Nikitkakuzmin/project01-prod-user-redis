package kz.nik.project01produserredis.service;

import kz.nik.project01produserredis.mapper.UserMapper;
import kz.nik.project01produserredis.model.Category;
import kz.nik.project01produserredis.model.Product;
import kz.nik.project01produserredis.repository.CategoryRepository;
import kz.nik.project01produserredis.repository.ProductRepository;
import kz.nik.project01produserredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;




    public Product createProduct(String name, double price, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return productRepository.save(Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .build());
    }


    public List<Product> getAllProducts() {

        String cacheKey = "all_products";
        List<Product> cachedProducts = (List<Product>) cacheService.getCachedObject(cacheKey);
        if (cachedProducts != null) {
            return cachedProducts;
        }
        List<Product> allProducts = productRepository.findAll();
        cacheService.cacheObject(cacheKey, allProducts, 1, TimeUnit.DAYS);
        return allProducts;
    }


    public List<Product> getProductsByCategory(Long categoryId) {
        String cacheKey = "category_" + categoryId;
        Object cachedObject = cacheService.getCachedObject(cacheKey);

        List<Product> cachedProducts = null;
        if (cachedObject instanceof List<?>) {
            cachedProducts = (List<Product>) cachedObject;
        }

        if (cachedProducts != null) {
            return cachedProducts;
        }

        List<Product> productsByCategory = productRepository.findByCategoryId(categoryId);
        cacheService.cacheObject(cacheKey, productsByCategory, 1, TimeUnit.DAYS);
        return productsByCategory;
    }

    public Product getProductById(Long id) {
        String cacheKey = "product_" + id;
        Product cachedProduct = (Product) cacheService.getCachedObject(cacheKey);
        if (cachedProduct != null) {
            return cachedProduct;
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            cacheService.cacheObject(cacheKey, product, 1, TimeUnit.DAYS);
        }
        return product;
    }

    public void deleteProduct(Long id) {

        productRepository.findById(id).ifPresent(product -> {
            productRepository.delete(product);
            cacheService.deleteCachedObject("product_" + id);
            cacheService.deleteCachedObject("category_" + product.getCategory());
        });
    }


    public Product updateProduct(Long id, String name, double price, Long categoryId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        return productRepository.save(product);
    }
}
