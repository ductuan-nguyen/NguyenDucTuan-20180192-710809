package controller;

import entity.order.Order;
import utils.ShippingFeeCalculator;
import utils.WeightBasedFeeCalculator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * controller xu li logic cho uc place rush order
 * @author Nguyen Duc Tuan - 20180192
 */
public class PlaceRushOrderController extends PlaceOrderController{
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());
    private final ShippingFeeCalculator shippingFeeCalculator = new WeightBasedFeeCalculator();
    @Override
    public void validateDeliveryInfo(HashMap<String, String> info) throws IOException {
        if(!validateName(info.get("name"))){
            throw new IOException("Invalid name");
        }
        if(!validateAddress(info.get("address"))){
            throw new IOException("Invalid address");
        }
        if(!validatePhoneNumber(info.get("phone"))){
            throw new IOException("Invalid phone number");
        }
        if(!validateProvince(info.get("province"))) {
            throw new IOException("Province is unsupported for rush delivery.");
        }
        if(!validateReceiveDate(info.get("date"))) {
            throw new IOException("Please choose another date!");
        }
    }

    // Nguyen Duc Tuan - 20180192
    public boolean validateProvince(String province){
        if(province == null || province.isEmpty()){
            return false;
        }
        return province.equals("Hà Nội");
    }

    // Nguyen Duc Tuan - 20180192
    public boolean validateReceiveDate(String date){
        if(date == null) {
            return false;
        }
        return !LocalDate.now().isAfter(LocalDate.parse(date)) && !LocalDate.now().isEqual(LocalDate.parse(date));
    }

    @Override
    public int calculateShippingFee(Order order) {
        return shippingFeeCalculator.calculateShippingFee(order);
    }
}
