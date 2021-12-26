package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

	@FXML
	private Label screenTitle;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField instructions;

	@FXML
	private ComboBox<String> province;

	@FXML
	private Text rushLabel;

	@FXML
	private Label deliveryDate;

	@FXML
	private DatePicker datePicker;

	private Order order;
	private Boolean isRush = false;


	public ShippingScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath);
		this.order = order;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
	}

	public void setVisibleInRush(boolean isVisible){
		this.isRush = isVisible;
		this.rushLabel.setVisible(isVisible);
		this.datePicker.setVisible(isVisible);
		this.deliveryDate.setVisible(isVisible);
	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		// add info to messages
		HashMap messages = new HashMap<>();
		messages.put("name", name.getText());
		messages.put("phone", phone.getText());
		messages.put("address", address.getText());
		messages.put("instructions", instructions.getText());
		messages.put("province", province.getValue());
		if(isRush) {
			messages.put("date", datePicker.getValue().toString());
		}
		try {
			if (isRush) {
				getRushController().processDeliveryInfo(messages);
			} else {
				getBController().processDeliveryInfo(messages);
			}
		} catch (IOException e){
			PopupScreen.error(e.getMessage());
			return;
		}
	
		// calculate shipping fees
		int shippingFees = getBController().calculateShippingFee(order);
		order.setShippingFees(shippingFees);
		order.setDeliveryInfo(messages);
		
		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public PlaceRushOrderController getRushController() {
		return (PlaceRushOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}

}
