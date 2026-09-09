package com.shop.producttrialmaster.config;

import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.enums.InventoryStatus;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Jette des données de démo en base au démarrage (H2 étant en mémoire, elle est vide à chaque redémarrage).
 * Seeds demo data on startup (H2 being in-memory, it's empty on every restart).
 * Désactivé en profil "test" : les tests d'intégration comptent sur une base vide au départ.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        createUserIfMissing("admin", "Admin", "admin@admin.com", "admin123");
        createUserIfMissing("jdoe", "John", "user@example.com", "user123");
    }

    private void createUserIfMissing(String username, String firstname, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }

        productRepository.saveAll(List.of(
                product("f230fh0g3", "Bamboo Watch", "Product Description", "bamboo-watch.jpg", "Accessories", 65.0, 24, "REF-123-456", 15L, InventoryStatus.INSTOCK, 5.0),
                product("nvklal433", "Black Watch", "Product Description", "black-watch.jpg", "Accessories", 72.0, 61, "REF-123-456", 15L, InventoryStatus.INSTOCK, 4.0),
                product("zz21cz3c1", "Blue Band", "Product Description", "blue-band.jpg", "Fitness", 79.0, 2, "REF-123-456", 15L, InventoryStatus.LOWSTOCK, 3.0),
                product("244wgerg2", "Blue T-Shirt", "Product Description", "blue-t-shirt.jpg", "Clothing", 29.0, 25, "REF-123-456", 15L, InventoryStatus.INSTOCK, 5.0),
                product("h456wer53", "Bracelet", "Product Description", "bracelet.jpg", "Accessories", 15.0, 73, "REF-123-456", 15L, InventoryStatus.INSTOCK, 4.0),
                product("av2231fwg", "Brown Purse", "Product Description", "brown-purse.jpg", "Accessories", 120.0, 0, "REF-123-456", 15L, InventoryStatus.OUTOFSTOCK, 4.0),
                product("bib36pfvm", "Chakra Bracelet", "Product Description", "chakra-bracelet.jpg", "Accessories", 32.0, 5, "REF-123-456", 15L, InventoryStatus.LOWSTOCK, 3.0),
                product("mbvjkgip5", "Galaxy Earrings", "Product Description", "galaxy-earrings.jpg", "Accessories", 34.0, 34, "REF-123-456", 15L, InventoryStatus.INSTOCK, 5.0)
        ));
    }

    private Product product(String code, String name, String description, String image, String category,
                             Double price, Integer quantity, String internalReference, Long shellId,
                             InventoryStatus inventoryStatus, Double rating) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setDescription(description);
        product.setImage(image);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setInternalReference(internalReference);
        product.setShellId(shellId);
        product.setInventoryStatus(inventoryStatus);
        product.setRating(rating);
        return product;
    }
}
