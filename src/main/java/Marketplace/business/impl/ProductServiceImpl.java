package Marketplace.business.impl;

import Marketplace.business.ProductService;
import Marketplace.domain.Product.CreateProductRequest;
import Marketplace.domain.Product.CreateProductResponse;
import Marketplace.domain.Product.UpdateProductRequest;
import Marketplace.persistence.ProductRepository;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.domain.Product.Product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request){
        ProductEntity createdProduct = ProductEntity.builder()
                .name(request.getProductName())
                .description(request.getProductDescription())
                .categoryId(request.getCategoryId())
                .attributeValues(request.getCategoryAttributes())
                .build();

        createdProduct = productRepository.saveProduct(createdProduct);

        return CreateProductResponse.builder()
                .productId(createdProduct.getId())
                .categoryId(createdProduct.getCategoryId())
                .build();
    }

    @Override
    public Optional<Product> getProduct(long productId){
        return productRepository.getProduct(productId)
                .map(productConverter::convertToDomain);
    }

    @Override
    public boolean updateProduct(UpdateProductRequest request){
        Optional<ProductEntity> productOptional = this.productRepository.getProduct(request.getId());

        if(productOptional.isPresent()){
            ProductEntity product = productOptional.get();
            product.setName(request.getProductName());
            product.setDescription(request.getProductDescription());
            product.setAttributeValues(request.getCategoryAttributes());

            productRepository.saveProduct(product);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteProduct(long productId){

        return this.productRepository.deleteProduct(productId);
    }

    @Override
    public List<Product> getProducts(){
        return this.productRepository.getProducts().stream()
                .map(productConverter::convertToDomain)
                .collect(Collectors.toList());
    }
}
