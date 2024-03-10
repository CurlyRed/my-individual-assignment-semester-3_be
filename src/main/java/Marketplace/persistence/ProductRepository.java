package Marketplace.persistence;

import Marketplace.persistence.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    ProductEntity saveProduct(ProductEntity product);

    boolean deleteProduct(long productId);

    Optional<ProductEntity> getProduct(long productId);

    List<ProductEntity> getProducts();
}
