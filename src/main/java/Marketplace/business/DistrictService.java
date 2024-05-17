package Marketplace.business;

import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.domain.District;

import java.util.List;

public interface DistrictService {
    List<District> getDistricts();
    GetLocationForProductResponse getLocationForProduct(long productId);
}
