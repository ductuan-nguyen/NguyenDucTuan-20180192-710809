package entity.payment;

import java.sql.Timestamp;

/**
 * @author Nguyen Duc Tuan - 20180192
 */
public class CreditCard extends PaymentCard{
	private String cardCode;
	private String owner;
	private int cvvCode;
	private String dateExpired;

	public CreditCard(String cardCode, String owner, int cvvCode, String dateExpired) {
		this.cardCode = cardCode;
		this.owner = owner;
		this.cvvCode = cvvCode;
		this.dateExpired = dateExpired;
	}
}
