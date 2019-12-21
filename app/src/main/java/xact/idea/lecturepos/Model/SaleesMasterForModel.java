package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaleesMasterForModel {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int status_code;
    @SerializedName("total_record")
    public int total_record;
    @SerializedName("message")
    public String message;

    public class Data {
        @SerializedName("STORE_ID")
        public String STORE_ID;
        @SerializedName("INVOICE_ID")
        public String INVOICE_ID;
        @SerializedName("INVOICE_NO")
        public String INVOICE_NO;
        @SerializedName("INV_DATE")
        public String INV_DATE;
        @SerializedName("RETAILER_CODE")
        public String RETAILER_CODE;
        @SerializedName("PHONE")
        public String PHONE;
        @SerializedName("NOTE")
        public String NOTE;
        @SerializedName("INVOICE_AMT")
        public String INVOICE_AMT;
        @SerializedName("DISCOUNT")
        public String DISCOUNT;
        @SerializedName("NET_VALUE")
        public String NET_VALUE;
        @SerializedName("PAY_MODE")
        public String PAY_MODE;
        @SerializedName("DEVICE")
        public String DEVICE;
        @SerializedName("UPD_NO")
        public String UPD_NO;
        @SerializedName("UPD_DATE")
        public String UPD_DATE;
    }

}
