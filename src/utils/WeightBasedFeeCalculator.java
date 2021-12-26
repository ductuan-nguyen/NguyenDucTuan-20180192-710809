package utils;

import entity.order.Order;
import entity.order.OrderMedia;
import utils.ShippingFeeCalculator;

import java.util.List;

public class WeightBasedFeeCalculator implements ShippingFeeCalculator {
    @Override

    // Nguyen Duc Tuan - 20180192
    public int calculateShippingFee(Order order){
        float weight = 0;
        List lstOrderMedia = order.getlstOrderMedia();
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            weight += om.getMedia().getWeight() * om.getQuantity();
        }
        float fees = weight * 10;
        return (int)fees;
    }
}
