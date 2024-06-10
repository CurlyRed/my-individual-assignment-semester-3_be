package Marketplace.controller;

import Marketplace.business.DistrictService;
import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.domain.District;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController {
    private final DistrictService districtService;
    @GetMapping
    public ResponseEntity<List<District>> getLocations() {
        List<District> districts = districtService.getDistricts();
        return ResponseEntity.ok().body(districts);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<GetLocationForProductResponse> getLocationForProduct(@PathVariable Long productId) {
        GetLocationForProductResponse response = districtService.getLocationForProduct(productId);
        return ResponseEntity.ok().body(response);
    }
}
