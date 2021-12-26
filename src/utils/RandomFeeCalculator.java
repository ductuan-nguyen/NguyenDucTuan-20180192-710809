package utils;

import entity.order.Order;
import utils.ShippingFeeCalculator;

import java.util.Random;

public class RandomFeeCalculator implements ShippingFeeCalculator {
    @Override
    // Nguyen Duc Tuan - 20180192
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int) (((rand.nextFloat()*10)/100) * order.getAmount());
        return fees;
    }
}
