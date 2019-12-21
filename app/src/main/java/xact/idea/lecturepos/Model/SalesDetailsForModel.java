package xact.idea.lecturepos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesDetailsForModel {
    @SerializedName("data")
    public List<Data> data;

    @SerializedName("status_code")
    public int status_code;
    @SerializedName("total_record")
    public int total_record;
    @SerializedName("message")
    public String message;

    public class Data {

        @SerializedName("INVOICE_ID")
        public String INVOICE_ID;
        @SerializedName("ITEM_ID")
        public String ITEM_ID;
        @SerializedName("QTY")
        public String QTY;
        @SerializedName("MRP")
        public String MRP;
        @SerializedName("DISCOUNT_PC")
        public String DISCOUNT_PC;
        @SerializedName("DISCOUNT_AMT")
        public String DISCOUNT_AMT;
        @SerializedName("TOTAL_VALUE")
        public String TOTAL_VALUE;
        @SerializedName("UPD_NO")
        public String UPD_NO;
        @SerializedName("UPD_DATE")
        public String UPD_DATE;

    }
}
