package Marketplace.business;

import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.domain.Product;
import Marketplace.business.dto.product.UpdateProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest request);

    Optional<Product> getProduct(long productId);

    void updateProduct(long productId ,UpdateProductRequest request);

    boolean deleteProduct(long productId);

    List<Product> getProducts();

    List<Product> getProductsForUser(long userId);

    List<Product> getProductsForCategory(long categoryId);
}

