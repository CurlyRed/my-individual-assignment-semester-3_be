package Marketplace.controller;

import Marketplace.business.ProductService;
import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.business.dto.product.UpdateProductRequest;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.domain.Product;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @RolesAllowed({"USER"})
    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request){
        CreateProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "id") final long productId){
        final Optional<Product> productOptional = productService.getProduct(productId);
        return productOptional.map(product -> ResponseEntity.ok().body(product)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RolesAllowed({"USER", "ADMIN", "SUPPORT"})
    @PutMapping("{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(value = "id") final long productId, @RequestBody @Valid UpdateProductRequest request){
        productService.updateProduct(productId, request);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok().body(products);
    }

    @RolesAllowed({"USER", "ADMIN", "SUPPORT"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductForUser(@PathVariable long userId){
        List<Product> products = productService.getProductsForUser(userId);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsForCategory(@PathVariable long categoryId){
            List<Product> products = productService.getProductsForCategory(categoryId);
            return ResponseEntity.ok().body(products);
    }
}
