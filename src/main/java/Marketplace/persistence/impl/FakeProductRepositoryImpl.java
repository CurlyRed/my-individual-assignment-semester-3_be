package Marketplace.persistence.impl;

import Marketplace.persistence.ProductRepository;
import Marketplace.persistence.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeProductRepositoryImpl implements ProductRepository {

    private static long NEXT_ID = 1;
    private final List<ProductEntity> products;

    public FakeProductRepositoryImpl() {
        this.products = new ArrayList<>();
    }

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        if (product.getId() == null) {
            product.setId(NEXT_ID);
            NEXT_ID++;
            this.products.add(product);
        }
        return product;
    }

    @Override
    public boolean deleteProduct(long productId) {
        return this.products.removeIf(productEntity -> productEntity.getId().equals(productId));
    }

    @Override
    public Optional<ProductEntity> getProduct(long productId) {
        return this.products.stream()
                .filter(productEntity -> productEntity.getId().equals(productId))
                .findFirst();
    }

    @Override
    public List<ProductEntity> getProducts() {
        return new ArrayList<>(this.products);
    }
}

