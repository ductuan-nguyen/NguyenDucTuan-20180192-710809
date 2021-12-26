package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.ShippingFeeCalculator;
import utils.WeightBasedFeeCalculator;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());
    private final ShippingFeeCalculator shippingFeeCalculator = new WeightBasedFeeCalculator();
    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws IOException{
        if(!validateName(info.get("name"))){
            throw new IOException("Name is invalid");
        }
        if(!validateAddress(info.get("address"))){
            throw new IOException("Address is invalid");
        }
        if(!validatePhoneNumber(info.get("phone"))){
            throw new IOException("Phone number is invalid");
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber.length() != 10) return false;
        if(!phoneNumber.startsWith("0")) return false;
        try {
            Integer.parseInt(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean validateName(String name) {
        if(name == null) return false;
        return name.matches("[a-zA-Z]+");
    }

    public boolean validateAddress(String address) {
        if(address == null) return false;
        return address.matches("[a-zA-Z0-9 ]+");
    }

    public int calculateShippingFee(Order order) {
        return shippingFeeCalculator.calculateShippingFee(order);
    }
}
