package Marketplace.business.impl;

import Marketplace.business.ProductService;
import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.business.dto.product.UpdateProductRequest;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.logger.LoggerUtil;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Attribute;
import Marketplace.domain.Product;
import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.converter.CityConverter;
import Marketplace.persistence.converter.DistrictConverter;
import Marketplace.persistence.converter.ProductConverter;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Log log = LogFactory.getLog(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ProductConverter productConverter;
    private final ProductAttributeRepository productAttributeRepository;
    private final ContactInformationRepository contactInformationRepository;
    private final AccessToken requestAccessToken;
    private final DistrictRepository districtRepository;
    private final DistrictConverter districtConverter;
    private final CityConverter cityConverter;
    private final LoggerUtil loggerUtil;

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        CityEntity cityEntity = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
        UserEntity userEntity = userRepository.findById(requestAccessToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!Objects.equals(userEntity.getId(), requestAccessToken.getUserId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getProductName())
                .description(request.getProductDescription())
                .price(request.getProductPrice())
                .date_of_post(request.getDateOfPost())
                .category(categoryEntity)
                .city(cityEntity)
                .user(userEntity)
                .build();

        final ProductEntity savedProductEntity = productRepository.save(productEntity);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (int i = 0; i < request.getAttributes().size(); i++) {
            AttributeEntity attribute = categoryEntity.getAttributes().get(i);
            String value = request.getAttributes().get(i).getValue();

            ProductAttributeEntity productAttributeEntity = ProductAttributeEntity.builder()
                    .value(value)
                    .attribute(attribute)
                    .product(savedProductEntity)
                    .build();
            productAttributes.add(productAttributeEntity);
        }

        productAttributeRepository.saveAll(productAttributes);

        savedProductEntity.setProduct_attributes(productAttributes);
        productRepository.save(savedProductEntity);

        ContactInformationEntity contactInformationEntity = ContactInformationEntity.builder()
                .contact_person(request.getContact_person())
                .email(request.getEmail())
                .phone_number(request.getPhone_number())
                .product(savedProductEntity)
                .build();

        contactInformationRepository.save(contactInformationEntity);

        return CreateProductResponse.builder()
                .productId(savedProductEntity.getId())
                .categoryId(savedProductEntity.getCategory().getId())
                .build();

    }

    @Override
    @Transactional
    public Optional<Product> getProduct(long productId){
        return productRepository.findById(productId)
                .map(productConverter::toDomain);
    }

    @Override
    @Transactional
    public void updateProduct(long productId, UpdateProductRequest request) {
        loggerUtil.logINFO(String.valueOf(request));
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }

        ProductEntity existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CityEntity cityEntity = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        UserEntity userEntity = userRepository.findById(requestAccessToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!Objects.equals(userEntity.getId(), requestAccessToken.getUserId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        ContactInformationEntity contactInformation = existingProduct.getContact_information();
        if (contactInformation == null) {
            throw new IllegalArgumentException();
        }
        contactInformation.setContact_person(request.getContact_person());
        contactInformation.setEmail(request.getEmail());
        contactInformation.setPhone_number(request.getPhone_number());

        existingProduct.setName(request.getProductName());
        existingProduct.setDescription(request.getProductDescription());
        existingProduct.setPrice(request.getProductPrice());
        existingProduct.setCity(cityEntity);
        existingProduct.setContact_information(contactInformation);

        productRepository.save(existingProduct);
    }


    @Override
    @Transactional
    public boolean deleteProduct(long productId){
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public List<Product> getProducts() {
        return this.productRepository.findAll().stream()
                .map(productConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Product> getProductsForUser(long userId) {
        if (requestAccessToken.getUserId() != userId) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }
        return this.productRepository.findByUserId(userId).stream()
                .map(productConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Product> getProductsForCategory(long categoryId) {
        if(productRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        return this.productRepository.findByCategoryId(categoryId).stream()
                .map(productConverter::toDomain)
                .collect(Collectors.toList());
    }
}
