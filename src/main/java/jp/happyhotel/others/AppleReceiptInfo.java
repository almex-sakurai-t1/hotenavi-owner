package jp.happyhotel.others;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleReceiptInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3462047790168609892L;
    public int                quantity;
    public String             product_id;
    public String             transaction_id;
    public String             original_transaction_id;
    public String             purchase_date;
    public String             original_purchase_date;
    public String             expires_date;
    public long               expires_date_ms;
    public String             cancellation_date;
    public String             app_item_id;
    public String             version_external_identifier;
    public String             web_order_line_item_id;
}
