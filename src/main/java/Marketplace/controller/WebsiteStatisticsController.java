package Marketplace.controller;

import Marketplace.business.WebsiteStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class WebsiteStatisticsController {
    private final WebsiteStatisticsService websiteStatisticsService;

    @GetMapping("/total-products")
    public ResponseEntity<Long> getTotalProducts() {
        return ResponseEntity.ok(websiteStatisticsService.getTotalProducts());
    }

    @GetMapping("/promoted-products")
    public ResponseEntity<Long> getPromotedProducts() {
        return ResponseEntity.ok(websiteStatisticsService.getPromotedProducts());
    }

    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(websiteStatisticsService.getTotalUsers());
    }

    @GetMapping("/new-users")
    public ResponseEntity<Long> getNewUsers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getNewUsers(startDate, endDate));
    }

    @GetMapping("/active-users")
    public ResponseEntity<Long> getActiveUsers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getActiveUsers(startDate, endDate));
    }

    @GetMapping("/user-demographics/age")
    public ResponseEntity<List<Object[]>> getUserDemographicsByAge() {
        return ResponseEntity.ok(websiteStatisticsService.getUserDemographicsByAge());
    }

    @GetMapping("/user-demographics/gender")
    public ResponseEntity<List<Object[]>> getUserDemographicsByGender() {
        return ResponseEntity.ok(websiteStatisticsService.getUserDemographicsByGender());
    }

    @GetMapping("/user-demographics/location")
    public ResponseEntity<List<Object[]>> getUserDemographicsByLocation() {
        return ResponseEntity.ok(websiteStatisticsService.getUserDemographicsByLocation());
    }

    @GetMapping("/total-sales")
    public ResponseEntity<BigDecimal> getTotalSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getTotalSales(startDate, endDate));
    }

    @GetMapping("/average-order-value")
    public ResponseEntity<BigDecimal> getAverageOrderValue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getAverageOrderValue(startDate, endDate));
    }

    @GetMapping("/sales-by-category")
    public ResponseEntity<List<Object[]>> getSalesByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getSalesByCategory(startDate, endDate));
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<Object[]>> getMonthlyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getMonthlyRevenue(startDate, endDate));
    }

    @GetMapping("/transactions-by-type")
    public ResponseEntity<List<Object[]>> getTransactionsByType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(websiteStatisticsService.getTransactionsByType(startDate, endDate));
    }

    @GetMapping("/product-listings-by-category")
    public ResponseEntity<List<Object[]>> getProductListingsByCategory() {
        return ResponseEntity.ok(websiteStatisticsService.getProductListingsByCategory());
    }
}
