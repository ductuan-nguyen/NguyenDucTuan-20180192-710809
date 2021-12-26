package views.screen.payment;

import java.io.IOException;
import java.util.Map;

import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;

public class PaymentScreenHandler extends BaseScreenHandler {

	@FXML
	private Label date;

	@FXML
	private Label code;

	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	// Nguyen Duc Tuan - 20180192
	@FXML
	private RadioButton creditCardRadioButton;

	@FXML
	private RadioButton domesticCardRadioButton;

	private Invoice invoice;
	private String paymentMethod = "Credit Card";

	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		
		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				confirmToPayOrder();
				((PaymentController) getBController()).emptyCart();
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});

		ToggleGroup group = new ToggleGroup();
		creditCardRadioButton.setToggleGroup(group);
		domesticCardRadioButton.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(group.getSelectedToggle() != null){
					RadioButton button = (RadioButton) group.getSelectedToggle();
					paymentMethod = button.getText();
					if(button.getText().equals("Domestic Credit Card")){
						date.setText("Valid From");
					} else {
						date.setText("Expiration Date");
					}
					code.setText("Card security code");

				}
			}
		});
	}

	@FXML
	private Label pageTitle;

	@FXML
	private TextField cardNumber;

	@FXML
	private TextField holderName;

	@FXML
	private TextField expirationDate;

	@FXML
	private TextField securityCode;

	void confirmToPayOrder() throws IOException{
		String contents = "pay order";
		PaymentController ctrl = (PaymentController) getBController();
		Map<String, String> response = ctrl.payOrderUsingCreditCard(invoice.getAmount(), contents, cardNumber.getText(), holderName.getText(),
				expirationDate.getText(), securityCode.getText());

		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
		resultScreen.setPreviousScreen(this);
		resultScreen.setHomeScreenHandler(homeScreenHandler);
		resultScreen.setScreenTitle("Result Screen");
		resultScreen.show();
	}

}