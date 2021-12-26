package utils;
import entity.order.Order;

/**
 * @author Nguyen Duc Tuan - 20180192
 */
public interface ShippingFeeCalculator {
    public int calculateShippingFee(Order order);
}
