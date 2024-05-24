package Marketplace.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface WebsiteStatisticsService {
    long getTotalUsers();
    long getNewUsers(Date startDate, Date endDate);
    long getActiveUsers(Date startDate, Date endDate);
    List<Object[]> getUserDemographicsByAge();
    List<Object[]> getUserDemographicsByGender();
    List<Object[]> getUserDemographicsByLocation();
    long getTotalProducts();
    long getPromotedProducts();
    BigDecimal getTotalSales(Date startDate, Date endDate);
    BigDecimal getAverageOrderValue(Date startDate, Date endDate);
    List<Object[]> getSalesByCategory(Date startDate, Date endDate);
    List<Object[]> getMonthlyRevenue(Date startDate, Date endDate);
    List<Object[]> getTransactionsByType(Date startDate, Date endDate);
    List<Object[]> getProductListingsByCategory();
}
