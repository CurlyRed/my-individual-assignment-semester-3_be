package Marketplace.business.impl;

import Marketplace.business.WebsiteStatisticsService;
import Marketplace.persistence.jpaRepository.ProductRepository;
import Marketplace.persistence.jpaRepository.TransactionRepository;
import Marketplace.persistence.jpaRepository.UserInformationRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class WebsiteStatisticsServiceImpl implements WebsiteStatisticsService {
    private UserRepository userRepository;
    private UserInformationRepository userInformationRepository;
    private ProductRepository productRepository;
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public long getTotalUsers() {
        return userRepository.countAllUsers();
    }

    @Transactional
    @Override
    public long getNewUsers(Date startDate, Date endDate) {
        return userRepository.countNewUsersSince(startDate);
    }

    @Transactional
    @Override
    public long getActiveUsers(Date startDate, Date endDate) {
        return userRepository.countActiveUsersBetweenDates(startDate, endDate);
    }

    @Transactional
    @Override
    public List<Object[]> getUserDemographicsByAge() {

        return userInformationRepository.countUsersByAge();
    }

    @Transactional
    @Override
    public List<Object[]> getUserDemographicsByGender() {

        return userInformationRepository.countUsersByGender();
    }

    @Transactional
    @Override
    public List<Object[]> getUserDemographicsByLocation() {

        return userInformationRepository.countUsersByLocation();
    }

    @Transactional
    @Override
    public long getTotalProducts() {

        return productRepository.countAllProducts();
    }

    @Transactional
    @Override
    public long getPromotedProducts() {

        return productRepository.countPromotedProducts();
    }

    @Transactional
    @Override
    public BigDecimal getTotalSales(Date startDate, Date endDate) {
        return transactionRepository.sumTotalSales(startDate, endDate);
    }

    @Transactional
    @Override
    public BigDecimal getAverageOrderValue(Date startDate, Date endDate) {
        return transactionRepository.avgOrderValue(startDate, endDate);
    }

    @Transactional
    @Override
    public List<Object[]> getSalesByCategory(Date startDate, Date endDate) {
        return transactionRepository.sumSalesByCategory(startDate, endDate);
    }

    @Transactional
    @Override
    public List<Object[]> getMonthlyRevenue(Date startDate, Date endDate) {
        return transactionRepository.sumMonthlyRevenue(startDate, endDate);
    }

    @Transactional
    @Override
    public List<Object[]> getTransactionsByType(Date startDate, Date endDate) {
        return transactionRepository.countTransactionsByType(startDate, endDate);
    }

    @Transactional
    @Override
    public List<Object[]> getProductListingsByCategory() {

        return productRepository.countProductsByCategory();
    }
}
