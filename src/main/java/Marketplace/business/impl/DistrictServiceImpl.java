package Marketplace.business.impl;

import Marketplace.business.DistrictService;
import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.domain.District;
import Marketplace.persistence.converter.CityConverter;
import Marketplace.persistence.converter.DistrictConverter;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.DistrictEntity;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.jpaRepository.CityRepository;
import Marketplace.persistence.jpaRepository.DistrictRepository;
import Marketplace.persistence.jpaRepository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final DistrictConverter districtConverter;
    private final CityConverter cityConverter;
    @Override
    public List<District> getDistricts(){
        return districtRepository.findAll().stream()
                .map(districtConverter::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public GetLocationForProductResponse getLocationForProduct(long productId) {
        Optional<ProductEntity> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        Optional<CityEntity> city = cityRepository.findById(product.get().getCity().getId());
        Optional<DistrictEntity> district = districtRepository.findById(city.get().getDistrict().getId());

        return GetLocationForProductResponse.builder()
                .districtName(district.get().getName())
                .cityName(city.get().getName())
                .build();
    }
}
