package Marketplace.business;

import Marketplace.domain.Product.CreateProductRequest;
import Marketplace.domain.Product.CreateProductResponse;
import Marketplace.domain.Product.Product;
import Marketplace.domain.Product.UpdateProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest request);

    Optional<Product> getProduct(long productId);

    boolean updateProduct(UpdateProductRequest request);

    boolean deleteProduct(long productId);

    List<Product> getProducts();
}

