package xact.idea.lecturepos.Model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SalesModel {

    @SerializedName("data")
    public List<SalesMaster> data;

    public static  class SalesMaster {
        @SerializedName("InvoiceId")
        public String InvoiceId;
        @SerializedName("TrnType")
        public String TrnType;
        @SerializedName("Status")
        public String Status;
        @SerializedName("InvoiceNumber")
        public String InvoiceNumber;
        @SerializedName("StoreId")
        public String StoreId;
        @SerializedName("Discount")
        public String Discount;
        @SerializedName("InvoiceAmount")
        public String InvoiceAmount;
        @SerializedName("RetailCode")
        public String RetailCode;
        @SerializedName("NetValue")
        public String NetValue;
        @SerializedName("PayMode")
        public String PayMode;
        @SerializedName("Device")
        public String Device;
        @SerializedName("CustomerName")
        public String CustomerName;
        @SerializedName("Note")
        public String Note;
        @SerializedName("InvoiceDate")
        public String InvoiceDate;
        @SerializedName("Phone")
        public String Phone;
        @SerializedName("UpdNo")
        public String UpdNo;
        @SerializedName("UpdDate")
        public String UpdDate;
        @SerializedName("ReturnAmt")
        public String ReturnAmt;

        public List<SalesDetails> salesDetails;

        public static  class SalesDetails {
            @SerializedName("StoreId")
            public String StoreId;
            @SerializedName("InvoiceId")
            public String InvoiceId;
            @SerializedName("BookId")
            public String BookId;
            @SerializedName("BookName")
            public String BookName;
            @SerializedName("Quantity")
            public String Quantity;
            @SerializedName("MRP")
            public String MRP;
            @SerializedName("Discount")
            public String Discount;
            @SerializedName("DiscountPc")
            public String DiscountPc;
            @SerializedName("TotalAmount")
            public String TotalAmount;
            @SerializedName("UpdNo")
            public String UpdNo;
            @SerializedName("UpdDate")
            public String UpdDate;
        }
    }


}
