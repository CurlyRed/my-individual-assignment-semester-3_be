package Marketplace.controller;

import Marketplace.business.ProductService;
import Marketplace.domain.Product.CreateProductRequest;
import Marketplace.domain.Product.CreateProductResponse;
import Marketplace.domain.Product.Product;

import Marketplace.domain.Product.UpdateProductRequest;
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

    @PutMapping("{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable long id,
                                              @RequestBody @Valid UpdateProductRequest request){
        request.setId(id);
        productService.updateProduct(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok().body(products);
    }
}
