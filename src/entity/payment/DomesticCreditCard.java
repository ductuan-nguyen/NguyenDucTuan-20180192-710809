package entity.payment;

/**
 * @author Nguyen Duc Tuan - 20180192
 */
public class DomesticCreditCard extends PaymentCard{
    private String cardCode;
    private String owner;
    private String validDate;
    private String issuingBank;

    public DomesticCreditCard(String cardCode, String owner, String validDate, String issuingBank){
        this.cardCode= cardCode;
        this.owner = owner;
        this.validDate = validDate;
        this.issuingBank = issuingBank;
    }
}
